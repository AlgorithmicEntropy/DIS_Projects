package de.dis.data;


public class Estate extends Entity {

    protected int id = -1;

    protected EstateAgent agent;
    protected String city;
    protected int postalCode;
    protected String street;
    protected String streetNumber;
    protected double squareArea;

    // getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public double getSquareArea() {
        return squareArea;
    }

    public void setSquareArea(double squareArea) {
        this.squareArea = squareArea;
    }

    public EstateAgent getEstateAgent() {
        return agent;
    }

    public void setEstateAgent(EstateAgent agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "Estate{" +
                "id=" + id +
                ", agentId=" + agent.getId() +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", squareArea=" + squareArea +
                '}';
    }
}

