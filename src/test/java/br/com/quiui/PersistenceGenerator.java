package br.com.quiui;

import javax.persistence.EntityManager;

import br.com.quiui.entities.Person;
import br.com.quiui.entities.User;
import br.com.quiui.entities.UserGroup;

public class PersistenceGenerator {
	
	public static void pupulate(EntityManager manager) {
		manager.getTransaction().begin();
		try {
			createUserOne(manager);
			createUserTwo(manager);
			createUserThree(manager);
			createUserFour(manager);
			createUserFive(manager);
			manager.getTransaction().commit();
		} catch (Exception exception) {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
			
			throw exception;
		}
	}
	
	private static void createUserOne(EntityManager manager) {
		Person person;
		person = new Person();
		person.setName("Willian");
		person.setEmail("willian@email.com");
		
		User user;
		user = new User();
		user.setPass("pass");
		user.setPerson(person);
		user.setLogin("willian");
		user.setGroup(UserGroup.ADMIN);
		
		manager.persist(user);
	}
	
	private static void createUserTwo(EntityManager manager) {
		Person person;
		person = new Person();
		person.setName("Mark");
		person.setEmail("mark@email.com");
		
		User user;
		user = new User();
		user.setPass("pass");
		user.setPerson(person);
		user.setLogin("mark");
		user.setGroup(UserGroup.ADMIN);
		
		manager.persist(user);
	}
	
	private static void createUserThree(EntityManager manager) {
		Person person;
		person = new Person();
		person.setName("Michael");
		person.setEmail("michael@email.com");
		
		User user;
		user = new User();
		user.setPass("pass");
		user.setPerson(person);
		user.setLogin("michael");
		user.setGroup(UserGroup.ADMIN);
		
		manager.persist(user);
	}
	
	private static void createUserFour(EntityManager manager) {
		Person person;
		person = new Person();
		person.setName("Lisa");
		person.setEmail("lisa@email.com");
		
		User user;
		user = new User();
		user.setPass("pass");
		user.setPerson(person);
		user.setLogin("lisa");
		user.setGroup(UserGroup.ADMIN);
		
		manager.persist(user);
	}
	
	private static void createUserFive(EntityManager manager) {
		Person person;
		person = new Person();
		person.setName("Susan");
		person.setEmail("susan@email.com");
		
		User user;
		user = new User();
		user.setPass("pass");
		user.setPerson(person);
		user.setLogin("susan");
		user.setGroup(UserGroup.ADMIN);
		
		manager.persist(user);
	}

}
