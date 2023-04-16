package de.dis.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Rents extends AbstractDataObject{

    private static final String ID= "id";
    private static final String APARTMENT_ID = "apartment_id";
    private static final String CONTRACT_ID= "contract_id";
    private static final String TENANT_ID= "tenant_id";

    private int id = -1;
    private int apartmentID = -1;
    private int contractID = -1;
    private int tenantID = -1;

    public int getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(int apartmentID) {
        this.apartmentID = apartmentID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getTenantID() {
        return tenantID;
    }

    public void setTenantID(int tenantID) {
        this.tenantID = tenantID;
    }

    public int getId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void save() {
       insert();
    }

    public static List<Rents> loadAll() {
        return loadAllInternal(Rents::new);
    }

    @Override
    public String toString() {
        var apartment = Apartment.load(apartmentID);
        var contract = TenancyContract.load(contractID);
        var tenant = Person.load(tenantID);
        var builder = new StringBuilder();
        builder.append("Tenancy Contract:\n");
        builder.append(ID).append(": ").append(id);
        builder.append("\n");
        builder.append(apartment.toString());
        builder.append("\n");
        builder.append(tenant.toString().replace("Person", "Tenant"));
        builder.append("\n");
        builder.append(contract.toString());
        return builder.toString();
    }

    @Override
    List<String> getDBFields() {
        return List.of(APARTMENT_ID, CONTRACT_ID, TENANT_ID);
    }

    @Override
    String getTableName() {
        return "rents";
    }

    @Override
    String getIdName() {
        return ID;
    }

    @Override
    int getIdValue() {
        return id;
    }

    @Override
    void setIdValue(int newId) {
        setID(newId);
    }

    @Override
    void setValues(PreparedStatement stmt) throws SQLException {
        List<String> dbFields = getDBFields();
        stmt.setInt(dbFields.indexOf(APARTMENT_ID) + 1, getApartmentID());
        stmt.setInt(dbFields.indexOf(CONTRACT_ID) + 1, getContractID());
        stmt.setInt(dbFields.indexOf(TENANT_ID) + 1, getTenantID());
    }

    @Override
    void loadValues(ResultSet rs) throws SQLException {
        setID(rs.getInt(ID));
        setApartmentID(rs.getInt(APARTMENT_ID));
        setContractID(rs.getInt(CONTRACT_ID));
        setTenantID(rs.getInt(TENANT_ID));
    }
}
