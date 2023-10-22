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
            axios("/api/clients/currents")
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
