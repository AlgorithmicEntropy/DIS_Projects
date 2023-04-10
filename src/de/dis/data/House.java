package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class House extends Estate {
    private int floors;
    private double price;
    private boolean garden;

    public House(int id, String city, int postalCode, String street, String streetNumber, double squareArea,
                 int floors, double price, boolean garden) {
        super(id, city, postalCode, street, streetNumber, squareArea);
        this.floors = floors;
        this.price = price;
        this.garden = garden;
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

    public boolean hasGarden() {
        return garden;
    }

    public void setGarden(boolean garden) {
        this.garden = garden;
    }

    public void save() {
        super.save();
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO houses (estate_id, floors, price, garden) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, getId());
            stmt.setInt(2, floors);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, garden);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

