package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.test.Address;
import com.technomark.fishymapper.test.Person;
import com.technomark.fishymapper.test.City;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by troy on 8/1/17.
 */
public class SelectBuilderTest extends ParserTest {

    @Test
    public void test1() {
        assertNotEquals(null, cityModel1.getId());
        assertNotEquals(null, cityModel1.getId());

        assertNotEquals(null, addressModel1.getId());
        assertNotEquals(null, addressModel2.getId());
        assertNotEquals(null, addressModel3.getId());

        assertNotEquals(null, personModel1.getId());
        assertNotEquals(null, personModel2.getId());
        assertNotEquals(null, personModel3.getId());
    }

    @Test
    public void test2() {
        try {
            personSelect.joinEntity(Address.class, "a","addresses")
                    .joinEntity(City.class, "c", "city");
            personSelect.setWhere("p.id = "+personModel1.getId());
            Person testPerson2 = (Person) personSelect.getItem();

            assertEquals(testPerson2, personModel1);
            assertEquals(testPerson2.getAddresses().size(), 2);

            Address testAddress1 = testPerson2.getAddresses().get(0);
            Address testAddress2 = testPerson2.getAddresses().get(1);

            assertEquals(testAddress1, addressModel1);
            assertEquals(testAddress2, addressModel2);

            assertEquals(testAddress1.getCity(), addressModel1.getCity());
            assertEquals(testAddress2.getCity(), addressModel2.getCity());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            addressSelect.joinEntity(Person.class, "p", "person");
            addressSelect.joinEntity(City.class, "c", "city");
            addressSelect.setWhere("a.id = "+addressModel3.getId());
            Address testAddress3 = (Address) addressSelect.getItem();

            assertEquals(testAddress3, addressModel3);
            assertEquals(testAddress3.getPerson(), personModel2);
            assertEquals(testAddress3.getCity(), cityModel2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        try {
            personSelect.joinEntity(Address.class, "a","addresses")
                    .joinEntity(City.class, "c", "city");

            String cityName = "Ottawa";
            personSelect.setWhere("c.name = '"+cityName+"'");
            List<Person> personList =  (List<Person>)personSelect.getList();

            assertEquals(personList.size(), 2);

            Person testPerson1 = personList.get(0);
            Person testPerson2 = personList.get(1);

            assertEquals(testPerson1, personModel1);
            assertEquals(testPerson2, personModel2);

            assertEquals(testPerson1.getAddresses().size(), 1);
            assertEquals(testPerson2.getAddresses().size(), 1);

            Address testAddress2 = testPerson1.getAddresses().get(0);
            Address testAddress3 = testPerson2.getAddresses().get(0);

            assertEquals(testAddress2, addressModel2);
            assertEquals(testAddress3, addressModel3);

            assertEquals(testAddress2.getCity(), cityModel2);
            assertEquals(testAddress3.getCity(), cityModel2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        try {
            addressSelect.joinEntity(Person.class, "p", "person");
            addressSelect.joinEntity(City.class, "c", "city");
            addressSelect.setWhere("p.id = "+personModel1.getId());

            List<Address> addressList = (List<Address>) addressSelect.getList();

            assertEquals(addressList.size(), 2);

            Address testAddress1 = addressList.get(0);
            Address testAddress2 = addressList.get(1);

            assertEquals(testAddress1, addressModel1);
            assertEquals(testAddress2, addressModel2);

            assertEquals(testAddress1.getCity(), cityModel1);
            assertEquals(testAddress2.getCity(), cityModel2);

            assertEquals(testAddress1.getPerson(), personModel1);
            assertEquals(testAddress2.getPerson(), personModel1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        try {
            personSelect.joinEntity(Address.class, "a","addresses")
                    .joinEntity(City.class, "c", "city");

            String cityName = "Ottawa";
            personSelect.setWhere("c.name = '"+cityName+"'");
            int personCount =  personSelect.getCount();

            assertEquals(personCount, 2);

            addressSelect.setWhere("person_id = "+personModel1.getId());
            int addressCount =  addressSelect.getCount();

            assertEquals(addressCount, 2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
