const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            account: [],
            loading: true,
            id: 0,
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
                    location.href = "http://localhost:8080/web/pages/login.html";
                });
        }

    },
}).mount("#app");

