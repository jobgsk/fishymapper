package com.technomark.fishymapper.test;

import com.technomark.fishymapper.dao.GenericDAO;
import com.technomark.fishymapper.test.Address;
import com.technomark.fishymapper.test.IAdderessDAO;

import javax.sql.DataSource;

/**
 * Created by troy on 7/20/17.
 */
public class AddressDAO extends GenericDAO<Address> implements IAdderessDAO {

    public AddressDAO(DataSource dataSource) {
        super(Address.class, dataSource);
    }
}
