# fishymapper


models:

```

@TableEntity(name = "test1_city")
public class City implements IModel, Serializable {

    private static final long serialVersionUID = 1L;

    @TableColumn(name = "id", type = TableColumn.FieldType.LONG, autogen = true)
    private Long id;

    @TableColumn(name = "name", type = TableColumn.FieldType.VARCHAR)
    private String name;

    public City() {

    }

    public City(String name) {
        this.name = name;
    }

    @Override
    public IModel clone() {
        City model = new City();
        model.name = this.name;
        return model;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        //if (((long)id) != ((long)city.id)) return false;
        if (id != null ? !id.equals(city.id) : city.id != null) return false;
        return name != null ? name.equals(city.name) : city.name == null;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result.intValue();
    }
}


@TableEntity(name = "test1_address")
public class Address implements IModel, Serializable {

    private static final long serialVersionUID = 1L;

    @TableColumn(name = "id", type = TableColumn.FieldType.LONG, autogen = true)
    private Long id;

    @TableColumn(name = "street_name", type = TableColumn.FieldType.VARCHAR)
    private String street;

    @TableJoin(joinType = TableJoin.JoinType.INNER)
    @TableColumn(name = "city_id", type = TableColumn.FieldType.LONG)
    private City city;

    @TableJoin(mappedBy = "addresses", joinType = TableJoin.JoinType.INNER)
    @TableColumn(name = "person_id", type = TableColumn.FieldType.LONG)
    private Person person;

    public Address() {
        super();
    }

    public Address(String street, City city) {
        this();
        this.city = city;
        this.street = street;
    }

    public Address(String street, City city, Person person) {
        this();
        this.city = city;
        this.street = street;
        this.person = person;
    }

    public IModel clone() {
        Address model = new Address();
        model.city = this.city;
        model.street = this.street;
        model.person = this.person;
        return model;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                "street='" + street + '\'' +
                "person='" + person + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        //if (((long)id) != ((long)address.id)) return false;
        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        //if (person != null ? !person.equals(address.person) : address.person != null) return false;
        return city != null ? city.equals(address.city) : address.city == null;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result.intValue();
    }
}


@TableEntity(name = "test1_person")
public class Person implements IModel, Serializable{

    private static final long serialVersionUID = 1L;

    @TableColumn(name = "id", type = TableColumn.FieldType.LONG, autogen = true)
    private Long id;
    @TableColumn(name = "First_Name", type = TableColumn.FieldType.VARCHAR)
    private String firstName;
    @TableColumn(name = "Last_Name", type = TableColumn.FieldType.VARCHAR)
    private String lastName;

    @TableJoin(mappedBy = "person", joinType = TableJoin.JoinType.INNER, cascade = true)
    private List<Address> addresses;

    public Person() {
        super();
        addresses = new ArrayList<Address>();
    }

    public Person(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public IModel clone() {
        Person model = new Person();
        model.firstName = this.firstName;
        model.lastName = this.lastName;
        model.addresses = this.addresses;
        return model;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public boolean isAddress(Address model) {
        for (Address a: this.addresses) {
            if (a.equals(model))
                return true;
        }
        return false;
    }

    public void setAddresses(Address model) {
        Address addressModel = (Address) model;
        if (isAddress(addressModel))
            return;
        this.addresses.add(addressModel);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                //", addresses=" + addresses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        //if (((long)id) != ((long)person.id)) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        return lastName != null ? lastName.equals(person.lastName) : person.lastName == null;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result.intValue();
    }
}

```

dao:

```
public interface IPersonDAO extends IGenericDAO<Person> {
    public List<Person> selectJoin() throws SQLException;
    public int countOfPersons(Person person) throws SQLException;
}

public interface ICityDAO extends IGenericDAO<City> {

}

public class CityDAO extends GenericDAO<City> implements ICityDAO {

    public CityDAO(DataSource dataSource) {
        super(City.class, dataSource);
    }
}


public class PersonDAO extends GenericDAO<Person> implements IPersonDAO {

    public PersonDAO(DataSource dataSource) {
        super(Person.class, dataSource);
    }

    @Override
    public List<Person> selectJoin() throws SQLException {
        SelectBuilder builder = new SelectBuilder(namedParameterJdbcTemplate, Person.class, "p");
        builder.joinEntity(Address.class, "a","addresses")
            .joinEntity(City.class, "c", "city");

        String cityName = "Toronto";
        builder.setWhere("c.name = '"+cityName+"'");

        System.out.println("Test Parser "+ builder.getSelectQuery());
        return  (List<Person>)builder.getList();
    }

    @Override
    public int countOfPersons(Person person) throws SQLException {
        SelectBuilder builder = new SelectBuilder(namedParameterJdbcTemplate, Person.class, "p");
        builder.setWhere("First_Name = :firstName and Last_Name = :lastName");
        builder.setString("firstName", person.getFirstName());
        builder.setString("lastName", person.getLastName());
        return builder.getCount();
    }
}

```

init:

```

DataSource dataSource = DataSourceFactory.getInstance().getDataSource();

TransactionTemplate transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource));

```

create models:

```
ICityDAO cityDao = new CityDAO(dataSource);

City c1 = cityDao.insert(new City("Toronto"));
City c2 = cityDao.insert(new City("Ottawa"));

final Person p1 = transactionTemplate.execute(new TransactionCallback<Person>() {

    public Person doInTransaction(TransactionStatus transactionStatus) {
        try {

            //create address
            Address a1 = new Address("Main St.", c1);
            Address a2 = new Address("Bay St.", c1);
            Address a3 = new Address("Dundas St.", c2);

            Person p1 = new Person("Java 1", "Honk");
            p1.setAddresses(a1);
            p1.setAddresses(a2);
            p1.setAddresses(a3);

            PersistantMerge pm = new PersistantMerge(dataSource);
            pm.exec(p1);

            return p1;

        } catch (Exception e) {
            System.out.println(e);
            transactionStatus.setRollbackOnly();
        }
        return null;
    }
});

```

read models:

```
System.out.println(personDao.countOfPersons(p1));
List<Person> personList = personDao.selectJoin();
for (Person p: personList) {
    System.out.println(p);
}

```

delete model:

```
transactionTemplate.execute(new TransactionCallbackWithoutResult() {
    @Override
    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
        try {

            DeleteCascade deleteCascade = new DeleteCascade(dataSource);
            deleteCascade.exec(p1);

            cityDao.delete(c1.getId());
            cityDao.delete(c2.getId());

        } catch (Exception e) {
            System.out.println(e);
            transactionStatus.setRollbackOnly();
        }
    }
});

```


Build jar:

```
mvn clean compile assembly:single
java -jar target/fishymapper-1-jar-with-dependencies.jar
```