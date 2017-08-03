package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.annotation.TableEntity;
import com.technomark.fishymapper.dao.IModel;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.annotation.Annotation;
import java.sql.SQLException;

//public class ParserDelete<M> extends ParserGeneral<M> {
public class ParserDelete  {
    protected Class<? extends IModel> clazz;
    protected IModel obj;

    protected String tableName;
    protected MapSqlParameterSource namedParameters;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private String DELETE_QUERY;
    private String WHERE_SQL;

    public ParserDelete(Class<? extends IModel> clazz, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
        super();
        this.clazz = clazz;

        if (!clazz.isAnnotationPresent(TableEntity.class)) {
            throw new SQLException("Class does not have TableEntity annotation");
        }

        this.namedParameterJdbcTemplate = jdbcTemplate;
        this.namedParameters = new MapSqlParameterSource();

        Annotation tableAnnotation = clazz.getAnnotation(TableEntity.class);
        TableEntity dt = (TableEntity) tableAnnotation;

        this.tableName = dt.name();

        DELETE_QUERY = "DELETE FROM " + this.tableName;
        WHERE_SQL = null;
    }

    public ParserDelete(IModel obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
        this(obj.getClass(), jdbcTemplate);
        this.obj = obj;
        WHERE_SQL = "id = "+obj.getId();
    }

    public String getQuery() {
        if (WHERE_SQL != null)
            return  DELETE_QUERY + " WHERE " + WHERE_SQL;
        return DELETE_QUERY;
    }

    public void setWhere(String where) {
        WHERE_SQL = where;
    }

    public void execute() {
        if (obj != null) {

        }
        this.namedParameterJdbcTemplate.update(getQuery(), this.namedParameters);
    }
}
