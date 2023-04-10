package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {
    private String firstName;
    private String name;
    private String address;

    public Person(String firstName, String name, String address) {
        this.firstName = firstName;
        this.name = name;
        this.address = address;
    }

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

    public void save() {
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO people (first_name, name, address) VALUES (?, ?, ?)");
            stmt.setString(1, firstName);
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

