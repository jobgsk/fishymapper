package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.helper.DataSourceFactory;
import com.technomark.fishymapper.test.Address;
import com.technomark.fishymapper.test.City;
import com.technomark.fishymapper.test.Person;
import com.technomark.fishymapper.select.SelectBuilder;
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
public abstract class ParserTest {

    protected static DataSource dataSource;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected SelectBuilder personSelect;
    protected SelectBuilder addressSelect;
    protected SelectBuilder citySelect;

    protected ParserDelete cityDelete;
    protected ParserDelete personDelete;
    protected ParserDelete addressDelete;

    protected City cityModel1 = null;
    protected City cityModel2 = null;

    protected Person personModel1 = null;
    protected Person personModel2 = null;
    protected Person personModel3 = null;

    protected Address addressModel1 = null;
    protected Address addressModel2 = null;
    protected Address addressModel3 = null;

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


            personModel1 = new Person("Java 1", "Honk");
            personModel2= new Person("Java 2", "Honk");
            personModel3 = new Person("Java 3", "Honk");

            ParserInsert personInsert1 = new ParserInsert(personModel1, namedParameterJdbcTemplate);
            ParserInsert personInsert2 = new ParserInsert(personModel2, namedParameterJdbcTemplate);
            ParserInsert personInsert3 = new ParserInsert(personModel3, namedParameterJdbcTemplate);

            personModel1.setId(personInsert1.execute("id"));
            personModel2.setId(personInsert2.execute("id"));
            personModel3.setId(personInsert3.execute("id"));


            addressModel1 = new Address("Main St.", cityModel1, personModel1);
            addressModel2 = new Address("Bay St.", cityModel2, personModel1);
            addressModel3 = new Address("Dundas St.", cityModel2, personModel2);

            ParserInsert addressInsert1 = new ParserInsert(addressModel1, namedParameterJdbcTemplate);
            ParserInsert addressInsert2 = new ParserInsert(addressModel2, namedParameterJdbcTemplate);
            ParserInsert addressInsert3 = new ParserInsert(addressModel3, namedParameterJdbcTemplate);

            addressModel1.setId(addressInsert1.execute("id"));
            addressModel2.setId(addressInsert2.execute("id"));
            addressModel3.setId(addressInsert3.execute("id"));

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
        // Do something after each test (run twice in this example)
        //delete model
        addressDelete.setWhere("id = "+addressModel1.getId());
        addressDelete.execute();

        addressDelete.setWhere("id = "+addressModel2.getId());
        addressDelete.execute();

        addressDelete.setWhere("id = "+addressModel3.getId());
        addressDelete.execute();


        personDelete.setWhere("id = "+personModel1.getId());
        personDelete.execute();

        personDelete.setWhere("id = "+personModel2.getId());
        personDelete.execute();

        personDelete.setWhere("id = "+personModel3.getId());
        personDelete.execute();

        cityDelete.setWhere("id = "+cityModel1.getId());
        cityDelete.execute();

        cityDelete.setWhere("id = "+cityModel2.getId());
        cityDelete.execute();
    }

    @AfterClass
    public static void tearDownClass() {
        // Do something after ALL tests have been run (run once)
    }
}
