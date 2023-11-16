const { createApp } = Vue;

createApp({
    data() {
        return {
            typeOfLoan: "",
            maxAmount: null,
            interestRate: null,
            payments: null,
            loading: true,
            paymentAdd: "",
            payments: [],
            isAdmin: null
        };
    },

    created() {
        this.getClients();
    },

    methods: {
        getClients() {
            axios("/api/clients/current")
                .then(({ data }) => {
                    this.isAdmin = data.admin;
                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        createNewLoan() {
            const obj = {
                nameOfLoan: this.typeOfLoan,
                maxAmount: this.maxAmount || 0.0,
                interestRate: this.interestRate || 0.0,
                payments: this.payments
            }
            axios
                .post("/api/loans/create", obj)
                .then(() => {
                    Swal.fire({
                        title: "Loan created!",
                        text: `The loan '${this.typeOfLoan}' has been successfully created.`,
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                })
                .catch((error) => this.messageError(error.response.data));
        },
        messageError(message) {
            Swal.fire({
                icon: "error",
                title: "An error has occurred",
                text: message,
                customClass: {
                    popup: 'text-center'
                },
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
        createLoan() {
            Swal.fire({
                title: "Are you sure?",
                text: "Do you want to create a new type of loan?",
                customClass: {
                    popup: 'text-center'
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, create!",
            }).then((result) => {
                if (result.isConfirmed) {
                    this.createNewLoan();
                }
            });
        },
        addPayments() {
            if (this.paymentAdd === "") {
                Swal.fire({
                    icon: "error",
                    title: "Error adding payments",
                    text: "You have not added any payments",
                    customClass: {
                        popup: 'text-center'
                    },
                    color: "#fff",
                    background: "#1c2754",
                    confirmButtonColor: "#17acc9",
                    position: "center",
                });
            }
            else if (this.payments.every(payment => payment < this.paymentAdd)) {
                this.payments.push(parseInt(this.paymentAdd));
                this.paymentAdd = "";
            } else {
                Swal.fire({
                    icon: "error",
                    title: "Error adding payment",
                    text: `You cannot enter a payment less than or equal to the previous one (${this.payments[this.payments.length-1]})`,
                    customClass: {
                        popup: 'text-center'
                    },
                    color: "#fff",
                    background: "#1c2754",
                    confirmButtonColor: "#17acc9",
                });
                this.paymentAdd = "";
            }
        },
        deletePayments() {
            if (this.payments.length === 0) {
                Swal.fire({
                    icon: "error",
                    title: "Error deleting payments",
                    text: "You have not added any payments",
                    customClass: {
                        popup: 'text-center'
                    },
                    color: "#fff",
                    background: "#1c2754",
                    confirmButtonColor: "#17acc9",
                    position: "center",
                });
            } else {
                Swal.fire({
                    title: "Are you sure?",
                    text: "Do you want to delete added payments?",
                    customClass: {
                        popup: 'text-center'
                    },
                    icon: "warning",
                    showCancelButton: true,
                    color: "#fff",
                    background: "#1c2754",
                    confirmButtonColor: "#17acc9",
                    cancelButtonColor: "#d33",
                    confirmButtonText: "Yes, delete!",
                }).then((result) => {
                    if (result.isConfirmed) {
                        this.payments = [];
                        Swal.fire({
                            title: "Payments deleted!",
                            text: "All payments entered have been deleted.",
                            icon: "success",
                            color: "#fff",
                            background: "#1c2754",
                        });
                    }
                });
            }
        }
    }
}).mount("#app");
