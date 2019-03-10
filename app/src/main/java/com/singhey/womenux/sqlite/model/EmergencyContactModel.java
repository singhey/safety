package com.singhey.womenux.sqlite.model;

public class EmergencyContactModel {

    private String number;
    private String name;
    private String email;
    private String location;
    private int id;


    public EmergencyContactModel(String name) {
        this.name = name;
    }

    public EmergencyContactModel() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EmergencyContactModel{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", id=" + id +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
