const { createApp } = Vue;

createApp({
    data() {
        return {
            account: [],
            transactions: [],
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
            axios(`http://localhost:8080/api/accounts/${this.id}`)
                .then(( {data} ) => {
                    this.account = data;
                    this.transactions = data.transactions;
                    this.transactions.sort((a,b) => b.id - a.id);
                    this.loading = false;
                })
                .catch((error) => console.log(error));
        }

    },
}).mount("#app");

