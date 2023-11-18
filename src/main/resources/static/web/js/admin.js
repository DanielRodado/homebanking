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
            isAdmin: null,

            adminFirstName: "",
            adminLastName: "",
            adminEmail: "",
            adminPass: "",

            emailClientToAdmin: "",

            valueInputPanelAdmin: "Create Loan"
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
                        customClass: {
                            popup: 'text-center'
                        },
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                    this.typeOfLoan = "";
                    this.maxAmount = null;
                    this.interestRate = null;
                    this.payments = [];
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
            this.alertSW("Do you want to create a new type of loan?", "Yes, create!", this.createNewLoan)
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
        },
        createNewAdmin() {
            const obj = {
                firstName: this.adminFirstName,
                lastName: this.adminLastName,
                email: this.adminEmail,
                password: this.adminPass,
                createAdmin: true
            }
            axios
                .post("/api/clients", obj)
                .then(() => {
                    Swal.fire({
                        title: "Admin created!",
                        text: `The admin '${this.adminFirstName} ${this.adminLastName}' has been successfully created.`,
                        customClass: {
                            popup: 'text-center'
                        },
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                    this.adminFirstName = "";
                    this.adminLastName = "";
                    this.adminEmail = "";
                    this.adminPass = "";
                })
                .catch((error) => this.messageError(error.response.data));
        },
        createAdmin() {
            this.alertSW("Do you want to create a new admin?", "Yes, create new admin!", this.createNewAdmin)
        },
        clientToAdmin() {
            axios
                .patch("/api/clients/admin", `clientEmail=${this.emailClientToAdmin}`)
                .then(() => {
                    Swal.fire({
                        title: "Done!",
                        text: `The client with email ${this.emailClientToAdmin} has become admin.`,
                        customClass: {
                            popup: 'text-center'
                        },
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                    this.emailClientToAdmin = "";
                })
                .catch((error) => this.messageError(error.response.data));
        },
        convertClientToAdmin() {
            this.alertSW("Do you want to convert this client to admin?", "Yes, convert!", this.clientToAdmin)
        },
        alertSW(message, buttonText, requestMethod) {
            Swal.fire({
                title: "Are you sure?",
                text: message,
                customClass: {
                    popup: 'text-center'
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: buttonText,
            }).then((result) => {
                if (result.isConfirmed) {
                    requestMethod();
                }
            });
        }

    }
}).mount("#app");
