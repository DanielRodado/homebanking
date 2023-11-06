const { createApp } = Vue;

createApp({
    data() {
        return {
            cardType: "Type",
            cardColor: "",
            userName: "",
            date: new Date,
            loading: true
        };
    },

    created() {
        axios("/api/clients/current")
            .then(({ data }) => {
                this.userName = data.firstName + " " + data.lastName;
                this.loading = false;
            })
            .catch((error) => console.log(error));

        const dates = new Date;
        dates.setFullYear(dates.getFullYear() + 5)
        dates.get
        this.date = `${dates.getFullYear().toString()}-${(new Date().getMonth() + 1).toString()}-${dates.getDate()}`;
    },

    methods: {
        createCard() {
            axios
                .post(
                    "/api/clients/current/cards",
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
                    location.pathname = "/web/pages/cards.html";
                })
                .catch((error) => {
                    console.error("Error:", error);
                    this.messageError(error.response.data);
                });
        },
        colorCards() {
            if (this.cardColor === "GOLD")
                return {
                    background:
                        "linear-gradient(to right, #ffd700 0%, #e5aa00 100%)",
                };
            else if (this.cardColor === "SILVER")
                return {
                    background:
                        "linear-gradient(to right, #c0c0c0 0%, #a6a6a6 100%)",
                };
            else if (this.cardColor === "TITANIUM")
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
