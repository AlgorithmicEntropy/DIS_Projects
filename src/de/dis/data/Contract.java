package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Contract {
    public static final String PLACE = "place";
    public static final String DATE = "date";
    public static final String CONTRACT_NUMBER = "contract_number";
    protected int contractNumber;
    protected Date date;
    protected String place;

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
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO contracts (" + getDBFields() + ") VALUES ("
                    + getDBFields().stream().map(s -> "?").collect(Collectors.joining(", ")) + ") RETURNING contract_number");
            setValues(stmt);
            stmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void setValues(PreparedStatement stmt) throws SQLException {
        List<String> columns = getDBFields();
        stmt.setInt(columns.indexOf(CONTRACT_NUMBER) + 1, contractNumber);
        stmt.setDate(columns.indexOf(DATE) + 1, new java.sql.Date(date.getTime()));
        stmt.setString(columns.indexOf(PLACE) + 1, place);
    }

    public List<String> getDBFields() {
        return List.of(CONTRACT_NUMBER, DATE, PLACE);
    }

    public static Contract load(int contractNumber) {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            Contract contract = null;
            String selectFields = String.join(",", new Contract().getDBFields());
            PreparedStatement stmt = con.prepareStatement("SELECT "+ selectFields + " FROM estates WHERE contract_number = ?");
            stmt.setInt(1, contractNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                contract = new Contract();
                contract.setContractNumber(rs.getInt(CONTRACT_NUMBER));
                contract.setDate(rs.getDate(DATE));
                contract.setPlace(rs.getString(PLACE));
            }
            rs.close();
            stmt.close();
            return contract;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractNumber=" + contractNumber +
                ", date=" + date +
                ", place='" + place + '\'' +
                '}';
    }
}


