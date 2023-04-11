package de.dis.cli;

import de.dis.data.*;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public class AgentMenu extends AbstractMenu {

    final int LOGOUT = 1;
    final int CREATE_ESTATE = 2;
    final int UPDATE_ESTATE = 3;
    final int DELETE_ESTATE = 4;
    final int SHOW_ESTATES = 5;
    final int CREATE_PERSON = 6;
    final int RENT_APARTMENT = 7;
    final int SELL_HOUSE = 8;

    final int SHOW_CONTRACTS = 9;

    public void showMenu() {
        Menu menu = new Menu("Agent menu");
        while(true) {
            menu.clear();
            menu.addEntry("Back", BACK);
            if (Session.getInstance().isAgent()) {
                menu.addEntry("Logout", LOGOUT);
                menu.addEntry("Add new estate", this.CREATE_ESTATE);
                menu.addEntry("Edit estates", this.UPDATE_ESTATE);
                menu.addEntry("Delete estates", this.DELETE_ESTATE);
                menu.addEntry("Show managed estates", SHOW_ESTATES);
                menu.addEntry("Add person", this.CREATE_PERSON);
                menu.addEntry("Rent apartment", RENT_APARTMENT);
                menu.addEntry("Sell house", SELL_HOUSE);
                menu.addEntry("Show all contracts", SHOW_CONTRACTS);
            } else {
                this.login();
                continue;
            }

            int response = menu.show();

            switch (response) {
                case BACK -> {
                    return;
                }
                case LOGOUT -> {
                    Session.getInstance().logoutAgent();
                    System.out.println("Logged out");
                    return;
                }
                case CREATE_ESTATE -> this.addEstate();
                case DELETE_ESTATE -> {
                    Estate estate = this.showEstateMenu();
                    estate.delete();
                }
                case UPDATE_ESTATE -> {
                    var estate = this.showEstateMenu();
                    this.editEstate(estate);
                }
                case SHOW_ESTATES -> {
                    this.showManagedEstates();
                }
                case CREATE_PERSON -> {
                    this.addPerson();
                }
                case RENT_APARTMENT -> {
                    this.rentApartment();
                }
                case SELL_HOUSE -> {
                    this.sellHouse();
                }
                case SHOW_CONTRACTS -> {
                    this.showContracts();
                }
            }
        }
    }

    private void showContracts() {
        var rents = Rents.loadAll();
        var sells = Sells.loadAll();

        assert rents != null;
        System.out.println("-- Tenancy Contracts --");
        for (var rent : rents) {
            System.out.println(rent.toString());
        }

        assert sells != null;
        System.out.println("-- Purchase Contracts --");
        for (var sold : sells) {
            System.out.println(sold.toString());
        }
        FormUtil.waitForInput();
    }

    private void showManagedEstates() {
        var houses = House.loadAll();
        var apartments = Apartment.loadAll();

        assert houses != null;
        System.out.println("-- Houses --");
        for (var house : houses) {
            System.out.println(house.toString());
        }
        assert apartments != null;
        System.out.println("-- Apartments --");
        for (var apartment : apartments) {
            System.out.println(apartment.toString());
        }
        FormUtil.waitForInput();
    }

    private void sellHouse() {
        Contract base = createBaseContract();

        int houseID = showHouseMenu().getId();
        int personID = showPersons().getId();

        int noOfInstallments = FormUtil.readInt("Number of installments");
        double intrestRate = FormUtil.readDouble("Interest Rate");

        PurchaseContract contract = new PurchaseContract(base);
        contract.setNumberOfInstallments(noOfInstallments);
        contract.setInterestRate(intrestRate);

        Sells sells = new Sells();
        sells.setHouseID(houseID);
        sells.setSellerID(personID);
        sells.setContractID(contract.getContractNumber());

        sells.save();
    }

    private void rentApartment() {
        Contract base = createBaseContract();

        int apartmentID = showApartmentMenu().getId();
        int tenantID = showPersons().getId();

        Date startDate = FormUtil.readDate("Start Date");
        Duration duration = FormUtil.readDuration("Contract Duration");
        double additionalCosts = FormUtil.readDouble("Additional Costs");

        TenancyContract tenancyContract = new TenancyContract(base);
        tenancyContract.setStartDate(startDate);
        tenancyContract.setDuration(duration);
        tenancyContract.setAdditionalCosts(additionalCosts);

        int contract_id = tenancyContract.Save();

        Rents rents = new Rents();
        rents.setApartmentID(apartmentID);
        rents.setTenantID(tenantID);
        rents.setContractID(contract_id);

        rents.save();
    }

    private Contract createBaseContract() {
        Date date = FormUtil.readDate("Contract Date");
        String place = FormUtil.readString("Place");
        Contract contract = new Contract();
        contract.setDate(date);
        contract.setPlace(place);
        return contract;
    }

    private Person showPersons() {
        var persons = Person.getAll();
        var menu = new Menu("Select a person");
        for (int i = 0; i < persons.size(); i++) {
            var person = persons.get(i);
            menu.addEntry( person.getFirstName() + " " + person.getName(), i);
        }
        var response = menu.show();
        return persons.get(response);
    }

    private void addPerson() {
        String firstName = FormUtil.readString("First Name");
        String name = FormUtil.readString("Name");
        String address = FormUtil.readString("Address");

        var person = new Person();
        person.setFirstName(firstName);
        person.setName(name);
        person.setAddress(address);

        person.save();
    }

    private void editEstate(Estate estate) {
        String city = FormUtil.changeString("City", estate.getCity());
        int postalCode = FormUtil.changeInt("Postal Code", estate.getPostalCode());
        String street = FormUtil.changeString("Street", estate.getStreet());
        String streetNumber = FormUtil.changeString("Street Number", estate.getStreetNumber());
        double squareArea = FormUtil.changeDouble("Square Area", estate.getSquareArea());

        estate.setCity(city);
        estate.setPostalCode(postalCode);
        estate.setStreet(street);
        estate.setStreetNumber(streetNumber);
        estate.setSquareArea(squareArea);

        if (estate instanceof House house) {
            var floors = FormUtil.changeInt("Floors", house.getFloors());
            var price = FormUtil.changeDouble("Price", house.getPrice());
            var garden = FormUtil.changeBoolean("Garden", house.getGarden());

            house.setFloors(floors);
            house.setPrice(price);
            house.setGarden(garden);

            house.save();

        } else if (estate instanceof Apartment apartment) {
            int floor = FormUtil.changeInt("Floor", apartment.getFloor());
            double rent = FormUtil.changeDouble("Rent", apartment.getRent());
            int rooms = FormUtil.changeInt("Rooms", apartment.getRooms());
            boolean balcony = FormUtil.changeBoolean("Balcony", apartment.getBalcony());
            boolean builtInKitchen = FormUtil.changeBoolean("Built-in Kitchen", apartment.getBuiltInKitchen());

            apartment.setFloor(floor);
            apartment.setRent(rent);
            apartment.setRooms(rooms);
            apartment.setBalcony(balcony);
            apartment.setBuiltInKitchen(builtInKitchen);

            apartment.save();
        }
    }

    private Estate showEstateMenu() {
        var menu = new Menu("Type of estate");
        menu.addEntry("House", 0);
        menu.addEntry("Apartment", 1);

        var response = menu.show();

        switch (response) {
            case 0 -> {
                return showHouseMenu();
            }
            case 1 -> {
                return showApartmentMenu();
            }
        }

        return null;
    }

    private Estate showHouseMenu() {
        var houses = House.loadAll();
        var menu = new Menu("Select an estate");
        for (int i = 0; i < houses.size(); i++) {
            var estate = houses.get(i);
            menu.addEntry(estate.toString(), i);
        }
        var response = menu.show();
        return houses.get(response);
    }

    private Estate showApartmentMenu() {
        var apartments = Apartment.loadAll();
        var menu = new Menu("Select an apartment");
        for (int i = 0; i < apartments.size(); i++) {
            var estate = apartments.get(i);
            menu.addEntry(estate.toString(), i);
        }
        var response = menu.show();
        return apartments.get(response);
    }

    private void login() {
        var login = FormUtil.readString("Login");
        var password = FormUtil.readString("Password");

        var agent = EstateAgent.fromLogin(login);
        try {
            agent.getPassword();
        } catch (NullPointerException ex) {
            System.out.println("Invalid login");
            return;
        }
        if (!Objects.equals(password, agent.getPassword())) {
            System.out.println("Wrong password");
            return;
        }
        Session.getInstance().loginAgent(agent);
        System.out.println("Logged in as " + agent.getLogin());
    }

    private void addEstate() {
        var menu = new EstateMenu();
        menu.showMenu();
    }


}
