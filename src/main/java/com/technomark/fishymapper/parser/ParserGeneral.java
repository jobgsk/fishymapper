package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.annotation.TableColumn;
import com.technomark.fishymapper.annotation.TableEntity;
import com.technomark.fishymapper.annotation.TableJoin;
import com.technomark.fishymapper.dao.IModel;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;

//public abstract class ParserGeneral<M> {
public abstract class ParserGeneral {

    //protected Class<M> clazz;
    //protected M obj;

    protected Class<? extends IModel> clazz;
    protected IModel obj;

    protected String tableName;
    protected MapSqlParameterSource namedParameters;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //private ParserGeneral(Class<M> clazz, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
    protected ParserGeneral(Class<? extends IModel> clazz, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {

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
    }

    //public ParserGeneral(Class<M> clazz, M obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
    public ParserGeneral(IModel obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
        this(obj.getClass(), jdbcTemplate);
        this.obj = obj;
    }

    protected void buldQuery() throws SQLException {

        int count = 0;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableColumn.class)) {

                Annotation annotation = field.getAnnotation(TableColumn.class);
                TableColumn df = (TableColumn) annotation;

                TableColumn.FieldType columnType = df.type();
                String columnName = df.name();

                //Class fieldType = field.getType();
                String fieldName = field.getName();
                String getter = "get"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                try {
                    Object result = clazz.getMethod(getter).invoke(obj);

                    boolean autogen = df.autogen();
                    if (autogen)
                        continue;

                    boolean nullable = df.nullable();
                    if (!nullable && result == null)
                        continue;

                    if (field.isAnnotationPresent(TableJoin.class)) {
                        result = ((IModel) result).getId();
                        columnType = TableColumn.FieldType.LONG;
                    }

                    //System.out.println("buldQuery "+getter+" "+name+" "+result);

                    addQuery(columnName, count);
                    addParameret(columnName, columnType, result);

                    count++;
                } catch (Throwable ex) {
                    System.out.println(ex);
                }
            }
        }
    }


    /*
     * fill parameters
     */
    protected void addParameret(String name, TableColumn.FieldType type, Object result) {

        if (type == TableColumn.FieldType.VARCHAR)
            namedParameters.addValue(name, (String)result);

        else if (type == TableColumn.FieldType.LONG)
            namedParameters.addValue(name, (Long)result);

        else if (type == TableColumn.FieldType.INT)
            namedParameters.addValue(name, (Integer)result);
    }

    public void setInt(String name, Long value) {
        namedParameters.addValue(name, value);
    }

    public MapSqlParameterSource getParameters() {
        return namedParameters;
    }

    public abstract void addQuery(String name, int count);
    public abstract void execute();

}
