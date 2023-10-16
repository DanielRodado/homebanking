const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            loading: true,
        };
    },

    created() {
        this.getClients();
    },

    methods: {
        getClients() {
            axios("http://localhost:8080/api/clients/1")
                .then(({ data }) => {
                    this.client = data;
                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
    },
}).mount("#app");

/* totalBalance() {
      return this.accounts.reduce(
        (total, account) => total + account.balance,
        0
      );
    }, */
