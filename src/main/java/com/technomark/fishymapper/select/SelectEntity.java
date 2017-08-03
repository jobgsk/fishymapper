package com.technomark.fishymapper.select;

import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.helper.HelperAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by troy on 6/28/17.
 */
public class SelectEntity { //extends ParserGeneral<M> {

    private Class clazz;
    private String tableName;
    private String alias;
    private String parenJoinField = null;

    private List<SelectEntity> joinList = new ArrayList<SelectEntity>();
    private List<IModel> modelList = new ArrayList<IModel>();


    public SelectEntity(Class clazz, String alias) throws SQLException {
        super();
        this.clazz = clazz;

        this.tableName = HelperAnnotation.getTableName(clazz);
        if (this.tableName == null) {
            throw new SQLException("1 Class does not have TableEntity annotation");
        }

        if (alias == null)
            this.alias = "__"+this.tableName;
        else
            this.alias = alias;
    }

    public SelectEntity(Class clazz) throws SQLException {
        this(clazz, null);
    }

    //addJoin("i", images);
    public SelectEntity joinEntity(Class<? extends IModel> entityType, String alias, String fieldName) throws SQLException {
        try {
            clazz.getDeclaredField("dsdsa");
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        }
        /*Class<? extends IModel> entityType = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableJoin.class) &&
                    fieldName.equals(field.getName())) {

                entityType = (Class<? extends IModel>) field.getType();
                break;
            }
        }

        System.out.println(entityType);

        if (entityType == null) {
            throw new SQLException("Join Entity does not have TableJoin annotation");
        }*/

        SelectEntity parser = new SelectEntity(entityType, alias);
        parser.parenJoinField = fieldName;
        joinList.add(parser);
        return parser;
    }

    public SelectEntity joinEntity(Class<? extends IModel> entityType, String fieldName) throws SQLException {
        SelectEntity parser = new SelectEntity(entityType);
        parser.parenJoinField = fieldName;
        joinList.add(parser);
        return parser;
    }

    public String getParenJoinField() {
        return this.parenJoinField;
    }

    public List<SelectEntity> getJoinList() {
        return this.joinList;
    }

    public Class getEntityType() {
        return this.clazz;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getAlias() {
        return this.alias;
    }


    /*
     * save query results
     */
    public IModel getModel(Long modelId) {
        for (IModel m: this.modelList) {
            if (((long)m.getId()) == ((long)modelId))
                return m;
        }
        return null;
    }

    public IModel createModel(ResultSet rs) {
        IModel createdModel = HelperAnnotation.createModel(clazz, alias, rs);
        if (createdModel == null)
            return null;

        IModel existingModel = getModel(createdModel.getId());
        if (existingModel != null)
            createdModel = existingModel;

        for(SelectEntity p: getJoinList()) {

            IModel joinModel = p.createModel(rs);

            try {
                String parrentField = p.getParenJoinField();
                //Field test = clazz.getDeclaredField(parrentField);

                String parrentSetter = "set" + Character.toUpperCase(parrentField.charAt(0)) + parrentField.substring(1);
                clazz.getMethod(parrentSetter, p.getEntityType()).invoke(createdModel, joinModel);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return createdModel;
    }

    public void addModel(IModel model) {
        if (getModel(model.getId()) != null)
            return;


        this.modelList.add(model);
    }

    public List<IModel> getModelList() {
        return this.modelList;
    }

    public void resetModelList() {
        this.modelList = new ArrayList<IModel>();
    }
}
