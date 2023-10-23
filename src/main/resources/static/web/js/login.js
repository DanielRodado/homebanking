const { createApp } = Vue;

createApp({
    data() {
        return {
            inputEmail: "",
            inputPassword: "",

            inputNameRegister: "",
            inputLastNameRegister: "",
            inputEmailRegister: "",
            inputPasswordRegister: "",
        };
    },

    created() {},

    methods: {
        login() {
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
        },
        register() {
            axios
                .post(
                    "/api/clients",
                    `firstName=${this.inputNameRegister}&lastName=${this.inputLastNameRegister}&email=${this.inputEmailRegister}&password=${this.inputPasswordRegister}`
                )
                .then(() => {
                    console.log("User registered");

                    axios
                        .post(
                            "/api/login",
                            `email=${this.inputEmailRegister}&password=${this.inputPasswordRegister}`
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

        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        }
    },
}).mount("#app");
