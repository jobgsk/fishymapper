package com.technomark.fishymapper.test;

//import org.jobgsk.testspring.dbmapper.SelectBuilder;
import com.technomark.fishymapper.dao.GenericDAO;
import com.technomark.fishymapper.select.SelectBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by troy on 6/28/17.
 */
public class PersonDAO extends GenericDAO<Person> implements IPersonDAO {

    public PersonDAO(DataSource dataSource) {
        super(Person.class, dataSource);
    }

    @Override
    public List<Person> selectJoin() throws SQLException {
        //SelectBuilder parser = new SelectBuilder(namedParameterJdbcTemplate, Person.class, "p");
        //parser.joinEntity(Address.class, "a", "addresses");

        SelectBuilder builder = new SelectBuilder(namedParameterJdbcTemplate, Person.class, "p");
        builder.joinEntity(Address.class, "a","addresses")
            .joinEntity(City.class, "c", "city");

        String cityName = "Toronto";
        builder.setWhere("c.name = '"+cityName+"'");

        System.out.println("Test Parser "+ builder.getSelectQuery());
        return  (List<Person>)builder.getList();
    }

    @Override
    public int countOfPersons(Person person) throws SQLException {
        SelectBuilder builder = new SelectBuilder(namedParameterJdbcTemplate, Person.class, "p");
        builder.setWhere("First_Name = :firstName and Last_Name = :lastName");
        builder.setString("firstName", person.getFirstName());
        builder.setString("lastName", person.getLastName());
        return builder.getCount();
    }
}
