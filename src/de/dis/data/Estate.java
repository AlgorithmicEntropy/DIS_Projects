package de.dis.data;

import java.sql.*;

public class Estate {
    private int id;
    private String city;
    private int postalCode;
    private String street;
    private String streetNumber;
    private double squareArea;

    public Estate(int id, String city, int postalCode, String street, String streetNumber, double squareArea) {
        this.id = id;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.squareArea = squareArea;
    }

    public Estate(String city, int postalCode, String street, String streetNumber, double squareArea) {
        this.id = -1;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.squareArea = squareArea;
    }

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

    public static Estate load(int id) {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            Estate estate = null;
            PreparedStatement stmt = con.prepareStatement("SELECT city, postal_code, street, street_number, square_area FROM estates WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String city = rs.getString("city");
                int postalCode = rs.getInt("postal_code");
                String street = rs.getString("street");
                String streetNumber = rs.getString("street_number");
                double squareArea = rs.getDouble("square_area");
                estate = new Estate(id, city, postalCode, street, streetNumber, squareArea);
            }
            rs.close();
            stmt.close();
            return estate;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void save() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt;
            if (getId() == -1) {
                stmt = con.prepareStatement("INSERT INTO estates (city, postal_code, street, street_number, square_area) VALUES (?, ?, ?, ?, ?) RETURNING id");
                stmt.setString(1, city);
                stmt.setInt(2, postalCode);
                stmt.setString(3, street);
                stmt.setString(4, streetNumber);
                stmt.setDouble(5, squareArea);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                    setId(id);
                }
            } else {
                stmt = con.prepareStatement("UPDATE estates SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area = ? WHERE id = ?");
                stmt.setString(1, city);
                stmt.setInt(2, postalCode);
                stmt.setString(3, street);
                stmt.setString(4, streetNumber);
                stmt.setDouble(5, squareArea);
                stmt.setInt(6, id);
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}

