const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            loading: true,
            typeAccount: "",
            idAccountDelete: 0,
            isAdmin: null,
            loader: false,
        };
    },

    created() {
        this.getClients();
    },

    methods: {
        getClients() {
            axios("/api/clients/current")
                .then(({ data }) => {
                    this.client = data;
                    this.isAdmin = data.admin;
                    this.loading = false;
                })
                .catch((error) => console.log(error));
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
        addAccount() {
            Swal.fire({
                title: "Are you sure?",
                text: `Are you sure you want to create a '${this.typeAccount}' type account?`,
                customClass: {
                    popup: "text-center",
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
                    this.loader = true;
                    axios
                        .post(
                            "/api/clients/current/accounts",
                            `typeAccount=${this.typeAccount}`
                        )
                        .then(() => {
                            this.getClients();

                            this.loader = false;

                            $('#modelAddAccount').modal('hide');

                            Swal.fire({
                                title: "Done!",
                                text: `'${this.typeAccount}' type account successfully created!`,
                                customClass: {
                                    popup: "text-center",
                                },
                                icon: "success",
                                color: "#fff",
                                background: "#1c2754",
                            });
                        })
                        .catch((error) => {
                            this.loader = false;
                            this.messageError(error.response.data);
                        });
                }
            });
        },
        deleteAccount() {
            Swal.fire({
                title: "Are you sure?",
                text: "Are you sure you want to delete this account?",
                customClass: {
                    popup: "text-center",
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
                    this.loader = true;
                    axios
                        .patch(
                            "/api/clients/current/accounts/delete",
                            `accountId=${this.idAccountDelete}`
                        )
                        .then(() => {
                            this.getClients();

                            this.loader = false;

                            $('#modalDeleteAccount').modal('hide');

                            Swal.fire({
                                title: "Account deleted!",
                                text: "Account successfully deleted.",
                                customClass: {
                                    popup: "text-center",
                                },
                                icon: "success",
                                color: "#fff",
                                background: "#1c2754",
                            });
                        })
                        .catch((error) => {
                            this.loader = false;
                            this.messageError(error.response.data);
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
    },
}).mount("#app");
