package de.dis.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Sells extends AbstractDataObject{

    // TODO an SW: Tabelle benötigt eine ID-Spalte!


    private static final String ID = "id";
    private static final String HOUSE_ID = "house_id";
    private static final String CONTRACT_ID = "contract_id";
    private static final String SELLER_ID = "seller";

    private int id = -1;
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
       insert();
    }

    public static List<Sells> loadAll() {
        return loadAllInternal(Sells::new);
    }

    public String toString() {
        var house = House.load(houseID);
        var contract = PurchaseContract.load(contractID);
        // TODO an SW: Ich glaube das müsste der Buyer und nicht der Seller sein. Dann müssten wir aber unser gesamtes Modell noch mal umkrempeln :(
        var seller = Person.load(sellerID);
        var builder = new StringBuilder();
        builder.append(getClass().getName()).append("{");
        builder.append(house.toString());
        builder.append(",");
        builder.append(seller.toString());
        builder.append(",");
        builder.append(contract.toString());
        builder.append("}");
        return builder.toString();
    }


    @Override
    List<String> getDBFields() {
        return List.of(HOUSE_ID, CONTRACT_ID, SELLER_ID);
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
        stmt.setInt(dbFields.indexOf(SELLER_ID) + 1, sellerID);
    }

    @Override
    void loadValues(ResultSet rs) throws SQLException {
        setHouseID(rs.getInt(HOUSE_ID));
        setContractID(rs.getInt(CONTRACT_ID));
        setSellerID(rs.getInt(SELLER_ID));
    }
}
