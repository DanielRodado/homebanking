const { createApp } = Vue;

createApp({
    data() {
        return {
            loans: [],
            loanId: 0,
            paymentsOfLoan: [],
            paymentsTo: 0,
            accounts: [],
            selectedAccountTo: "",
            amount: "",

            loading: true,
        };
    },

    created() {
        this.getData();
        this.getAccounts();
    },

    methods: {
        getData() {
            axios("/api/loans")
                .then(({ data }) => {
                    this.loans = data;
                    this.loans.sort((a, b) => a.id - b.id);
                    this.loanId = this.loans[0].id;

                    this.paymentsOfLoan = this.loans[0].payments;

                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        getAccounts() {
            axios("/api/clients/current/accounts")
                .then(({ data }) => {
                    this.accounts = data;
                    this.accounts.sort((a, b) => b.id - a.id);
                    this.selectedAccountTo = this.accounts[0].number;
                })
                .catch((error) => console.log(error));
        },
        postLoan() {
            const newLoan = {
                idLoan: this.loanId,
                amount: this.amount,
                payments: this.paymentsTo,
                numberAccountTo: this.selectedAccountTo,
            };
            axios
                .post("/api/loans", newLoan)
                .then(() => {
                    location.pathname = "/web/pages/accounts.html";
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
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: "btn btn-success",
                    cancelButton: "btn btn-danger",
                },
                buttonsStyling: false,
            });
            swalWithBootstrapButtons
                .fire({
                    title: "Are you sure?",
                    text: "You won't be able to revert this!",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: "Yes, Apply for a loan!",
                    confirmButtonColor: "#17acc9",
                    cancelButtonText: "No, cancel!",
                    reverseButtons: true,
                    background: "#1c2754",
                    color: "#fff",
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        swalWithBootstrapButtons.fire({
                            title: "Loan approved!",
                            text: "Loan approved",
                            icon: "success",
                            background: "#1c2754",
                            color: "#fff",
                        });
                        this.postLoan();
                    } else if (
                        /* Read more about handling dismissals below */
                        result.dismiss === Swal.DismissReason.cancel
                    ) {
                        swalWithBootstrapButtons.fire({
                            title: "Cancelled",
                            text: "Loan application cancelled",
                            icon: "error",
                            background: "#1c2754",
                            color: "#fff",
                        });
                    }
                });
        },
    },

    computed: {
        changeValuePayments() {
            this.paymentsOfLoan = this.paymentsOfLoan =
                this.loans[this.loanId - 1].payments;
            this.paymentsTo = this.paymentsOfLoan[0];
            console.log(this.loanId);
        },
    },
}).mount("#app");
