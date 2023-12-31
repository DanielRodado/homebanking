const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            account: [],
            loading: true,
            isAdmin: null,
            id: 0
        };
    },

    created() {
        const parameter = location.search;
        const parameterKeyValue = new URLSearchParams(parameter);
        this.id = parameterKeyValue.get("id");
        this.getAccount();
    },

    methods: {
        getAccount() {
            axios("/api/clients/current")
                .then(( {data} ) => {
                    this.client = data;
                    this.isAdmin = data.admin;
                    console.log(data);
                    this.account = this.client.accounts.find(account => account.id == this.id);
                    this.account.transactions.sort((a,b) => b.id - a.id);
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
        }

    },
}).mount("#app");
