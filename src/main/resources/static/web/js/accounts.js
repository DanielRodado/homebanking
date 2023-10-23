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
        logout() {
            axios.post("/api/logout")
                .then(() => {
                    console.log("signed out!!!");
                    location.pathname = "web/pages/login.html";
                });
        },
        addAccount() {
            axios.post("/api/clients/current/accounts")
                .then(() => {
                    this.getClients();
                })
                .catch((error) => {
                    Swal.fire({
                        icon: "error",
                        title: "Error...",
                        text: "No se pueden añadir más cuentas",
                        color: "#fff",
                        background: "#1c2754",
                        confirmButtonColor: "#17acc9",
                    });
                });
        }
    },
}).mount("#app");

/* totalBalance() {
      return this.accounts.reduce(
        (total, account) => total + account.balance,
        0
      );
    }, */
