package de.dis.menu;

import de.dis.data.Apartment;

import java.util.Iterator;
import java.util.Set;

/**
 * A small menu showing all apartments from a set for selection
 */
public class AppartmentSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public AppartmentSelectionMenu(String title, Set<Apartment> apartmenten) {
		super(title);
		
		Iterator<Apartment> it = apartmenten.iterator();
		while(it.hasNext()) {
			Apartment w = it.next();
			addEntry(w.getStreet()+" "+w.getStreetNumber()+", "+w.getPostalCode()+" "+w.getCity(), w.getId());
		}
		addEntry("Back", BACK);
	}
}
