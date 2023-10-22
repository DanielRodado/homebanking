const { createApp } = Vue;

createApp({
    data() {
        return {
            inputName: "",
            inputLastName: "",
            inputEmail: "",
            inputPassword: "",
        };
    },

    created() {},

    methods: {
        register() {
            axios
                .post(
                    "/api/clients",
                    `firstName=${this.inputName}&lastName=${this.inputLastName}&email=${this.inputEmail}&password=${this.inputPassword}`
                )
                .then(() => {
                    console.log("User registered");

                    axios
                        .post(
                            "/api/login",
                            `email=${this.inputEmail}&password=${this.inputPassword}`
                        )
                        .then((response) => {
                            console.log(response);
                            location.pathname = "/web/pages/accounts.html";
                        })
                        .catch((error) => console.log(error));
                })
                .catch((error) => {
                    console.error("Error registering user:", error);
                });
        },
    },
}).mount("#app");
