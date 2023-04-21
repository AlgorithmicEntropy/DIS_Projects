package de.dis.data;

import java.util.Set;

public class Apartment extends Estate {
    private int floor;
    private double rent;
    private int rooms;
    private boolean balcony;
    private boolean builtInKitchen;
    private Set<Rents> rents;

    public Apartment() {
    }

    public Apartment(Estate estate) {
        setEstateAgent(estate.getEstateAgent());
        setId(estate.getId());
        setCity(estate.getCity());
        setPostalCode(estate.getPostalCode());
        setStreet(estate.getStreet());
        setStreetNumber(estate.getStreetNumber());
        setSquareArea(estate.getSquareArea());
    }

    // getters and setters
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public boolean getBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public boolean getBuiltInKitchen() {
        return builtInKitchen;
    }

    public void setBuiltInKitchen(boolean builtInKitchen) {
        this.builtInKitchen = builtInKitchen;
    }

    public Set<Rents> getRents() {
        return rents;
    }

    public void setRents(Set<Rents> rents) {
        this.rents = rents;
    }

    @Override
    public String toString() {
        return "Apartment {" +
                "floor=" + floor +
                ", rent=" + rent +
                ", rooms=" + rooms +
                ", balcony=" + balcony +
                ", builtInKitchen=" + builtInKitchen +
                ", id=" + id +
                ", agentId=" + agent.getId() +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", squareArea=" + squareArea +
                '}';
    }

}
