package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.test.Address;
import com.technomark.fishymapper.test.City;
import com.technomark.fishymapper.test.Person;
import org.junit.*;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by troy on 8/2/17.
 */
public class PersistMergeTest extends PersistTest {



    @Test
    public void test1() {
        Assert.assertNotEquals(null, cityModel1.getId());
        Assert.assertNotEquals(null, cityModel1.getId());
    }

    @Test
    public void test2() {

        try {
            persistantmanager.exec(personModel);

            personSelect.joinEntity(Address.class, "a","addresses")
                    .joinEntity(City.class, "c", "city");
            personSelect.setWhere("p.id = "+personModel.getId());

            Person testPerson = (Person) personSelect.getItem();

            Assert.assertEquals(testPerson, personModel);

            assertEquals(testPerson.getAddresses().size(), 3);

            Address testAddress1 = testPerson.getAddresses().get(0);
            Address testAddress2 = testPerson.getAddresses().get(1);
            Address testAddress3 = testPerson.getAddresses().get(2);

            Assert.assertEquals(testAddress1.getCity(), cityModel1);
            Assert.assertEquals(testAddress2.getCity(), cityModel1);
            Assert.assertEquals(testAddress3.getCity(), cityModel2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
