package de.dis.data;

public class Sells extends Entity {

    private int id = -1;
    private House house;
    private PurchaseContract contract;
    private Person buyer;


    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Person getBuyer() {
        return buyer;
    }

    public void setBuyer(Person buyer) {
        this.buyer = buyer;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(PurchaseContract contract) {
        this.contract = contract;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        var builder = new StringBuilder();
        builder.append("Sold House:\n");
        builder.append("id: ").append(id);
        builder.append("\n");
        builder.append(house.toString());
        builder.append("\n");
        builder.append(buyer.toString().replace("Person", "Buyer"));
        builder.append("\n");
        builder.append(contract.toString());
        return builder.toString();
    }
}
