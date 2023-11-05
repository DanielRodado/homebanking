const { createApp } = Vue;

createApp({
    data() {
        return {
            cardType: "",
            cardColor: "",
        };
    },

    created() {},

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
                })
                .catch((error) => {
                    console.error("Error:", error);
                    this.messageError(error.response.data);
                });
        },
        logout() {
            axios.post("/api/logout")
                .then(() => {
                    console.log("signed out!!!");
                    localStorage.setItem("isAuthenticated", JSON.stringify(this.isAuthenticated = false));
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
