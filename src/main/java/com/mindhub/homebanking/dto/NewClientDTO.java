package com.mindhub.homebanking.dto;

public class NewClientDTO {

    private String firstName, lastName, email, password;

    private boolean createAdmin;

    public NewClientDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCreateAdmin() {
        return createAdmin;
    }
}
