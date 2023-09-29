package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long id;
    private String firstName;
    private String lastsName;
    private String email;

    public Client() {
    }
    public Client(String firstName, String lastsName, String email) {
        this.firstName = firstName;
        this.lastsName = lastsName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastsName() {
        return lastsName;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastsName(String lasttName) {
        this.lastsName = lasttName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastsName='" + lastsName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
