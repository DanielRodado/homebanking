const { createApp } = Vue;

createApp({
    data() {
        return {
            accounts: [],
            myAccountsTo: [],
            optionTransaction: "others",
            selectedAccountFrom: "",
            selectedAccountTo: "",
            accountFrom: null,
            amount: null,
            description: "",
            optionsNumber: { style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2 },

            isAdmin: null,
            loading: true,
        };
    },

    created() {
        this.getClients();
        this.getData();
    },

    methods: {
        getClients() {
            axios("/api/clients/current")
                .then(({ data }) => {
                    this.isAdmin = data.admin;
                })
                .catch((error) => console.log(error));
        },
        getData() {
            axios("/api/clients/current/accounts")
                .then(({ data }) => {
                    this.accounts = data;
                    this.accounts.sort((a, b) => b.id - a.id);

                    this.selectedAccountFrom = this.accounts[0].number;
                    this.accountFrom = this.accounts[0];

                    this.myAccountsTo = this.accounts.filter(
                        (account) => account.number !== this.selectedAccountFrom
                    );

                    this.loading = false;
                })
                .catch((error) => console.log(error));
        },
        postTransfer() {
            Swal.fire({
                title: "Are you sure?",
                text: `You want to make a transfer of $${this.amount.toLocaleString()} from ${this.selectedAccountFrom} to ${this.selectedAccountTo}?`,
                customClass: {
                    popup: 'text-center'
                },
                icon: "warning",
                showCancelButton: true,
                color: "#fff",
                background: "#1c2754",
                confirmButtonColor: "#17acc9",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, trasfer!",
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                        .post(
                            "/api/clients/current/transactions",
                            `amount=${this.amount}&description=${this.description}&numberOfAccountFrom=${this.selectedAccountFrom}&numberOfAccountTo=${this.selectedAccountTo}`
                        )

                        .catch((error) =>
                            this.messageError(error.response.data)
                        );
                    Swal.fire({
                        title: "Done!",
                        text: `$${this.amount.toLocaleString()} were transferred to ${this.selectedAccountTo}`,
                        icon: "success",
                        color: "#fff",
                        background: "#1c2754",
                    });
                }
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
                    ? ""
                    : this.myAccountsTo[0].number;
        },
        changeAccount() {
            this.accountFrom = this.accounts.find(
                (account) => account.number === this.selectedAccountFrom
            );
        },
    }
}).mount("#app");
