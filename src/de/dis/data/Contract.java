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
            PreparedStatement stmt = con.prepareStatement("INSERT INTO "+getTableName() + " (" + getDBFields() + ") VALUES ("
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

    public String getTableName() {
        return "contracts";
    }

    public static Contract load(int contractNumber) {
        Contract contract = new Contract();
        return loadInternal(contractNumber, contract);
    }

    /**
     * Loads the contract data for the contract number. If there is a result for the contract number, the contract object is filled with the found data.
     * Otherwise, null is returned.
     * @param <C> The type of the contract to be handled
     * @param contractNumber Number of the contract to be loaded
     * @param contract contract object to be filled with data if there is a result
     * @return contract data / null if there is no result for the contract number
     */
    protected static <C extends Contract> C  loadInternal (int contractNumber, C contract    ){
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectFields = String.join(",", contract.getDBFields());
            PreparedStatement stmt = con.prepareStatement("SELECT "+ selectFields + " FROM "+contract.getTableName() + " WHERE contract_number = ?");
            stmt.setInt(1, contractNumber);
            ResultSet rs = stmt.executeQuery();
            C result = null;
            if (rs.next()) {
                contract.loadValues(rs);
                result = contract;
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }


    protected void loadValues(ResultSet rs) throws SQLException {
        this.setContractNumber(rs.getInt(CONTRACT_NUMBER));
        this.setDate(rs.getDate(DATE));
        this.setPlace(rs.getString(PLACE));
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


