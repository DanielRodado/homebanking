const { createApp } = Vue;

createApp({
    data() {
        return {
            loans: [],
            loanById: {},
            loanId: 0,
            paymentsOfLoan: [],
            paymentsTo: 0,
            accounts: [],
            selectedAccountTo: "",
            amount: false,
            amountIncrement: false,

            loading: true,
            isAdmin: null
        };
    },

    created() {
        this.getClients();
        this.getData();
        this.getAccounts();
    },

    methods: {
        getClients() {
            axios("/api/clients/current")
                .then(({ data }) => {
                    this.isAdmin = data.admin;
                })
                .catch((error) => console.log(error));
        },
        getData() {
            axios("/api/loans")
                .then(({ data }) => {
                    this.loans = data;
                    this.loans.sort((a, b) => a.id - b.id);
                    this.loanId = this.loans[0].id;

                    this.paymentsOfLoan = this.loans[0].payments;
                    this.loanById = this.loans[0];
                })
                .catch((error) => console.log(error));
        },
        getAccounts() {
            axios("/api/clients/current/accounts")
                .then(({ data }) => {
                    this.accounts = data;
                    this.accounts.sort((a, b) => b.id - a.id);
                    this.selectedAccountTo = this.accounts[0].number;

                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        postLoan() {
            const newLoan = {
                idLoan: this.loanId,
                amount: this.amount || 0,
                payments: this.paymentsTo,
                numberAccountTo: this.selectedAccountTo,
            };
            axios
                .post("/api/loans", newLoan)
                .then(() => {
                    setTimeout(
                        () => (location.pathname = "/web/pages/accounts.html"),
                        2000
                    );
                })
                .catch((error) => this.messageError(error.response.data));
        },
        messageError(message) {
            Swal.fire({
                icon: "error",
                title: "An error has occurred",
                text: message,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                position: "center",
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
        sendLoan() {
            Swal.fire({
                title: "Are you sure?",
                text: "Do you want to apply for this loan?",
                customClass: {
                    popup: 'text-center'
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, apply!",
            }).then((result) => {
                if (result.isConfirmed) {
                    this.postLoan();
                    Swal.fire({
                        title: "Approved plan!",
                        text: "The requested loan has been approved.",
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                }
            });
        },
    },

    computed: {
        changeTypeLoan() {
            this.loanById = this.loans.find((loan) => loan.id == this.loanId);
        },
        maxAmount() {
            this.amount >  this.loanById.maxAmount ? this.amountIncrement = true : this.amountIncrement = false
        },
        changeValuePayments() {
            this.paymentsOfLoan = this.loans.find(
                (loan) => loan.id == this.loanId
            ).payments;
            this.paymentsTo = this.paymentsOfLoan[0];
        }
    },
}).mount("#app");
