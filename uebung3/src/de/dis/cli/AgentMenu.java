package de.dis.cli;

import de.dis.core.EstateService;
import de.dis.data.*;

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

    final EstateService service = EstateService.getInstance();

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
                    if(estate instanceof House && !service.getAllSellsForHouse((House) estate).isEmpty()){
                        System.out.println("The house is part of a sell and therefore can't be deleted.");
                    } else if(estate instanceof Apartment && !service.getAllRentsForApartment((Apartment) estate).isEmpty()){
                        System.out.println("The apartment is part of a rent and therefore can't be deleted.");
                    } else{
                        service.delete(estate);
                    }
                }
                case UPDATE_ESTATE -> {
                    var estate = this.showEstateMenu();
                    assert estate != null;
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
        var rents = service.getAllRents();
        var sells = service.getAllSells();

        System.out.println("-- Tenancy Contracts --");
        for (var rent : rents) {
            System.out.println(rent.toString());
            System.out.println("\n");
        }

        System.out.println("-- Purchase Contracts --");
        for (var sold : sells) {
            System.out.println(sold.toString());
        }
        System.out.println("\nPress any key to continue");
        FormUtil.waitForInput();
    }

    private void showManagedEstates() {
        var houses = service.getAllHousesForEstateAgent(Session.getInstance().getAgent());
        var apartments = service.getAllApartmentsForEstateAgent(Session.getInstance().getAgent());

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

        House house = showHouseMenu();
        Person buyer = showPersons();

        int noOfInstallments = FormUtil.readInt("Number of installments");
        double intrestRate = FormUtil.readDouble("Interest Rate");

        PurchaseContract contract = new PurchaseContract(base);
        contract.setNumberOfInstallments(noOfInstallments);
        contract.setInterestRate(intrestRate);
        service.persist(contract);

        Sells sells = new Sells();
        sells.setHouse(house);
        sells.setBuyer(buyer);
        sells.setContract(contract);

        service.persist(sells);
    }

    private void rentApartment() {
        Contract base = createBaseContract();

        var apartmentID = showApartmentMenu();
        var tenantID = showPersons();

        Date startDate = FormUtil.readDate("Start Date");
        Date endDate = FormUtil.readDate("Contract End Date");
        double additionalCosts = FormUtil.readDouble("Additional Costs");

        TenancyContract tenancyContract = new TenancyContract(base);
        tenancyContract.setStartDate(startDate);
        tenancyContract.setDuration(endDate);
        tenancyContract.setAdditionalCosts(additionalCosts);

        service.persist(tenancyContract);

        Rents rents = new Rents();
        rents.setApartment(apartmentID);
        rents.setTenant(tenantID);
        rents.setContract(tenancyContract);

        service.persist(rents);
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
        var persons = service.getAllPersons();
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

        service.persist(person);
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

            service.update(house);

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

            service.update(apartment);
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

    private House showHouseMenu() {
        var houses = service.getAllHousesForEstateAgent(Session.getInstance().getAgent());
        var menu = new Menu("Select an estate");
        for (int i = 0; i < houses.size(); i++) {
            var estate = houses.get(i);
            menu.addEntry(estate.toString(), i);
        }
        var response = menu.show();
        return houses.get(response);
    }

    private Apartment showApartmentMenu() {
        var apartments = service.getAllApartmentsForEstateAgent(Session.getInstance().getAgent());
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

        var agent = service.getEstateAgentByLogin(login);
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
        System.out.println("Welcome " + agent.getName());
    }

    private void addEstate() {
        var menu = new EstateMenu();
        menu.showMenu();
    }


}
