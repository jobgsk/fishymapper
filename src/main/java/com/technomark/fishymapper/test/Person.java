package com.technomark.fishymapper.test;


import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.annotation.TableColumn;
import com.technomark.fishymapper.annotation.TableEntity;
import com.technomark.fishymapper.annotation.TableJoin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@TableEntity(name = "test1_person")
public class Person implements IModel, Serializable{

    private static final long serialVersionUID = 1L;

    @TableColumn(name = "id", type = TableColumn.FieldType.LONG, autogen = true)
    private Long id;
    @TableColumn(name = "First_Name", type = TableColumn.FieldType.VARCHAR)
    private String firstName;
    @TableColumn(name = "Last_Name", type = TableColumn.FieldType.VARCHAR)
    private String lastName;

    @TableJoin(mappedBy = "person", joinType = TableJoin.JoinType.INNER, cascade = true)
    private List<Address> addresses;

    public Person() {
        super();
        addresses = new ArrayList<Address>();
    }

    public Person(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public IModel clone() {
        Person model = new Person();
        model.firstName = this.firstName;
        model.lastName = this.lastName;
        model.addresses = this.addresses;
        return model;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public boolean isAddress(Address model) {
        for (Address a: this.addresses) {
            if (a.equals(model))
                return true;
        }
        return false;
    }

    public void setAddresses(Address model) {
        Address addressModel = (Address) model;
        if (isAddress(addressModel))
            return;
        this.addresses.add(addressModel);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addresses=" + addresses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        //if (((long)id) != ((long)person.id)) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        return lastName != null ? lastName.equals(person.lastName) : person.lastName == null;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result.intValue();
    }
}