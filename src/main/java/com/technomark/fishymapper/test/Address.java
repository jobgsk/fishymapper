package com.technomark.fishymapper.test;


import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.annotation.TableColumn;
import com.technomark.fishymapper.annotation.TableEntity;
import com.technomark.fishymapper.annotation.TableJoin;

import java.io.Serializable;

/**
 * Created by troy on 6/29/17.
 */
@TableEntity(name = "test1_address")
public class Address implements IModel, Serializable {

    private static final long serialVersionUID = 1L;

    @TableColumn(name = "id", type = TableColumn.FieldType.LONG, autogen = true)
    private Long id;

    @TableColumn(name = "street_name", type = TableColumn.FieldType.VARCHAR)
    private String street;

    @TableJoin(joinType = TableJoin.JoinType.INNER)
    @TableColumn(name = "city_id", type = TableColumn.FieldType.LONG)
    private City city;

    @TableJoin(mappedBy = "addresses", joinType = TableJoin.JoinType.INNER)
    @TableColumn(name = "person_id", type = TableColumn.FieldType.LONG)
    private Person person;

    public Address() {
        super();
    }

    public Address(String street, City city) {
        this();
        this.city = city;
        this.street = street;
    }

    public Address(String street, City city, Person person) {
        this();
        this.city = city;
        this.street = street;
        this.person = person;
    }

    public IModel clone() {
        Address model = new Address();
        model.city = this.city;
        model.street = this.street;
        model.person = this.person;
        return model;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                "street='" + street + '\'' +
                "person='" + person + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        //if (((long)id) != ((long)address.id)) return false;
        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        //if (person != null ? !person.equals(address.person) : address.person != null) return false;
        return city != null ? city.equals(address.city) : address.city == null;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result.intValue();
    }
}
