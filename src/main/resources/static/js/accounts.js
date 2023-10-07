const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            loading: true,
        };
    },

    mounted() {
        
    },

    created() {
        this.getClients();
    },

    methods: {
        getClients() {
            axios("http://localhost:8080/api/clients/1")
                .then(({ data }) => {
                    this.client = data;
                    setTimeout(() => this.loading = false, 200);
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
