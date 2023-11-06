const { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            myAccountsTo: [],
            optionTransaction: "others",
            selectedAccountFrom: "",
            selectedAccountTo: "",

            amount: null,
            description: "",

            loading: true
        };
    },

    created() {
        this.getData();
    },

    methods: {
        getData() {
            axios("/api/clients/current/accounts")
                .then(({ data }) => {
                    this.accounts = data;
                    this.accounts.sort((a, b) => b.id - a.id);

                    this.selectedAccountFrom = this.accounts[0].number;

                    this.myAccountsTo = this.accounts.filter(
                        (account) => account.number !== this.selectedAccountFrom
                    );

                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        postTransfer() {
            axios.post(
                "/api/clients/current/transactions",
                `amount=${this.amount}&description=${this.description}&numberOfAccountFrom=${this.selectedAccountFrom}&numberOfAccountTo=${this.selectedAccountTo}`
            )
                .then(() => {
                    Swal.fire({
                        icon: "success",
                        title: "Send",
                        text: "Transaction sent successfully",
                        color: "#fff",
                        background: "#1c2754",
                        confirmButtonColor: "#17acc9",
                    });
                })
                .catch((error) => this.messageError(error.response.data));
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
        logout() {
            axios.post("/api/logout")
                .then(() => {
                    console.log("signed out!!!");
                    localStorage.setItem("isAuthenticated", JSON.stringify(this.isAuthenticated = false));
                    location.pathname = "web/pages/login.html";
                });
        },
    },

    computed: {
        filterAccounts() {
            if (this.optionTransaction === "myAccounts") {
                this.myAccountsTo = this.accounts.filter(
                    (account) => account.number !== this.selectedAccountFrom
                );
                this.selectedAccountTo = this.myAccountsTo[0].number;
            }
        },
        changeValueAccountTo() {
            this.selectedAccountTo =
                this.optionTransaction === "others"
                    ? "" : this.myAccountsTo[0].number;
        },
    },
}).mount("#app");
