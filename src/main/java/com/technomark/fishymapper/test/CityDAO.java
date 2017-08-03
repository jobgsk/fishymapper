package com.technomark.fishymapper.test;

import com.technomark.fishymapper.dao.GenericDAO;
import com.technomark.fishymapper.test.City;
import com.technomark.fishymapper.test.ICityDAO;

import javax.sql.DataSource;

/**
 * Created by troy on 7/20/17.
 */
public class CityDAO extends GenericDAO<City> implements ICityDAO {

    public CityDAO(DataSource dataSource) {
        super(City.class, dataSource);
    }
}
