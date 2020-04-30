package br.com.quiui;

import java.util.Set;
import java.util.HashSet;
import java.util.GregorianCalendar;

import br.com.quiui.entities.User;
import br.com.quiui.entities.Person;
import br.com.quiui.entities.Activity;
import br.com.quiui.entities.UserGroup;

import javax.persistence.EntityManager;

public class PersistenceGenerator {

	public static void pupulate(EntityManager manager) {
		manager.getTransaction().begin();
		try {
			createUserOne(manager);
			createUserTwo(manager);
			createUserThree(manager);
			createUserFour(manager);
			createUserFive(manager);

			createActivityOne(manager);
			createActivityTwo(manager);
			createActivityThree(manager);
			createActivityFour(manager);

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
		user.setGroup(UserGroup.GUEST);

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
		user.setGroup(UserGroup.GUEST);

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

	private static void createActivityOne(EntityManager manager) {
		Activity activity = new Activity();

		activity.setValue(0);
		activity.setName("ACTIVITY ONE");
		activity.setBegin(new GregorianCalendar());
		activity.setFinish(new GregorianCalendar());
		activity.setDescription("this is the activity one");

		manager.persist(activity);
	}

	private static void createActivityTwo(EntityManager manager) {
		Activity activity = new Activity();

		activity.setValue(0);
		activity.setName("ACTIVITY TWO");
		activity.setBegin(new GregorianCalendar());
		activity.setFinish(new GregorianCalendar());
		activity.setDescription("this is the activity two");

		Set<UserGroup> groups = new HashSet();
		groups.add(UserGroup.ADMIN);

		activity.setGroups(groups);

		manager.persist(activity);
	}

	private static void createActivityThree(EntityManager manager) {
		Activity activity = new Activity();

		activity.setValue(0);
		activity.setName("ACTIVITY THREE");
		activity.setBegin(new GregorianCalendar());
		activity.setFinish(new GregorianCalendar());
		activity.setDescription("this is the activity three");

		Set<UserGroup> groups = new HashSet();
		groups.add(UserGroup.GUEST);

		activity.setGroups(groups);

		manager.persist(activity);
	}

	private static void createActivityFour(EntityManager manager) {
		Activity activity = new Activity();

		activity.setValue(0);
		activity.setName("ACTIVITY FOUR");
		activity.setBegin(new GregorianCalendar());
		activity.setFinish(new GregorianCalendar());
		activity.setDescription("this is the activity four");

		Set<UserGroup> groups = new HashSet();
		groups.add(UserGroup.ADMIN);
		groups.add(UserGroup.GUEST);

		activity.setGroups(groups);

		manager.persist(activity);
	}

}
