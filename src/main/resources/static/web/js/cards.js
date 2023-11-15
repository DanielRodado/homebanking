const { createApp } = Vue;

createApp({
    data() {
        return {
            cards: [],
            cardsFiltered: [],
            loading: true,
            cards: false,
            valueCard: "",
            dateNow: null,

            idCardDelete: 0,
            isAdmin: null,
        };
    },

    created() {
        this.getClients();
        this.dateNow = this.formattedDateCard();
    },

    methods: {
        getClients() {
            axios("/api/clients/current")
                .then(({ data }) => {
                    this.cards = data.cards;
                    this.isAdmin = data.admin;
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
                return {
                    background:
                        "linear-gradient(to right, #ffd700 0%, #e5aa00 100%)",
                };
            else if (card.color === "SILVER")
                return {
                    background:
                        "linear-gradient(to right, #c0c0c0 0%, #a6a6a6 100%)",
                };
            else if (card.color === "TITANIUM")
                return {
                    background:
                        "linear-gradient(to right, #708090 0%, #4d555f 100%)",
                };
        },
        logout() {
            axios.post("/api/logout").then(() => {
                console.log("signed out!!!");
                localStorage.setItem(
                    "isAuthenticated",
                    JSON.stringify((this.isAuthenticated = false))
                );
                location.pathname = "web/pages/login.html";
            });
        },
        formattedDateCard() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, "0");
            const day = String(today.getDate()).padStart(2, "0");
            return `${year}-${month}-${day}`;
        },
        deleteCard() {
            Swal.fire({
                title: "Are you sure?",
                text: "Are you sure you want to delete this card?",
                customClass: {
                    popup: "text-center",
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, delete!",
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                        .post(
                            "/api/clients/current/cards/delete",
                            `id=${this.idCardDelete}`
                        )
                        .then(() => {
                            Swal.fire({
                                title: "Card deleted!",
                                text: "Card successfully deleted.",
                                customClass: {
                                    popup: "text-center",
                                },
                                icon: "success",
                                color: "#fff",
                                background: "#1c2754",
                            });
                            this.getClients();
                        })
                        .catch((error) =>
                            this.messageError(error.response.data)
                        );
                }
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

/* totalBalance() {
      return this.accounts.reduce(
        (total, account) => total + account.balance,
        0
      );
    }, */
