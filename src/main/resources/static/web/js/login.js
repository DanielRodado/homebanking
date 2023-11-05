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

            isAuthenticated: false,
            isAuthenticatedVefified: false
        };
    },

    created() {
        this.isAuthenticated = JSON.parse(localStorage.getItem("isAuthenticated")) || false;
    },

    methods: {
        login() {
            axios
                .post(
                    "/api/login",
                    `email=${this.inputEmail}&password=${this.inputPassword}`
                )
                .then((response) => {
                    console.log(response);
                    this.saveAuthenticationInLocalStorage();
                    location.pathname = "/web/pages/accounts.html";
                })
                .catch((error) => {
                    console.log(error);
                    if (this.inputEmail = "" || this.inputPassword === "") {
                        Swal.fire({
                            icon: "error",
                            title: "Error...",
                            text: "Fill in all fields",
                            color: "#fff",
                            background: "#1c2754",
                            confirmButtonColor: "#17acc9",
                        });
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Invalid user",
                            text: "This user is not registered",
                            color: "#fff",
                            background: "#1c2754",
                            confirmButtonColor: "#17acc9"
                        });
                    }
                });
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
                            this.saveAuthenticationInLocalStorage();
                            location.pathname = "/web/pages/accounts.html";
                        })
                        .catch((error) => console.log(error));
                })
                .catch((error) => {
                        console.error("Error registering user:", error);
                        this.messageError(error.response.data);
                    }
                );
        },
        logout() {
            axios.post("/api/logout")
                .then(() => {
                    console.log("signed out!!!");
                    localStorage.setItem("isAuthenticated", JSON.stringify(this.isAuthenticated = false));
                });
        },
        manageAccess() {
            if(this.isAuthenticated) this.logout();
            else location.pathname = "web/pages/login.html";
        },
        saveAuthenticationInLocalStorage() {
            localStorage.setItem("isAuthenticated", JSON.stringify(this.isAuthenticated = true));
        },
        messageError(message) {
            Swal.fire({
                icon: "error",
                title: "An error has occurred",
                text: message,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9"
            });
        }
    },
}).mount("#app");
