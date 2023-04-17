package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Person extends AbstractDataObject {
    private static final String ID = "id";
    private static final String FIRST_NAME= "first_name";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    private int id = -1;
    private String firstName;
    private String name;
    private String address;

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save() {
        insertOrUpdate();
    }

    public static Person load(int id) {
        Person person = new Person();
        person.setId(id);
        return loadInternal(id, person);
    }

    public static List<Person> getAll() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT id FROM persons";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            var people = new ArrayList<Person>();
            while (rs.next()) {
                var id = rs.getInt(1);
                people.add(Person.load(id));
            }
            rs.close();
            pstmt.close();
            return people;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Person {" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    List<String> getDBFields() {
        return List.of(FIRST_NAME, NAME, ADDRESS);
    }

    @Override
    String getTableName() {
        return "persons";
    }

    @Override
    String getIdName() {
        return ID;
    }

    @Override
    int getIdValue() {
        return getId();
    }

    @Override
    void setIdValue(int newId) {
        setId(newId);
    }

    @Override
    void setValues(PreparedStatement stmt) throws SQLException {
        List<String> dbFields = getDBFields();
        stmt.setString(dbFields.indexOf(FIRST_NAME) + 1, firstName);
        stmt.setString(dbFields.indexOf(NAME) + 1, name);
        stmt.setString(dbFields.indexOf(ADDRESS) + 1, address);
    }

    @Override
    void loadValues(ResultSet rs) throws SQLException {
        this.setFirstName(rs.getString(FIRST_NAME));
        this.setName(rs.getString(NAME));
        this.setAddress(rs.getString(ADDRESS));
    }
}

