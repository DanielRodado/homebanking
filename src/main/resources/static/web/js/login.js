const { createApp } = Vue;

createApp({
    data() {
        return {
            inputEmail: "",
            inputPassword: "",
        };
    },

    created() {
        
    },

    methods: {
        login() {
            axios
                .post("/api/login", `email=${this.inputEmail}&password=${this.inputPassword}`)
                .then((response) => {
                        console.log(response);
                        location.pathname = "/web/pages/accounts.html";
                    })
                .catch((error) => console.log(error));
        },
    },
}).mount("#app");
