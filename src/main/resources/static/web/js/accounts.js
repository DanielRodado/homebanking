const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            loading: true,
            typeAccount: "",
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
                    this.client = data;
                    this.isAdmin = data.admin;
                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        logout() {
            axios.post("/api/logout")
                .then(() => {
                    console.log("signed out!!!");
                    localStorage.setItem("isAuthenticated", JSON.stringify(this.isAuthenticated = false));
                    location.pathname = "web/pages/login.html";
                });
        },
        addAccount() {
            axios.post("/api/clients/current/accounts", `typeAccount=${this.typeAccount}`)
                .then(() => {
                    this.getClients();
                })
                .catch((error) => {
                    console.log(error);
                });
        }
    },
}).mount("#app");
