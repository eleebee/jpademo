package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import domain.Person;

/** Shows use of TypedQuery to avoid Query warnings.
 * @author Ian Darwin
 */
public class TypedQueryDemo {
	
	public static void main(String[] args) {

		System.out.println("TypedQueryDemo.main()");

		// These two steps would be done for you
		// were you running in an EE App Server; you'd
		// just have the EntityManager injected if using JavaEE or Spring.
		EntityManagerFactory entityMgrFactory = JpaUtil.getEntityManagerFactory();
		EntityManager entityManager = JpaUtil.getEntityManager();

		TypedQuery<Person> query = 
				entityManager.createQuery("select p from Person p order by p.lastName",
						Person.class);
		for (Person p : query.getResultList()) {
			System.out.println(
					p.getFirstName() + ' ' + p.getLastName());
		}
		System.out.println();		

		if (entityManager != null)
			entityManager.close();
		if (entityMgrFactory != null)
			entityMgrFactory.close();
	}

}
