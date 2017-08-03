package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.test.Address;
import com.technomark.fishymapper.test.City;
import com.technomark.fishymapper.test.Person;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by troy on 8/2/17.
 */
public class DeleteCascadeTest extends PersistTest {

    @Test
    public void test1() {
        Assert.assertNotEquals(null, cityModel1.getId());
        Assert.assertNotEquals(null, cityModel2.getId());
    }

    @Test
    public void test2() {

        try {
            //create persistent model
            persistantmanager.exec(personModel);

            //select persistent model
            personSelect.joinEntity(Address.class, "a","addresses")
                    .joinEntity(City.class, "c", "city");
            personSelect.setWhere("p.id = "+personModel.getId());

            Person testPerson1 = (Person) personSelect.getItem();
            Assert.assertEquals(testPerson1, personModel);

            //select address model
            addressSelect.setWhere("person_id = "+personModel.getId());
            List<Address> addressList1 = (List<Address>) addressSelect.getList();
            assertEquals(addressList1.size(), 3);

            //delete persistent model
            DeleteCascade deleteCascade = new DeleteCascade(dataSource);
            deleteCascade.exec(personModel);

            //select persistent model
            personSelect.setWhere("p.id = "+personModel.getId());

            Person testPerson2 = (Person) personSelect.getItem();
            assertEquals(testPerson2, null);

            //select address model
            addressSelect.setWhere("person_id = "+personModel.getId());
            List<Address> addressList2 = (List<Address>) addressSelect.getList();
            assertEquals(addressList2.size(), 0);

            //select cities model
            List<City> cityList = (List<City>) citySelect.getList();
            assertEquals(cityList.size(), 2);

            City testCity1 = cityList.get(0);
            City testCity2 = cityList.get(1);

            Assert.assertEquals(testCity1, cityModel1);
            Assert.assertEquals(testCity2, cityModel2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    @Override
    public void tearDown() {
        //delete model
        cityDelete.setWhere("id = "+cityModel1.getId()); cityDelete.execute();
        cityDelete.setWhere("id = "+cityModel2.getId()); cityDelete.execute();
    }
}
