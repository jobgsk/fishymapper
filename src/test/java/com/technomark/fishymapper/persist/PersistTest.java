package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.helper.DataSourceFactory;
import com.technomark.fishymapper.test.Address;
import com.technomark.fishymapper.test.Person;
import com.technomark.fishymapper.parser.ParserDelete;
import com.technomark.fishymapper.parser.ParserInsert;
import com.technomark.fishymapper.select.SelectBuilder;
import com.technomark.fishymapper.test.City;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by troy on 8/2/17.
 */
public abstract class PersistTest {

    protected   static DataSource dataSource;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected PersistantMerge persistantmanager;

    protected SelectBuilder personSelect;
    protected SelectBuilder addressSelect;
    protected SelectBuilder citySelect;

    protected ParserDelete cityDelete;
    protected ParserDelete personDelete;
    protected ParserDelete addressDelete;

    protected Person personModel = null;

    protected City cityModel1 = null;
    protected City cityModel2 = null;

    @BeforeClass
    public static void setUpClass() {
        dataSource = DataSourceFactory.getInstance().getDataSource();
        // Initialize stuff once for ALL tests (run once)
    }

    @Before
    public void setUp() {
        try {
            // Initialize stuff before every test (this is run twice in this example)
            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            cityModel1 = new City("Toronto");
            cityModel2 = new City("Ottawa");

            ParserInsert cityInsert1 = new ParserInsert(cityModel1, namedParameterJdbcTemplate);
            ParserInsert cityInsert2 = new ParserInsert(cityModel2, namedParameterJdbcTemplate);

            cityModel1.setId(cityInsert1.execute("id"));
            cityModel2.setId(cityInsert2.execute("id"));

            //create address
            Address a1 = new Address("Main St.", cityModel1);
            Address a2 = new Address("Bay St.", cityModel1);
            Address a3 = new Address("Dundas St.", cityModel2);

            personModel = new Person("Java 1", "Honk");
            personModel.setAddresses(a1);
            personModel.setAddresses(a2);
            personModel.setAddresses(a3);

            persistantmanager = new PersistantMerge(dataSource);

            addressSelect = new SelectBuilder(namedParameterJdbcTemplate, Address.class, "a");
            personSelect = new SelectBuilder(namedParameterJdbcTemplate, Person.class, "p");
            citySelect = new SelectBuilder(namedParameterJdbcTemplate, City.class, "c");

            cityDelete = new ParserDelete(City.class, namedParameterJdbcTemplate);
            addressDelete = new ParserDelete(Address.class, namedParameterJdbcTemplate);
            personDelete = new ParserDelete(Person.class, namedParameterJdbcTemplate);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        //delete model
        Address address1 = personModel.getAddresses().get(0);
        Address address2 = personModel.getAddresses().get(1);
        Address address3 = personModel.getAddresses().get(2);

        addressDelete.setWhere("id = "+address1.getId()); addressDelete.execute();
        addressDelete.setWhere("id = "+address2.getId()); addressDelete.execute();
        addressDelete.setWhere("id = "+address3.getId()); addressDelete.execute();

        personDelete.setWhere("id = "+personModel.getId()); personDelete.execute();

        cityDelete.setWhere("id = "+cityModel1.getId()); cityDelete.execute();
        cityDelete.setWhere("id = "+cityModel2.getId()); cityDelete.execute();
    }


    @AfterClass
    public static void tearDownClass() {
        // Do something after ALL tests have been run (run once)
    }
}
