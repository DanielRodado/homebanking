const { createApp } = Vue;

createApp({
    data() {
        return {
            loans: [],
            idLoanSelected: null,
            loanSelected: {},
            amount: 0,
            payments: null,
            paymentAmout: null,
            amountReadOnly: null,

            accounts: [],
            fromAccount: "",

            isAdmin: null,
            loading: true,
        };
    },

    created() {
        this.getClients();
    },

    methods: {
        getClients() {
            axios("/api/clients/current")
                .then(({ data }) => {
                    this.loans = data.loans;
                    this.accounts = data.accounts;
                    this.accounts.sort((a, b) => b.balance - a.balance);
                    this.fromAccount = this.accounts[0].number;
                    this.idLoanSelected = this.loans[0].id;

                    this.isAdmin = data.admin;
                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        postPayLoan() {
            Swal.fire({
                title: "Are you sure?",
                text: `You wish to pay ${this.payments} of the loan '${
                    this.loanSelected.name
                }' installments at a price of $${this.amount.toLocaleString()}?`,
                customClass: {
                    popup: "text-center",
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, pay!",
            }).then((result) => {
                if (result.isConfirmed) {
                    const obj = {
                        clientLoanId: this.idLoanSelected,
                        payments: this.payments || 0,
                        amountToPay: parseFloat(this.amount) || 0.0,
                        accountNumber: this.fromAccount,
                    };
                    axios
                        .post("/api/loans/pay", obj)
                        .then(()=>  setTimeout(() => location.pathname = "/web/pages/accounts.html", 1200))
                        .catch((error) =>
                            this.messageError(error.response.data)
                        );
                    Swal.fire({
                        title: "Paid for!!",
                        text: "The payment was successful!",
                        customClass: {
                            popup: "text-center",
                        },
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                }
            });
        },
        messageError(message) {
            Swal.fire({
                icon: "error",
                title: "An error has occurred",
                text: message,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
            });
        },
        logout() {
            axios.post("/api/logout").then(() => {
                console.log("signed out!!!");
                localStorage.setItem(
                    "isAuthenticated",
                    JSON.stringify((this.isAuthenticated = false))
                );
                location.pathname = "web/pages/login.html";
            });
        },
    },

    computed: {
        chamgeLoanSelected() {
            this.loanSelected = this.loans.find(
                (loan) => loan.id == this.idLoanSelected
            );
        },
        changeAmount() {
            this.paymentAmout =
                this.loanSelected.amount / this.loanSelected.payments;
        },
        amountChangePayments() {
            this.amount = (this.paymentAmout * parseInt(this.payments)).toFixed(
                0
            );
        },
        amountRead() {
            this.amountReadOnly =
                this.amount > 0
                    ? `$${this.amount.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`
                    : 0;
        },
    },
}).mount("#app");
