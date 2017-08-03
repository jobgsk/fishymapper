package com.technomark.fishymapper.test;

import com.technomark.fishymapper.dao.IGenericDAO;
import com.technomark.fishymapper.test.Person;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by troy on 6/28/17.
 */
public interface IPersonDAO extends IGenericDAO<Person> {

    public List<Person> selectJoin() throws SQLException;
    public int countOfPersons(Person person) throws SQLException;
}
