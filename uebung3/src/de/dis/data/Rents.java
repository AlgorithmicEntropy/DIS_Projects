package de.dis.data;

public class Rents extends Entity {
    private int id = -1;
    private Apartment apartment;
    private TenancyContract contract;
    private Person tenant;

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartmentID) {
        this.apartment = apartmentID;
    }

    public TenancyContract getContract() {
        return contract;
    }

    public void setContract(TenancyContract contract) {
        this.contract = contract;
    }

    public Person getTenant() {
        return tenant;
    }

    public void setTenant(Person tenant) {
        this.tenant = tenant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        /*
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
        */
        return "";
    }

}
