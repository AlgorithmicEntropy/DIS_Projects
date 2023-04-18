package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Contract extends AbstractDataObject {
    public static final String PLACE = "place";
    public static final String DATE = "date";
    public static final String CONTRACT_NUMBER = "contract_number";

    protected int contractNumber = -1;
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

    protected void setValues(PreparedStatement stmt) throws SQLException {
        List<String> columns = getDBFields();
        stmt.setDate(columns.indexOf(DATE) + 1, new java.sql.Date(date.getTime()));
        stmt.setString(columns.indexOf(PLACE) + 1, place);
    }

    public List<String> getDBFields() {
        return List.of(DATE, PLACE);
    }

    public String getTableName() {
        return "contracts";
    }

    @Override
    String getIdName() {
        return CONTRACT_NUMBER;
    }

    @Override
    int getIdValue() {
        return getContractNumber();
    }

    @Override
    void setIdValue(int newId) {
        setContractNumber(newId);
    }

    public void save() {
        insertOrUpdate();
    }

    public static Contract load(int contractNumber) {
        Contract contract = new Contract();
        contract.setContractNumber(contractNumber);
        return loadInternal(contractNumber, contract);
    }


    protected void loadValues(ResultSet rs) throws SQLException {
        this.setDate(rs.getDate(DATE));
        this.setPlace(rs.getString(PLACE));
    }

    @Override
    public String toString() {
        return "Contract {" +
                "contractNumber=" + contractNumber +
                ", date=" + date +
                ", place='" + place + '\'' +
                '}';
    }
}


