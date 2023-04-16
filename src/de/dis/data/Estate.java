package de.dis.data;

import de.dis.cli.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Estate {
    public static final java.lang.String CITY = "city";
    public static final java.lang.String POSTAL_CODE = "postal_code";
    public static final java.lang.String STREET = "street";
    public static final java.lang.String STREET_NUMBER = "street_number";
    public static final java.lang.String SQUARE_AREA = "square_area";

    // TODO an SW: Warum initialisierst du id und agentId aber nicht postalCode?
    // TODO an SW: Sind hier die primitiven Datentypen immer richtig oder brauchen wir vielleicht ab und an die Wrapper-Typen?

    protected int id = -1;
    protected int agentId = -1;
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

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public static Estate load(int id) {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            Estate estate = null;
            PreparedStatement stmt = con.prepareStatement("SELECT city, postal_code, street, street_number, square_area FROM estates WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String city = rs.getString(CITY);
                int postalCode = rs.getInt(POSTAL_CODE);
                String street = rs.getString(STREET);
                String streetNumber = rs.getString(STREET_NUMBER);
                double squareArea = rs.getDouble(SQUARE_AREA);
                estate = new Estate();
                estate.setId(id);
                estate.setCity(city);
                estate.setPostalCode(postalCode);
                estate.setStreet(street);
                estate.setStreetNumber(streetNumber);
                estate.setSquareArea(squareArea);
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
        // TODO fix me
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt;
            if (getId() == -1) {
                stmt = con.prepareStatement("INSERT INTO estates (" + String.join(",", getDBFields()) + ") VALUES ("
                        + getDBFields().stream().map(s -> "?").collect(Collectors.joining(", ")) + ") RETURNING id");
                setValues(stmt);

                var rs = stmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                    setId(id);
                }
            } else {
                java.lang.String fieldsToUpdate = getDBFields().stream().map(s -> s + " = ? ").collect(Collectors.joining(", "));
                stmt = con.prepareStatement("UPDATE estates SET " + fieldsToUpdate + " WHERE id = ?");
                setValues(stmt);
                stmt.setInt(getDBFields().size() + 1, id);
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    protected void setValues(PreparedStatement stmt) throws SQLException {
        List<String> columns = getDBFields();
        stmt.setString(columns.indexOf(CITY) + 1, city);
        stmt.setInt(columns.indexOf(POSTAL_CODE) + 1, postalCode);
        stmt.setString(columns.indexOf(STREET) + 1, street);
        stmt.setString(columns.indexOf(STREET_NUMBER) + 1, streetNumber);
        stmt.setDouble(columns.indexOf(SQUARE_AREA) + 1, squareArea);
    }

    public List<String> getDBFields() {
        return List.of(CITY, POSTAL_CODE, STREET, STREET_NUMBER, SQUARE_AREA);
    }

    public void delete() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String sql = "DELETE FROM estates WHERE id = ?";
            var stm = con.prepareStatement(sql);
            stm.setInt(1, this.id);
            stm.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Estate> loadAll() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT id FROM estates WHERE agent_id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, Session.getInstance().getAgent().getId());
            ResultSet rs = pstmt.executeQuery();
            var estates = new ArrayList<Estate>();
            while (rs.next()) {
                var id = rs.getInt(1);
                estates.add(Estate.load(id));
            }
            rs.close();
            pstmt.close();
            return estates;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Estate{" +
                "id=" + id +
                ", agentId=" + agentId +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", squareArea=" + squareArea +
                '}';
    }
}

