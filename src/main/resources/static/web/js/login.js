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
                            confirmButtonColor: "#17acc9",
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
                            location.pathname = "/web/pages/accounts.html";
                        })
                        .catch((error) => console.log(error));
                })
                .catch((error) => {
                    console.error("Error registering user:", error);
                    if (
                        this.inputNameRegister === "" ||
                        this.inputLastNameRegister === "" ||
                        this.inputEmailRegister === "" ||
                        this.inputPasswordRegister === ""
                    ) {
                        Swal.fire({
                            icon: "error",
                            title: "Error...",
                            text: "Fill in all fields",
                            color: "#fff",
                            background: "#1c2754",
                            confirmButtonColor: "#17acc9",
                        });
                    } else if (error.response.status === 403) {
                        Swal.fire({
                            icon: "error",
                            title: "Invalid e-mail address",
                            text: "This e-mail address is already registered",
                            color: "#fff",
                            background: "#1c2754",
                            confirmButtonColor: "#17acc9",
                        });
                    }
                });
        },
    },
}).mount("#app");
