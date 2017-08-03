package com.technomark.fishymapper.test;

import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.annotation.TableColumn;
import com.technomark.fishymapper.annotation.TableEntity;

import java.io.Serializable;

/**
 * Created by troy on 7/2/17.
 */
@TableEntity(name = "test1_city")
public class City implements IModel, Serializable {

    private static final long serialVersionUID = 1L;

    @TableColumn(name = "id", type = TableColumn.FieldType.LONG, autogen = true)
    private Long id;

    @TableColumn(name = "name", type = TableColumn.FieldType.VARCHAR)
    private String name;

    public City() {

    }

    public City(String name) {
        this.name = name;
    }

    @Override
    public IModel clone() {
        City model = new City();
        model.name = this.name;
        return model;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        //if (((long)id) != ((long)city.id)) return false;
        if (id != null ? !id.equals(city.id) : city.id != null) return false;
        return name != null ? name.equals(city.name) : city.name == null;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result.intValue();
    }
}
