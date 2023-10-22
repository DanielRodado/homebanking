const { createApp } = Vue;

createApp({
    data() {
        return {
            cards: [],
            cardsFiltered: [],
            loading: true,
            cards: false,
            valueCard: "",
        };
    },

    created() {
        this.getClients();
    },

    methods: {
        getClients() {
            axios("/api/clients/currents")
                .then(({ data }) => {
                    this.cards = data.cards;
                    this.cardsFiltered = data.cards;
                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },

        filterCards() {
            if (this.valueCard === "ALL") this.cardsFiltered = this.cards;
            else
                this.cardsFiltered = this.cards.filter(
                    (card) => card.type === this.valueCard
                );
            +console.log(this.cardsFiltered);
        },
        colorCards(card) {
            if (card.color === "GOLD")
                return {'background': 'linear-gradient(to right, #ffd700 0%, #e5aa00 100%)'};
            else if (card.color === "SILVER")
                return {'background': 'linear-gradient(to right, #c0c0c0 0%, #a6a6a6 100%)'};
            else if (card.color === "TITANIUM")
                return {'background': 'linear-gradient(to right, #708090 0%, #4d555f 100%)'};
        },
    },
}).mount("#app");

/* totalBalance() {
      return this.accounts.reduce(
        (total, account) => total + account.balance,
        0
      );
    }, */
