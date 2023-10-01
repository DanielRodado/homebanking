const { createApp } = Vue;

createApp({

    data() {
        return {
            clientsData: [],
            userData: {},
            inputName: "",
            inputLastName: "",
            inputEmail: "",
        }
    },

    created() {
        this.getClients();
    },

    methods: {
        addClient() {
            this.userData = {
                firstName: this.inputName,
                lastName: this.inputLastName,
                email: this.inputEmail
            };
            this.postClient();
        },

        getClients() {
            axios("http://localhost:8080/clients")
            .then(({data}) => {
                this.clientsData = data._embedded.clients;
            })
            .catch(error => console.log(error));
        },

        postClient() {
            axios
        .post("http://localhost:8080/clients", this.userData)
        .then(response => console.log(response))
        .catch(error => console.log(error));
        }
    }


}).mount("#app");