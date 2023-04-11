package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {

    private int id;
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
        try {
            Connection conn = DbConnectionManager.getInstance().getConnection();
            if (getId() == -1) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO people (first_name, name, address) VALUES (?, ?, ?)");
                stmt.setString(1, firstName);
                stmt.setString(2, name);
                stmt.setString(3, address);
                stmt.executeUpdate();
            } else {
                PreparedStatement stmt = conn.prepareStatement("UPDATE people SET first_name = ?, name = ?, address = ? WHERE id = ?");
                stmt.setString(1, firstName);
                stmt.setString(2, name);
                stmt.setString(3, address);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Person load(int id) {
        Person person = null;
        try {
            var con = DbConnectionManager.getInstance().getConnection();
            var stmt = con.prepareStatement("SELECT * FROM people WHERE id = ?");
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String name = rs.getString("name");
                String address = rs.getString("address");
                person = new Person();
                person.setId(id);
                person.setName(name);
                person.setFirstName(firstName);
                person.setAddress(address);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return person;
    }


}

