package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    @Override
    protected void setValues(PreparedStatement stmt) throws SQLException {
        super.setValues(stmt);
        List<String> columns = getDBFields();
        stmt.setInt(columns.indexOf(FLOOR) +1, getFloor());
        stmt.setDouble(columns.indexOf(RENT) +1, getRent());
        stmt.setInt(columns.indexOf(ROOMS) +1, getRooms());
        stmt.setBoolean(columns.indexOf(BALCONY) +1, getBalcony());
        stmt.setBoolean(columns.indexOf(BUILT_IN_KITCHEN) +1, getBuiltInKitchen());
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(FLOOR, RENT, ROOMS, BALCONY, BUILT_IN_KITCHEN).stream())
                .collect(Collectors.toList());
    }
}
