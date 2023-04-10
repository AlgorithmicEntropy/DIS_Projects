package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Contract {
    private int contractNumber;
    private Date date;
    private String place;

    public Contract(int contractNumber, Date date, String place) {
        this.contractNumber = contractNumber;
        this.date = date;
        this.place = place;
    }

    // getters and setters
    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void save() {
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO contracts (contract_number, date, place) VALUES (?, ?, ?)");
            stmt.setInt(1, contractNumber);
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            stmt.setString(3, place);
            stmt.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
}

