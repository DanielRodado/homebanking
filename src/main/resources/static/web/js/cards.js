const { createApp } = Vue;

createApp({
    data() {
        return {
            cards: [],
            cardsFiltered: [],
            loading: true,
            cards: false,
            valueCard: "ALL",
            dateNow: null,

            idCardDelete: 0,
            isAdmin: null,
            loader: false
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
                    this.filterCards();
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
        },
        colorCards(card) {
            if (card.color === "GOLD")
                return {
                    background:
                        "linear-gradient(222deg, #D17D25 30%, #F5D628 70%)",
                };
            else if (card.color === "SILVER")
                return {
                    background:
                        "linear-gradient(226deg, #C0C1C3 40%, #737575 70%)",
                };
            else if (card.color === "TITANIUM")
                return {
                    background:
                        "linear-gradient(226deg, #002E68 40%, #01000B 70%)",
                };
        },
        logout() {
            axios.post("/api/logout").then(() => {
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
                    this.loader = true;
                    axios
                        .patch(
                            "/api/clients/current/cards/delete",
                            `cardId=${this.idCardDelete}`
                        )
                        .then(() => {
                            this.getClients();

                            this.loader = false;

                            $('#modalDeleteCard').modal('hide');

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
                        })
                        .catch((error) => {
                            this.loader = false;
                            $('#modalDeleteCard').modal('hide');
                            this.messageError(error.response.data);
                        });
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
