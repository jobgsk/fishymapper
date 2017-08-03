package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.test.City;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by troy on 8/1/17.
 */
public class ParserDeleteTest extends ParserTest {

    @Test
    public void test1() {

        assertNotEquals(null, cityModel1.getId());

        //select model
        citySelect.setWhere("id = "+cityModel1.getId());
        City testCity1 = (City) citySelect.getItem();

        assertEquals(cityModel1.getId(), testCity1.getId());

        //delete model
        cityDelete.setWhere("id = "+cityModel1.getId());
        cityDelete.execute();

        City cityModel2 = (City) citySelect.getItem();
        assertEquals(null, cityModel2);
    }

}
