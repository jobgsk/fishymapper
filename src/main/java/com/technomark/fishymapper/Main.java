package com.technomark.fishymapper;


import com.technomark.fishymapper.helper.DataSourceFactory;
import com.technomark.fishymapper.persist.DeleteCascade;
import com.technomark.fishymapper.persist.PersistantMerge;
import com.technomark.fishymapper.test.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        DataSource dataSource = DataSourceFactory.getInstance().getDataSource();

        TransactionTemplate transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource));

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        IPersonDAO personDao = new PersonDAO(dataSource);
        ICityDAO cityDao = new CityDAO(dataSource);
        IAdderessDAO addressDao = new AddressDAO(dataSource);

        City c1 = cityDao.insert(new City("Toronto"));
        City c2 = cityDao.insert(new City("Ottawa"));

        //TransactionCallback
        final Person p1 = transactionTemplate.execute(new TransactionCallback<Person>() {

            public Person doInTransaction(TransactionStatus transactionStatus) {
                try {

                    //create address
                    Address a1 = new Address("Main St.", c1);
                    Address a2 = new Address("Bay St.", c1);
                    Address a3 = new Address("Dundas St.", c2);

                    Person p1 = new Person("Java 1", "Honk");
                    p1.setAddresses(a1);
                    p1.setAddresses(a2);
                    p1.setAddresses(a3);

                    PersistantMerge pm = new PersistantMerge(dataSource);
                    pm.exec(p1);

                    System.out.println(p1);
                    System.out.println(a1);
                    System.out.println(a2);
                    System.out.println(a3);

                    //if (true)
                    //    throw new Exception("rollback test"); //DataAccessException

                    return p1;

                } catch (Exception e) {
                    System.out.println(e);
                    transactionStatus.setRollbackOnly();
                }
                return null;
            }
        });

        //Select all inserted user from the table
        System.out.println("\nList of persons in the table\n");
        List<Person> personList = personDao.selectJoin();
        for (Person p: personList) {
            System.out.println(p);
        }

        //TransactionCallbackWithoutResult
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {

                    DeleteCascade deleteCascade = new DeleteCascade(dataSource);
                    deleteCascade.exec(p1);
                } catch (Exception e) {
                    System.out.println(e);
                    transactionStatus.setRollbackOnly();
                }
            }
        });

        Person person1; Person person2; Person person3;
        Address address1; Address address2; Address address3;
        TransactionStatus transactionStatus1 = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            person1 = personDao.insert(new Person("Java 1", "Honk"));
            person2 = personDao.insert(new Person("Java 2", "Honk"));
            person3 = personDao.insert(new Person("Java 3", "Honk"));

            System.out.println("\nPerson1 inserted to the table "+person1);
            System.out.println("Person2 inserted to the table "+person2);
            System.out.println("Person3 inserted to the table "+person3);

            //create address
            address1 = addressDao.insert(new Address("Main St.", c1, person1));
            address2 = addressDao.insert(new Address("Bay St.", c1, person2));
            address3 = addressDao.insert(new Address("Dundas St.", c2, person3));

            System.out.println("\nAddress 1 "+address1);
            System.out.println("Address 2 "+address2);
            System.out.println("Address 3 "+address3);

            //Select data from tabel where person
            System.out.println("\nSelect Person from the table "+person3);
            Person selectedPerson1 = personDao.getItem(person3.getId());
            System.out.println("Selected person "+selectedPerson1);

            //count
            int count = personDao.countOfPersons(person1);
            System.out.println("\nCount of persons "+count);

            //update
            System.out.println("\nUpdete person from the table "+person2);
            Person updatedPerson = personDao.update(person2.getId(), new Person("Updated 2", "Honk"));
            System.out.println("Updated person "+updatedPerson);

            //if (true)
            //    throw new Exception("rollback test"); //DataAccessException

            transactionManager.commit(transactionStatus1);
        }
        catch (Exception e) {
            System.out.println("Error in creating record, rolling back");
            transactionManager.rollback(transactionStatus1);
            throw e;
        }

        TransactionStatus transactionStatus2 = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //Select all inserted user from the table
            System.out.println("\nList of persons in the table\n");
            List<Person> plist = personDao.selectJoin();
            for (Iterator<Person> iterator = plist.iterator(); iterator.hasNext();) {
                Person model = (Person)iterator.next();
                System.out.println(model);
            }


            addressDao.delete(address1.getId());
            addressDao.delete(address2.getId());
            addressDao.delete(address3.getId());

            personDao.delete(person1.getId());
            personDao.delete(person2.getId());
            personDao.delete(person3.getId());

            cityDao.delete(c1.getId());
            cityDao.delete(c2.getId());

            transactionManager.commit(transactionStatus2);
        }
        catch (Exception e) {
            System.out.println("Error in creating record, rolling back");
            transactionManager.rollback(transactionStatus2);
            throw e;
        }

        System.out.println("\nList of persons after delete "+personDao.getList().size());
    }
}
