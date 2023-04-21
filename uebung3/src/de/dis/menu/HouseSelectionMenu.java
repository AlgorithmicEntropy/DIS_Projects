package de.dis.menu;

import de.dis.data.House;

import java.util.Iterator;
import java.util.Set;

/**
 *  A small menu showing all houses from a set for selection
 */
public class HouseSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public HouseSelectionMenu(String title, Set<House> haeuser) {
		super(title);
		
		Iterator<House> it = haeuser.iterator();
		while(it.hasNext()) {
			House h = it.next();
			addEntry(h.getStreet()+" "+h.getStreetNumber()+", "+h.getPostalCode()+" "+h.getCity(), h.getId());
		}
		addEntry("Back", BACK);
	}
}
