package de.dis.core;

import de.dis.data.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 *  Class for managing all database entities.
 */
public class EstateService {
	
	//Hibernate Session
	private SessionFactory sessionFactory;

	private static EstateService instance;
	
	private EstateService() {
		sessionFactory = new Configuration().configure("hibernate.postgresql.cfg.xml").buildSessionFactory();
	}

	public static EstateService getInstance() {
		if(instance == null) {
			instance = new EstateService();
		}
		return instance;
	}

	public void persist(Entity o) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.persist(o);
		session.getTransaction().commit();
		session.close();
	}

	public void update(Entity o) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.merge(o);
		session.getTransaction().commit();
		session.close();
	}

	public void delete(Entity o) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.remove(o);
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * Find an estate agent with the given id
	 * @param id The ID of the agent
	 * @return Agent with ID or null
	 */
	public EstateAgent getEstateAgentByID(int id) {

		Session session = sessionFactory.openSession();
		var agent = session.get(EstateAgent.class, id);
		session.close();
		return agent;
	}
	
	/**
	 * Find estate agent with the given login.
	 * @param login The login of the estate agent
	 * @return Estate agent with the given ID or null
	 */
	public EstateAgent getEstateAgentByLogin(String login) {
		Session session = sessionFactory.openSession();
		var agent = (EstateAgent) session.createQuery("from EstateAgent where login = :login").setParameter("login", login).uniqueResult();
		session.close();
		return agent;
	}
	
	/**
	 * Returns all estateAgents
	 */
	public List<EstateAgent> getAllEstateAgents() {
		Session session = sessionFactory.openSession();
		var agents = session.createQuery("from EstateAgent").list();
		session.close();
		return agents;
	}
	
	/**
	 * Find a person with the given id
	 * @param id The ID of the person
	 * @return Person with ID or null
	 */
	public Person getPersonById(int id) {
		Session session = sessionFactory.openSession();
		var person = session.get(Person.class, id);
		session.close();
		return person;
	}
	
	/**
	 * Returns all persons
	 */
	public List<Person> getAllPersons() {
		Session session = sessionFactory.openSession();
		var persons = session.createQuery("from Person").list();
		session.close();
		return persons;
	}
	
	/**
	 * Returns all houses of an estate agent
	 * @param ea the estate agent
	 * @return All houses managed by the estate agent
	 */
	public List<House> getAllHousesForEstateAgent(EstateAgent ea) {
		Session session = sessionFactory.openSession();
		var houses = session.createQuery("from House where estateAgent.id = :id").setParameter("id", ea.getId()).list();
		session.close();
		return houses;
	}

	public List<Sells> getAllSellsForHouse(House house){
		Session session = sessionFactory.openSession();
		var sells = session.createQuery("from Sells where house.id = :id").setParameter("id", house.getId()).list();
		session.close();
		return sells;
	}

	public List<Sells> getAllRentsForApartment(Apartment apartment){
		Session session = sessionFactory.openSession();
		var rents = session.createQuery("from Rents where apartment.id = :id").setParameter("id", apartment.getId()).list();
		session.close();
		return rents;
	}
	
	/**
	 * Find a house with a given ID
	 * @param  id the house id
	 * @return The house or null if not found
	 */
	public House getHouseById(int id) {
		Session session = sessionFactory.openSession();
		var house = session.get(House.class, id);
		session.close();
		return house;
	}
	
	/**
	 * Returns all apartments of an estate agent
	 * @param ea The estate agent
	 * @return All apartments managed by the estate agent
	 */
	public List<Apartment> getAllApartmentsForEstateAgent(EstateAgent ea) {
		Session session = sessionFactory.openSession();
		var apartments = session.createQuery("from Apartment where estateAgent.id = :id").setParameter("id", ea.getId()).list();
		session.close();
		return apartments;
	}
	
	/**
	 * Find an apartment with given ID
	 * @param id The ID
	 * @return The apartment or zero, if not found
	 */
	public Apartment getApartmentByID(int id) {
		Session session = sessionFactory.openSession();
		var apartment = session.get(Apartment.class, id);
		session.close();
		return apartment;
	}
	
	/**
	 * Finds a tenancy contract with a given ID
	 * @param id Die ID
	 * @return The tenancy contract or zero if not found
	 */
	public TenancyContract getTenancyContractByID(int id) {
		Session session = sessionFactory.openSession();
		var tenancyContract = session.get(TenancyContract.class, id);
		session.close();
		return tenancyContract;
	}
	
	/**
	 * Finds a purchase contract with a given ID
	 * @param id The id of the purchase contract
	 * @return The purchase contract or null if not found
	 */
	public PurchaseContract getPurchaseContractById(int id) {
		Session session = sessionFactory.openSession();
		var purchaseContract = session.get(PurchaseContract.class, id);
		session.close();
		return purchaseContract;
	}

	public List<Sells> getAllSells() {
		Session session = sessionFactory.openSession();
		var sells = session.createQuery("from Sells").list();
		session.close();
		return sells;
	}

	public List<Rents> getAllRents() {
		Session session = sessionFactory.openSession();
		var rents = session.createQuery("from Rents").list();
		session.close();
		return rents;
	}
}
