package com.technomark.fishymapper.select;

import com.technomark.fishymapper.annotation.TableColumn;
import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.helper.HelperAnnotation;
import com.technomark.fishymapper.mapper.MapperList;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by troy on 7/1/17.
 */
public class SelectBuilder {

    private MapSqlParameterSource namedParameters;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SelectEntity mainEntity;

    private String WHERE_SQL = null;

    public SelectBuilder(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Class clazz, String alias) throws SQLException {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.namedParameters = new MapSqlParameterSource();
        mainEntity = new SelectEntity(clazz, alias);
    }

    public SelectBuilder(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Class clazz) throws SQLException {
        this(namedParameterJdbcTemplate, clazz, null);
    }

    public SelectEntity joinEntity(Class<? extends IModel> entityType, String alias, String fieldName) throws SQLException {
        return mainEntity.joinEntity(entityType, alias, fieldName);
    }

    public SelectEntity joinEntity(Class<? extends IModel> entityType, String fieldName) throws SQLException {
        return mainEntity.joinEntity(entityType, fieldName);
    }

    /*
        Class entityType = this.clazz;
        String entityAlias = this.alias;
     */
    private String buildSelectQuery(List<SelectEntity> joinList, Class entityType, String entityAlias) throws SQLException {
        int count = 0;
        String query = "";

        for (Field field : entityType.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableColumn.class)) {

                Annotation annotation = field.getAnnotation(TableColumn.class);
                TableColumn df = (TableColumn) annotation;

                try {
                    String name = df.name();

                    if (count == 0) {
                        query += entityAlias + "." + name;
                    } else {
                        query += ", " + entityAlias + "." + name;
                    }

                    count++;
                } catch (Throwable ex) {
                    System.out.println(ex);
                }
            }
        }

        for (SelectEntity e: joinList) {
            query += ", " + buildSelectQuery(e.getJoinList(), e.getEntityType(), e.getAlias());
        }

        return query;
    }



    /*
        joinType join TableName as alias on parentAlias.referencedField = alias.joinField
        Class entityType = this.clazz;
        String entityAlias = this.alias;
     */
    private String buildJoinQuery(List<SelectEntity> joinList, Class entityType, String entityAlias) throws SQLException {
        String query = " ";

        for (SelectEntity e: joinList) {

            String joinType = HelperAnnotation.getJoinType(entityType, e.getParenJoinField());
            if (joinType == null)
            {
                throw new SQLException("Join Field does not have ColumnJoin annotation");
            }

            String tableName = HelperAnnotation.getTableName(e.getEntityType());
            if (tableName == null)
            {
                throw new SQLException("Join Entity does not have TableJoin annotation");
            }

            String alias = e.getAlias();

            String referencedField = HelperAnnotation.getReferencedField(entityType, e.getParenJoinField());
            if (referencedField == null)
            {
                throw new SQLException("Referenced Field does not have proper annotation");
            }

            String joinField = HelperAnnotation.getJoinField(entityType, e.getParenJoinField(), e.getEntityType());
            if (joinField == null)
            {
                System.out.println(joinType + " JOIN " + tableName + " AS " + alias + " ON " + entityAlias + "." + referencedField + " = " + alias + ".");
                throw new SQLException("Join Field does not have proper annotation");
            }

            query += joinType + " JOIN " + tableName + " AS " + alias + " ON " + entityAlias + "." + referencedField + " = " + alias + "." + joinField;
            query += buildJoinQuery(e.getJoinList(), e.getEntityType(), e.getAlias());
        }

        return query;
    }

    public String getSelectQuery() {
        String query = "SELECT ";
        try {
            query += buildSelectQuery(mainEntity.getJoinList(), mainEntity.getEntityType(), mainEntity.getAlias());

            query += " FROM " + mainEntity.getTableName() + " as " + mainEntity.getAlias();

            query += " " + buildJoinQuery(mainEntity.getJoinList(), mainEntity.getEntityType(), mainEntity.getAlias());

            if (WHERE_SQL != null)
                query += " WHERE " + WHERE_SQL;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return query;
    }

    public String getCountQuery() {
        String query = "SELECT COUNT(*) ";
        try {
            query += " FROM " + mainEntity.getTableName() + " as " + mainEntity.getAlias();

            query += " " + buildJoinQuery(mainEntity.getJoinList(), mainEntity.getEntityType(), mainEntity.getAlias());

            if (WHERE_SQL != null)
                query += " WHERE " + WHERE_SQL;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return query;
    }

    /*
     * parameters
     */
    public void setWhere(String where) {
        WHERE_SQL = where;
    }

    public void setInt(String name, Long value) {
        namedParameters.addValue(name, value);
    }

    public void setString(String name, String value) {
        namedParameters.addValue(name, value);
    }

    public MapSqlParameterSource getParameters() {
        return namedParameters;
    }

    /*
     * request
     */
    public int getCount() throws SQLException {
        return this.namedParameterJdbcTemplate.queryForObject(getCountQuery(), namedParameters, Integer.class);
    }

    public Object getItem() {
        try {
            //Object models = namedParameterJdbcTemplate.queryForObject(getSelectQuery(), namedParameters, new MapperModel(mainEntity.getEntityType()));
            //return models;
            List<? extends IModel> modelList = getList();
            if (modelList.size() == 0)
                return null;
            return modelList.get(0);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<? extends IModel> getList() {

        MapperList rowCallbackHandler = new MapperList(mainEntity);
        namedParameterJdbcTemplate.query(getSelectQuery(), namedParameters, rowCallbackHandler);
        //namedParameterJdbcTemplate.query(getSelectQuery(), rowCallbackHandler);
        List<? extends IModel> models = rowCallbackHandler.getList();
        mainEntity.resetModelList();
        return models;
    }
}
