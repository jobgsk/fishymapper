package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.test.City;
import org.junit.*;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by troy on 7/31/17.
 */
public class ParserUpdateTest extends ParserTest{

    @Test
    public void test1() {
        try {
            assertNotEquals(null, cityModel1.getId());

            //select model
            citySelect.setWhere("id = "+cityModel1.getId());
            City cityModel1 = (City) citySelect.getItem();

            assertEquals(cityModel1.getId(), cityModel1.getId());

            //update model
            String cityName = "Test Name";
            cityModel1.setName(cityName);

            ParserUpdate updateParser = new ParserUpdate(cityModel1, namedParameterJdbcTemplate);
            updateParser.execute();

            City cityModel2 = (City) citySelect.getItem();
            assertEquals(cityName, cityModel2.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
