package de.dis.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Sells extends AbstractDataObject{

    private static final String ID = "id";
    private static final String HOUSE_ID = "house_id";
    private static final String CONTRACT_ID = "contract_id";
    private static final String BUYER_ID = "buyer_id";

    private int id = -1;
    private int houseID = -1;
    private int contractID = -1;
    private int buyerID = -1;


    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(int buyerID) {
        this.buyerID = buyerID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
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

    public static List<Sells> loadAll() {
        return loadAllInternal(Sells::new);
    }

    public String toString() {
        var house = House.load(houseID);
        var contract = PurchaseContract.load(contractID);
        var buyer = Person.load(buyerID);
        var builder = new StringBuilder();
        builder.append("Sold House:\n");
        builder.append(ID).append(": ").append(id);
        builder.append("\n");
        builder.append(house.toString());
        builder.append("\n");
        builder.append(buyer.toString().replace("Person", "Buyer"));
        builder.append("\n");
        builder.append(contract.toString());
        return builder.toString();
    }


    @Override
    List<String> getDBFields() {
        return List.of(HOUSE_ID, CONTRACT_ID, BUYER_ID);
    }

    @Override
    String getTableName() {
        return "sells";
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
        this.id = newId;
    }

    @Override
    void setValues(PreparedStatement stmt) throws SQLException {
        List<String> dbFields = getDBFields();
        stmt.setInt(dbFields.indexOf(HOUSE_ID) + 1, houseID);
        stmt.setInt(dbFields.indexOf(CONTRACT_ID) + 1, contractID);
        stmt.setInt(dbFields.indexOf(BUYER_ID) + 1, buyerID);
    }

    @Override
    void loadValues(ResultSet rs) throws SQLException {
        setID(rs.getInt(ID));
        setHouseID(rs.getInt(HOUSE_ID));
        setContractID(rs.getInt(CONTRACT_ID));
        setBuyerID(rs.getInt(BUYER_ID));
    }
}
