package de.dis.data;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public boolean hasBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public boolean hasBuiltInKitchen() {
        return builtInKitchen;
    }

    public void setBuiltInKitchen(boolean builtInKitchen) {
        this.builtInKitchen = builtInKitchen;
    }

    public void save() {
        super.save();
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO apartments (estate_id, floor, rent, rooms, balcony, built_in_kitchen) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, getId());
            stmt.setInt(2, floor);
            stmt.setDouble(3, rent);
            stmt.setInt(4, rooms);
            stmt.setBoolean(5, balcony);
            stmt.setBoolean(6, builtInKitchen);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
