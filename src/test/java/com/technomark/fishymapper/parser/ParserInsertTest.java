package com.technomark.fishymapper.parser;


import com.technomark.fishymapper.test.Address;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Created by troy on 7/31/17.
 */
public class ParserInsertTest extends ParserTest{

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

        //select model
        addressSelect.setWhere("id = "+addressModel1.getId());
        Address testAddress1 = (Address) addressSelect.getItem();

        assertEquals(addressModel1.getId(), testAddress1.getId());
        assertEquals(addressModel1.getCity().getId(), cityModel1.getId());
        assertEquals(addressModel1.getPerson().getId(), personModel1.getId());

        addressSelect.setWhere("id = "+addressModel3.getId());
        Address testAddress3 = (Address) addressSelect.getItem();

        assertEquals(addressModel3.getId(), testAddress3.getId());
        assertEquals(addressModel3.getCity().getId(), cityModel2.getId());
        assertEquals(addressModel3.getPerson().getId(), personModel2.getId());

    }

}
