/*
 * Copyright 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.quiui;

import br.com.quiui.entities.Activity;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.util.Collection;

import br.com.quiui.entities.User;
import br.com.quiui.entities.Person;
import br.com.quiui.entities.UserGroup;
import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.CoreMatchers.is;

import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
		Person person = new Person();
		person.setName("Willian");

		User user = new User();
		user.setPerson(person);

		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.create(user).setFirst(0).setMax(10);

		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(true));
	}

	@Test
	public void ignoringPrimitivesAttributes() throws Exception {
		Person person = new Person();
		person.setName("Willian");

		User user = new User();
		user.setPerson(person);

		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignorePrimitives();
		query.create(user);
		query.setFirst(0);
		query.setMax(10);

		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}

	@Test
	public void selecting() throws Exception {
		Person person = new Person();
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
		Person person = new Person();
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
		Person person = new Person();
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
		Person person = new Person();
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
		Person person = new Person();
		person.setName("is");

		User user = new User();
		user.setPerson(person);

		QuiuiBuilder<User> query = new QuiuiBuilder<User>(manager, User.class);
		query.ignoreAttribute(Person.class, "code");
		query.ignoreAttribute(User.class, "code");
		query.enableLike().create(user);
		query.setFirst(0).setMax(10);

		Collection<User> users = query.select();
		assertThat(users.isEmpty(), is(false));
	}

	@Test
	public void likeClassAttributes() throws Exception {
		Person person = new Person();
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
		Person person = new Person();
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

	@Test
	public void oneToMany() throws Exception {
		Person person = new Person();
		person.addUser(new User(UserGroup.ADMIN));

		QuiuiBuilder<Person> query = new QuiuiBuilder<Person>(manager, Person.class);
		query.ignorePrimitives();
		query.ignoreAttribute(User.class, "code");
		query.create(person);
		query.asc("name");

		Collection<Person> people = query.select();

		assertEquals(3, people.size());

		String[] names = { "Michael", "Susan", "Willian" };

		int i = 0;
		for (Person response : people) {
			assertEquals(names[i++], response.getName());
		}
	}

	@Test
	public void memberOrEmpty() throws Exception {
		QuiuiBuilder<Activity> query = new QuiuiBuilder<Activity>(manager, Activity.class);
		query.ignorePrimitives();

		Collection<UserGroup> groups = new ArrayList();
		groups.add(UserGroup.GUEST);

		query.isMemberOrEmpty("groups", groups);

		assertEquals(3, query.select().size());
	}

	@Test
	public void member() throws Exception {
		QuiuiBuilder<Activity> query = new QuiuiBuilder<Activity>(manager, Activity.class);
		query.ignorePrimitives();

		Collection<UserGroup> groups = new ArrayList();
		groups.add(UserGroup.ADMIN);

		query.isMember("groups", groups);

		assertEquals(2, query.select().size());
	}

}
