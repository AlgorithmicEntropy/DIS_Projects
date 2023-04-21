package de.dis.menu;

import de.dis.data.Person;

import java.util.Iterator;
import java.util.Set;

/**
 *  A small menu showing all persons from a set for selection
 */
public class PersonSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public PersonSelectionMenu(String title, Set<Person> personen) {
		super(title);
		
		Iterator<Person> it = personen.iterator();
		while(it.hasNext()) {
			Person p = it.next();
			addEntry(p.getFirstName()+" "+p.getName(), p.getId());
		}
		addEntry("Back", BACK);
	}
}
