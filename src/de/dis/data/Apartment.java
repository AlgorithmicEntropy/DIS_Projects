package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Apartment extends Estate {
    private int floor;
    private double rent;
    private int rooms;
    private boolean balcony;
    private boolean builtInKitchen;

    public Apartment(int id, String city, int postalCode, String street, String streetNumber, double squareArea,
                     int floor, double rent, int rooms, boolean balcony, boolean builtInKitchen) {
        super(id, city, postalCode, street, streetNumber, squareArea);
        this.floor = floor;
        this.rent = rent;
        this.rooms = rooms;
        this.balcony = balcony;
        this.builtInKitchen = builtInKitchen;
    }

    public Apartment(String city, int postalCode, String street, String streetNumber, double squareArea,
                     int floor, double rent, int rooms, boolean balcony, boolean builtInKitchen) {
        super(city, postalCode, street, streetNumber, squareArea);
        this.floor = floor;
        this.rent = rent;
        this.rooms = rooms;
        this.balcony = balcony;
        this.builtInKitchen = builtInKitchen;
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

    public void save() {
        super.save();
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO apartments (id, floor, rent, rooms, balcony, built_in_kitchen) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, getId());
            stmt.setInt(2, getFloor());
            stmt.setDouble(3, getRent());
            stmt.setInt(4, getRooms());
            stmt.setBoolean(5, getBalcony());
            stmt.setBoolean(6, getBuiltInKitchen());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
