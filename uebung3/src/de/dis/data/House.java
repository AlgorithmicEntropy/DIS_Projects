package de.dis.data;

import java.util.Set;

public class House extends Estate {
    private int floors;
    private double price;
    private boolean garden;
    private Set<Sells> sells;

    public House(Estate estate) {
        setEstateAgent(estate.getEstateAgent());
        setId(estate.getId());
        setCity(estate.getCity());
        setPostalCode(estate.getPostalCode());
        setStreet(estate.getStreet());
        setStreetNumber(estate.getStreetNumber());
        setSquareArea(estate.getSquareArea());
    }

    // getters and setters
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean getGarden() {
        return garden;
    }

    public void setGarden(boolean garden) {
        this.garden = garden;
    }

    public Set<Sells> getSells() {
        return sells;
    }

    public void setSells(Set<Sells> sells) {
        this.sells = sells;
    }

    public House() {
    }

    @Override
    public String toString() {
        return "House {" +
                "id=" + id +
                ", agentId=" + agent.getId() +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", squareArea=" + squareArea +
                ", floors=" + floors +
                ", price=" + price +
                ", garden=" + garden +
                '}';
    }

}

