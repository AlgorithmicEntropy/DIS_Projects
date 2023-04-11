package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Sells {
    private int houseID = -1;
    private int contractID = -1;
    private int sellerID = -1;


    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public void save() {
        try {
            Connection conn = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO sells (house_id, contract_id, seller) VALUES (?, ?, ?)");
            stmt.setInt(1, houseID);
            stmt.setInt(2, contractID);
            stmt.setInt(3, sellerID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Sells> loadAll() {
        // TODO implement me
        return null;
    }
}