package de.dis.data;

import de.dis.cli.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class House extends Estate {
    public static final java.lang.String FLOORS = "floors";
    public static final java.lang.String PRICE = "price";
    public static final java.lang.String GARDEN = "garden";
    private int floors;
    private double price;
    private boolean garden;

    public House() {}

    public House(Estate estate) {
        setAgentId(estate.getAgentId());
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

    @Override
    public String getTableName() {
        return "houses";
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
    protected void loadValues(ResultSet rs) throws SQLException {
        super.loadValues(rs);
        this.setFloors(rs.getInt(FLOORS));
        this.setPrice(rs.getDouble(PRICE));
        this.setGarden(rs.getBoolean(GARDEN));
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(FLOORS, PRICE, GARDEN).stream())
                .collect(Collectors.toList());
    }


    public static House load(int id) {
        House house = new House();
        house.setId(id);
        return loadInternal(id, house);
    }

    public static List<Estate> loadAll() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT id FROM houses WHERE agent_id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, Session.getInstance().getAgent().getId());
            ResultSet rs = pstmt.executeQuery();
            var houses = new ArrayList<Estate>();
            while (rs.next()) {
                var id = rs.getInt(1);
                houses.add(House.load(id));
            }
            rs.close();
            pstmt.close();
            return houses;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "House {" +
                "id=" + id +
                ", agentId=" + agentId +
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

