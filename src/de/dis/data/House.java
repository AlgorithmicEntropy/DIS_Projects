package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class House extends Estate {
    public static final java.lang.String FLOORS = "floors";
    public static final java.lang.String PRICE = "price";
    public static final java.lang.String GARDEN = "garden";
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

    @Override
    protected void setValues(PreparedStatement stmt) throws SQLException {
        super.setValues(stmt);
        List<String> columns = getDBFields();
        stmt.setInt(columns.indexOf(FLOORS) +1, floors);
        stmt.setDouble(columns.indexOf(PRICE) +1, price);
        stmt.setBoolean(columns.indexOf(GARDEN) +1, garden);
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(FLOORS, PRICE, GARDEN).stream())
                .collect(Collectors.toList());
    }
}

