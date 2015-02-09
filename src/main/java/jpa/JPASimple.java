package jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import domain.Address;
import domain.HierBottom;
import domain.Person;
import domain.sales.Customer;

public class JPASimple {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		System.out.println("JPASimple.main()");

		// These two steps would be done for you
		// were you running in an EE App Server.
		// Or just the EntityManager injected if you were using JavaEE or Spring
		EntityManagerFactory entityMgrFactory = JPAUtil.getEntityManagerFactory();
		EntityManager entityManager = JPAUtil.getEntityManager();
		
		try {
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();

			// Create an entity in the database.
			Person np = new Person("Tom", "Boots");
			System.out.println(np);
			entityManager.persist(np);
			transaction.commit();
			
			int id = np.getId();
			System.out.println("Created Person with Id " + id);
			
			transaction = entityManager.getTransaction();
			transaction.begin();

			Customer person = new Customer("Happy", "User");
			person.getHomeAddress().setStreetAddress("123 Main St");
			Address home = person.getHomeAddress();
			if (home != null && (home.getStreetAddress() != null || home.getCity() != null)) {
				entityManager.persist(home);
			} else {
				person.setHomeAddress(null);
			}
			Address work = person.getWorkAddress();
			if (work != null && (work.getStreetAddress() != null || work.getCity() != null)) {
				entityManager.persist(work);
			} else {
				person.setWorkAddress(null);
			}
			entityManager.persist(person);
			transaction.commit();
			System.out.println("Created Customer " + person + ", HomeAddress = " + person.getHomeAddress());
			
			Query query = entityManager.createQuery("select p from Person p order by p.lastName");

			List<Person> list = query.getResultList();
			System.out.println("There are " + list.size() + " persons:");
			for (Person p : list) {
				System.out.println(
					p.getFirstName() + ' ' + p.getLastName());
			}
			System.out.println();
			
			// Remove an entity without actually loading it.
			transaction = entityManager.getTransaction();
			transaction.begin();
			Person dp = entityManager.getReference(Person.class, 1);
			System.out.println("Removing person " + dp.getId());
			entityManager.remove(dp);
			transaction.commit();

			// Try out Table Per Class hierarchy relationship
			transaction = entityManager.getTransaction();
			transaction.begin();
			entityManager.persist(new HierBottom());
			transaction.commit();
			
		
		} finally {	
			if (entityManager != null)
				entityManager.close();
			if (entityMgrFactory != null)
				entityMgrFactory.close();
		}
	}

}