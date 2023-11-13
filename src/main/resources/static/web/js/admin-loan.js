const { createApp } = Vue;

createApp({
    data() {
        return {
            typeOfLoan: "",
            maxAmount: 0,
            interestRate: 0,
            payments: 0,
            loading: true,
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
            axios
                .post("/api/loans/create", `nameOfLoan=${this.typeOfLoan}&maxAmount=${this.maxAmount}&interestRate=${this.interestRate}&payment=${this.payments}`)
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
                    Swal.fire({
                        title: "Loan created!",
                        text: `The loan '${this.typeOfLoan}' has been successfully created.`,
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                }
            });
        },
    }
}).mount("#app");
