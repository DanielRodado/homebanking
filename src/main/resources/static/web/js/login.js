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
            isAuthenticatedVefified: false,

            loadingLogin: false
        };
    },

    created() {
        this.isAuthenticated = JSON.parse(localStorage.getItem("isAuthenticated")) || false;
    },

    methods: {
        login() {
            this.loadingLogin = true;
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
                    this.loadingLogin = false;
                    console.log(error);
                    if (this.inputEmail === "" || this.inputPassword === "") {
                        Swal.fire({
                            icon: "error",
                            title: "Error...",
                            text: "Fill in all fields",
                            color: "#fff",
                            background: "#1c2754",
                            confirmButtonColor: "#17acc9",
                        });
                    } else {
                        this.inputPassword = "";
                        Swal.fire({
                            icon: "error",
                            title: "Error logging in",
                            text: "Incorrect email address or password",
                            color: "#fff",
                            background: "#1c2754",
                            confirmButtonColor: "#17acc9"
                        });
                    }
                });
        },
        register() {
            this.loadingLogin = true;
            const objRegister = {
                firstName: this.inputNameRegister,
                lastName: this.inputLastNameRegister,
                email: this.inputEmailRegister,
                password: this.inputPasswordRegister,
                createAdmin: false
            }
            axios
                .post(
                    "/api/clients",
                    objRegister
                )
                .then(() => {
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
                    this.loadingLogin = false;
                    this.messageError(error.response.data);
                    }
                );
        },
        logout() {
            axios.post("/api/logout")
                .then(() => {
                    console.log("signed out!!!");
                    localStorage.setItem("isAuthenticated", JSON.stringify(this.isAuthenticated = false));
                    Swal.fire({
                        title: "Closed session",
                        text: "successful logout!",
                        customClass: {
                            popup: 'text-center'
                        },
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
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
                customClass: {
                    popup: 'text-center'
                },
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9"
            });
        }
    },
}).mount("#app");
