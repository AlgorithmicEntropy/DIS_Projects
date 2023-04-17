package de.dis.data;

import de.dis.cli.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Estate extends AbstractDataObject {
    public static final java.lang.String CITY = "city";
    public static final java.lang.String POSTAL_CODE = "postal_code";
    public static final java.lang.String STREET = "street";
    public static final java.lang.String STREET_NUMBER = "street_number";
    public static final java.lang.String SQUARE_AREA = "square_area";
    private static final String AGENT_ID = "agent_id";

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

    public String getTableName() {
        return "estates";
    }

    @Override
    String getIdName() {
        return "id";
    }

    @Override
    int getIdValue() {
        return getId();
    }

    @Override
    void setIdValue(int newId) {
        setId(newId);
    }

    public static Estate load(int id) {
        Estate estate = new Estate();
        return loadInternal(id, estate);
    }

    protected void setValues(PreparedStatement stmt) throws SQLException {
        List<String> columns = getDBFields();
        stmt.setString(columns.indexOf(CITY) + 1, city);
        stmt.setInt(columns.indexOf(POSTAL_CODE) + 1, postalCode);
        stmt.setString(columns.indexOf(STREET) + 1, street);
        stmt.setString(columns.indexOf(STREET_NUMBER) + 1, streetNumber);
        stmt.setDouble(columns.indexOf(SQUARE_AREA) + 1, squareArea);
        stmt.setInt(columns.indexOf(AGENT_ID) + 1, agentId);
    }

    @Override
    protected void loadValues(ResultSet rs) throws SQLException {
        this.setCity(rs.getString(CITY));
        this.setPostalCode(rs.getInt(POSTAL_CODE));
        this.setStreet(rs.getString(STREET));
        this.setStreetNumber(rs.getString(STREET_NUMBER));
        this.setSquareArea(rs.getDouble(SQUARE_AREA));
        this.setAgentId(rs.getInt(AGENT_ID));
    }

    public List<String> getDBFields() {
        return List.of(CITY, POSTAL_CODE, STREET, STREET_NUMBER, SQUARE_AREA, AGENT_ID);
    }

    @Override
    public void delete() {
        super.delete();
    }

    public void save() {
        insertOrUpdate();
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

