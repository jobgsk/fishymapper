package com.technomark.fishymapper.dao;

import com.technomark.fishymapper.select.SelectBuilder;
import com.technomark.fishymapper.parser.ParserDelete;
import com.technomark.fishymapper.parser.ParserInsert;
import com.technomark.fishymapper.parser.ParserUpdate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by troy on 6/28/17.
 */
 abstract public class GenericDAO<M extends IModel> implements IGenericDAO<M> {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Class<M> clazz;

    public GenericDAO(Class<M> clazz, DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.clazz = clazz;
    }

    public M insert(final M obj) throws SQLException {
        //ParserInsert<M> parser = new ParserInsert<M>(this.clazz, obj, namedParameterJdbcTemplate);
        ParserInsert parser = new ParserInsert(obj, namedParameterJdbcTemplate);
        Long primaryKey = parser.execute("id");

        M model = (M) obj.clone();
        model.setId(primaryKey);
        return model;
    }

    public M update(final Long id, final M obj) throws SQLException {
        //ParserUpdate<M> parser = new ParserUpdate<M>(this.clazz, obj, namedParameterJdbcTemplate);
        ParserUpdate parser = new ParserUpdate(obj, namedParameterJdbcTemplate);
        parser.setWhere("id = "+id);
        parser.execute();

        M model = (M) obj.clone();
        model.setId(id);
        return model;
    }

    public void delete(final Long id) throws SQLException {
        //ParserDelete<M> parser = new ParserDelete<M>(this.clazz, namedParameterJdbcTemplate);
        ParserDelete parser = new ParserDelete(this.clazz, namedParameterJdbcTemplate);
        parser.setWhere("id = "+id);
        parser.execute();
    }

    public M getItem(Long id) throws SQLException {
        SelectBuilder parser = new SelectBuilder(namedParameterJdbcTemplate, clazz);

        parser.setWhere("id = "+id);
        //System.out.println("Get Item Query "+parser.getSelectQuery());
        return (M) parser.getItem();
    }

    public List<M> getList() throws SQLException {
        SelectBuilder parser = new SelectBuilder(namedParameterJdbcTemplate, clazz);
        return (List<M>) parser.getList();
    }
}
