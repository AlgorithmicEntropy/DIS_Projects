package de.dis.data;

import de.dis.cli.Session;
import jdk.jshell.spi.ExecutionControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Apartment extends Estate {
    public static final java.lang.String FLOOR = "floor";
    public static final java.lang.String RENT = "rent";
    public static final java.lang.String ROOMS = "rooms";
    public static final java.lang.String BALCONY = "balcony";
    public static final java.lang.String BUILT_IN_KITCHEN = "built_in_kitchen";
    private int floor;
    private double rent;
    private int rooms;
    private boolean balcony;
    private boolean builtInKitchen;

    public Apartment() {
    }

    public Apartment(Estate estate) {
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

    @Override
    protected void setValues(PreparedStatement stmt) throws SQLException {
        super.setValues(stmt);
        List<String> columns = getDBFields();
        stmt.setInt(columns.indexOf(FLOOR) + 1, getFloor());
        stmt.setDouble(columns.indexOf(RENT) + 1, getRent());
        stmt.setInt(columns.indexOf(ROOMS) + 1, getRooms());
        stmt.setBoolean(columns.indexOf(BALCONY) + 1, getBalcony());
        stmt.setBoolean(columns.indexOf(BUILT_IN_KITCHEN) + 1, getBuiltInKitchen());
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(FLOOR, RENT, ROOMS, BALCONY, BUILT_IN_KITCHEN).stream())
                .collect(Collectors.toList());
    }

    public static Apartment load(int id) {
        try {
            var estate = Estate.load(id);
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT floor, rent, rooms, balcony, built_in_kitchen FROM apartments WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                var apartment = new Apartment(estate);
                apartment.setFloor(rs.getInt(FLOOR));
                apartment.setRent(rs.getDouble(RENT));
                apartment.setRooms(rs.getInt(ROOMS));
                apartment.setBalcony(rs.getBoolean(BALCONY));
                apartment.setBuiltInKitchen(rs.getBoolean(BUILT_IN_KITCHEN));
                rs.close();
                pstmt.close();
                return apartment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Estate> loadAll() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT id FROM apartments WHERE agent_id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, Session.getInstance().getAgent().getId());
            ResultSet rs = pstmt.executeQuery();
            var apartments = new ArrayList<Estate>();
            while (rs.next()) {
                var id = rs.getInt(1);
                apartments.add(Apartment.load(id));
            }
            rs.close();
            pstmt.close();
            return apartments;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "floor=" + floor +
                ", rent=" + rent +
                ", rooms=" + rooms +
                ", balcony=" + balcony +
                ", builtInKitchen=" + builtInKitchen +
                ", id=" + id +
                ", agentId=" + agentId +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", squareArea=" + squareArea +
                '}';
    }
}
