const { createApp } = Vue;

createApp({
    data() {
        return {
            cardType: "Type",
            cardColor: "",
            userName: "",
            date: new Date(),
            loading: true,
            sendCard: false,
            isAdmin: null,
            loader: false,
        };
    },

    created() {
        axios("/api/clients/current")
            .then(({ data }) => {
                this.userName = data.firstName + " " + data.lastName;
                this.loading = false;
                this.isAdmin = data.admin;
            })
            .catch((error) => console.log(error));

        const dates = new Date();
        dates.setFullYear(dates.getFullYear() + 5);
        dates.get;
        this.date = `${dates.getFullYear().toString()}-${(
            new Date().getMonth() + 1
        ).toString()}-${dates.getDate()}`;
    },

    methods: {
        createCard() {
            Swal.fire({
                title: "Are you sure?",
                text: `You want to create a card of type ${this.cardType} and color ${this.cardColor}?`,
                customClass: {
                    popup: "text-center",
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, create!",
            }).then((result) => {
                if (result.isConfirmed) {
                    this.loader = true;
                    axios
                        .post(
                            "/api/clients/current/cards",
                            `cardColor=${this.cardColor}&cardType=${this.cardType}`
                        )
                        .then(() => {
                            this.loader = false;
                            Swal.fire({
                                title: "Created!",
                                text: `Successfully created ${this.cardColor}-colored, ${this.cardType}-type card.`,
                                icon: "success",
                                color: "#fff",
                                background: "#1c2754",
                            });
                            setTimeout(
                                () =>
                                    (location.pathname =
                                        "/web/pages/cards.html"),
                                1500
                            );
                        })
                        .catch((error) => {
                            this.loader = false;
                            this.messageError(error.response.data);
                        });
                }
            });
        },
        colorCards() {
            if (this.cardColor === "GOLD")
                return {
                    background:
                        "linear-gradient(222deg, #D17D25 30%, #F5D628 70%)",
                };
            else if (this.cardColor === "SILVER")
                return {
                    background:
                        "linear-gradient(226deg, #C0C1C3 40%, #737575 70%)",
                };
            else if (this.cardColor === "TITANIUM")
                return {
                    background:
                        "linear-gradient(226deg, #002E68 40%, #01000B 70%)",
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

    computed: {
        send() {
            this.sendCard =
                this.cardType === "CREDIT" ||
                (this.cardType === "DEBIT" && this.cardColor === "GOLD") ||
                this.cardColor === "SILVER" ||
                this.cardColor === "TITANIUM"
                    ? true
                    : false;
        },
    },
}).mount("#app");
