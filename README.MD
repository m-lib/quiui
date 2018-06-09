# Quiui
Quiui is a dynamic query builder that you can use to generate basics JPA queries from the filled attributes of an entity.

Quiui was initially developed as a internal piece of a small program, in order to enable a really basic kind of query by example with pure JPA Criteria that could consider simple associations.

## Examples
### Selecting
Let's assume that we need to select all users of a person named "Bill". All we have to do is fill the entities with the desired informations and ask Quiui to prepare it for us:

```java
User user = new User();
Person person = new Person();

person.setName("Bill");
user.setPerson(person);

QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.create(user);

query.setFirst(0);
query.setMax(10);

query.select();
```

Which will generate something like this:

```sql
SELECT * FROM 
	user 
INNER JOIN 
	person ON user.person_idPerson = person.idPerson 
WHERE 
	person.name = "Bill" 
LIMIT 10;
```

### Counting:
Counting results is equally simple:

```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.create(user);
query.count();
```

### Unique:
The same principles apply for unique results:

```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.create(user);
query.unique();
```

### Ordering
Ordering a selection is quite simple:

```java
QuiuiBuilder<Person> query = new QuiuiBuilder<Person>(entityManager, Person.class);

query.desc("age");
query.asc("name");

query.select();
```

You can also pass several ordination parameters at once:

```java
QuiuiBuilder<Person> query = new QuiuiBuilder<Person>(entityManager, Person.class);

query.asc("name", "age", "email");

query.select();
```

### Ignoring Attributes:
Sometimes it may be convenient to ignore certain attributes when generating a query, like for example, the primitives ones:

```java
public class User {
	private long id;
	private Group group;
	// other attributes
}
```

In this case, primitive attributes can become a problem for Quiui, because when a `User` is instantiated, all primitive attributes will receive default values, which will be considered by Quiui when creating the query.

To avoid this situations, you can ask Quiui to ignore such attributes before creating the query:

```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.ignore(User.class, "id");
query.create(user);

query.setFirst(0);
query.setMax(10);

query.select();
```

### Adding Some Restrictions:
Let's take a look at the example that accomplish the selection of all users of a person named "Bill" again, but this time, we'll assume that we also need to exclude from this selection any user from the administrators group.

This could be done as follows:

```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.notEqual("group", Group.ADMIN);
query.create(user);
query.select();
```

### Like:
Like searches can be done in three distinct ways:

#### Like All
```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.enableLike();
query.create(user);

query.setFirst(0);
query.setMax(10);

query.select();
```
This will treat all String attributes from all entities with like.

#### Like Class Attributes
```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.enableLikeFor(Person.class);
query.create(user);

query.setFirst(0);
query.setMax(10);

query.select();
```
This will treat all String attributes from `user.Person` with like.

#### Like Individual Attributes
```java
QuiuiBuilder<User> query = new QuiuiBuilder<User>(entityManager, User.class);
query.enableLikeForAttribute(Person.class, "name");
query.create(user);

query.setFirst(0);
query.setMax(10);

query.select();
```
This will treat the `name` attribute (if it's a String) from `user.Person` with like.

## Limitations
Quiui is really basic and simple so keep in mind that there aren't many others functionalities beyond the ones presented here.

### Association Loop
You must beware of association's loops when filling your entities chain:
```java
Person person = new Person("Bill");
Pet pet = new Pet("Fido");

// one to many
person.add(pet);

// bidirection relationship
pet.setOwner(person);
```
The code above will throw Quiui into a endless loop.

## Licence
MIT License
