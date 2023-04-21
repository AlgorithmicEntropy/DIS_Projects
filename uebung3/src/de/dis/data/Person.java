package de.dis.data;

import java.util.Set;

public class Person extends Entity {
    private int id = -1;
    private String firstName;
    private String name;
    private String address;
    private Set<Apartment> rents;
    private Set<House> sells;

    // getters and setters
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Set<Apartment> getRents() {
        return rents;
    }
    public void setRents(Set<Apartment> rents) {
        this.rents = rents;
    }
    public Set<House> getSells() {
        return sells;
    }

    public void setSells(Set<House> sells) {
        this.sells = sells;
    }

    @Override
    public String toString() {
        return "Person {" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

