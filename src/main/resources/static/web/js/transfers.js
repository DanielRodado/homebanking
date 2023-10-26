const { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            optionTransaction: "myAccounts"
        };
    },

    created() {},

    methods: {
        getData() {
            axios("/api/clients/currents/accounts")
                .then(({ data }) => {
                    this.accounts = data;
                })
                .catch((error) => console.log(error));
        },
        createCard() {
            axios
                .post(
                    "/api/clients/currents/cards",
                    `cardColor=${this.cardColor}&cardType=${this.cardType}`
                )
                .then(() => {
                    console.log("Card created!");
                    Swal.fire({
                        icon: "success",
                        title: "Card created",
                        text: "Card created",
                        color: "#fff",
                        background: "#1c2754",
                        confirmButtonColor: "#17acc9",
                    });
                })
                .catch((error) => {
                    console.error("Error:", error);
                    this.messageError(error.response.data);
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
