package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Rents {
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

    public void save() {
        try {
            Connection conn = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO rents (apartment_id, contract_id, tenant_id) VALUES (?, ?, ?)");
            stmt.setInt(1, apartmentID);
            stmt.setInt(2, contractID);
            stmt.setInt(3, tenantID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Rents> loadAll() {
        // TODO implement me
        return null;
    }

    @Override
    public String toString() {
        var apartment = Apartment.load(apartmentID);
        var contract = TenancyContract.load(contractID);
        var tenant = Person.load(tenantID);
        var builder = new StringBuilder();
        builder.append(getClass().getName()).append("{");
        builder.append(apartment.toString());
        builder.append(",");
        builder.append(tenant.toString());
        builder.append(",");
        builder.append(contract.toString());
        builder.append("}");
        return builder.toString();
    }
}
