const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            loading: true,
            typeAccount: "",
            idAccountDelete: 0,
            isAdmin: null,
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
            axios
                .post(
                    "/api/clients/current/accounts",
                    `typeAccount=${this.typeAccount}`
                )
                .then(() => {
                    this.getClients();
                })
                .catch((error) => {
                    console.log(error);
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
                    axios
                    .post(
                        "/api/clients/current/accounts/delete",
                        `id=${this.idAccountDelete}`
                    )
                    .then(() => {
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
                        this.getClients();
                    })
                    .catch((error) => this.messageError(error.response.data));
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
