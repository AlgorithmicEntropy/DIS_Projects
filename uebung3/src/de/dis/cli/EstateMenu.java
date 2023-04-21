package de.dis.cli;

import de.dis.core.EstateService;
import de.dis.data.Apartment;
import de.dis.data.Estate;
import de.dis.data.House;

public class EstateMenu extends AbstractMenu {
    final int HOUSE = 1;
    final int APAERTMENT = 2;

    @Override
    public void showMenu() {
        var menu = new Menu("Estate menu");

        String city = FormUtil.readString("City");
        int code = FormUtil.readInt("Postal Code");
        String street = FormUtil.readString("Street");
        String streetNumber = FormUtil.readString("Street Number");
        double squareArea = FormUtil.readDouble("Square Area");

        Estate estate = new Estate();
        estate.setCity(city);
        estate.setPostalCode(code);
        estate.setStreet(street);
        estate.setStreetNumber(streetNumber);
        estate.setSquareArea(squareArea);
        estate.setEstateAgent(Session.getInstance().getAgent());

        menu.addEntry("House", HOUSE);
        menu.addEntry("Apartment", APAERTMENT);

        while (true) {

            var response = menu.show();
            switch (response) {
                case HOUSE -> {
                    this.houseFromEstate(estate);
                    return;
                }
                case APAERTMENT -> {
                    this.apartmentFromEstate(estate);
                    return;
                }
            }
        }
    }

    private void apartmentFromEstate(Estate estate) {
        int floor = FormUtil.readInt("Floor");
        double rent = FormUtil.readDouble("Rent");
        int rooms = FormUtil.readInt("Rooms");
        boolean balcony = FormUtil.readBoolean("Balcony");
        boolean kitchen = FormUtil.readBoolean("Built-in Kitchen");

        var apartment = new Apartment(estate);
        apartment.setFloor(floor);
        apartment.setRent(rent);
        apartment.setRooms(rooms);
        apartment.setBalcony(balcony);
        apartment.setBuiltInKitchen(kitchen);

        EstateService.getInstance().persist(apartment);
    }

    private void houseFromEstate(Estate estate) {
        int floors = FormUtil.readInt("Floors");
        double price = FormUtil.readDouble("Price");
        boolean garden = FormUtil.readBoolean("Garden");

        var house = new House(estate);
        house.setFloors(floors);
        house.setPrice(price);
        house.setGarden(garden);

        EstateService.getInstance().persist(house);
    }
}
