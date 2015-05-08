package br.com.quiui;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.quiui.entities.Person;
import br.com.quiui.entities.User;

public class Basic {
	
	private EntityManager manager;
	private EntityManagerFactory factory;
	
	@Before
	public void iniciar() {
		factory = Persistence.createEntityManagerFactory("quiui");
		manager = factory.createEntityManager();
		PersistenceGenerator.pupulate(manager);
	}
	
	@After
	public void terminar() {
		manager.close();
		factory.close();
	}
	
	@Test
	public void consideringPrimitivesAttributes() throws Exception {
		Person person;
		person = new Person();
		person.setName("Willian");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(true));
	}
	
	@Test
	public void ignoringPrimitivesAttributes() throws Exception {
		Person person;
		person = new Person();
		person.setName("Willian");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}
	
	@Test
	public void selecting() throws Exception {
		Person person;
		person = new Person();
		person.setName("Willian");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}
	
	@Test
	public void counting() throws Exception {
		Person person;
		person = new Person();
		person.setName("Willian");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.create(user);

		assertEquals(new Long(1), query.count());
	}
	
	@Test
	public void unique() throws Exception {
		Person person;
		person = new Person();
		person.setName("Willian");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.create(user);
		
		User response = query.unique();
		assertEquals(1L, response.getCode());
	}
	
	@Test
	public void noLike() throws Exception {
		Person person;
		person = new Person();
		person.setName("is");
		
		User user = new User();
		user.setPerson(person);

		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(true));
	}
	
	@Test
	public void likeAll() throws Exception {
		Person person;
		person = new Person();
		person.setName("is");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.enableLike();
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}
	
	@Test
	public void likeClassAttributes() throws Exception {
		Person person;
		person = new Person();
		person.setName("is");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.enableLikeFor(Person.class);
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}
	
	@Test
	public void likeIndividualAttribute() throws Exception {
		Person person;
		person = new Person();
		person.setName("is");
		
		User user = new User();
		user.setPerson(person);
		
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.enableLikeForAttribute(Person.class, "name");
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.create(user);
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}
	
	@Test
	public void ascendant() throws Exception {
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(User.class, "code");
		query.create(new User());
		query.asc("login");
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		String[] names = { "lisa", "mark", "michael", "susan", "willian" };
		
		int i = 0;
		for (User response : users) {
			assertEquals(names[i++], response.getLogin());
		}
	}
	
	@Test
	public void descendant() throws Exception {
		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(User.class, "code");
		query.create(new User());
		query.desc("login");
		query.setFirst(0);
		query.setMax(10);
		
		Collection<User> users = query.select();
		String[] names = { "willian", "susan", "michael", "mark", "lisa" };
		
		int i = 0;
		for (User response : users) {
			assertEquals(names[i++], response.getLogin());
		}
	}

}
