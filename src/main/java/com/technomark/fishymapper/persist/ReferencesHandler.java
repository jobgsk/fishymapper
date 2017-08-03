package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.annotation.TableJoin;
import com.technomark.fishymapper.dao.IModel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by troy on 8/1/17.
 */
public class ReferencesHandler {

    private IModel currentModel;
    private Class<? extends IModel> currentClazz;

    private IModel parrentModel;
    private Class<? extends IModel> parrentClazz;
    private String parrentField;

    public ReferencesHandler(IModel currentModel) {
        this.currentModel = currentModel;
        this.currentClazz = currentModel.getClass();
    }

    public ReferencesHandler(IModel currentModel, IModel parrentModel, String parrentField) {
        this(currentModel);

        this.parrentModel = parrentModel;
        this.parrentClazz = parrentModel.getClass();
        this.parrentField = parrentField;
    }

    public IModel getModel() {
        return this.currentModel;
    }

    public void process(ReferencesProcessor persist) throws SQLException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //primary references
        for (Field field : currentClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableJoin.class)) {
                String fieldName = field.getName();
                String getter = "get"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                String setter = "set"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                Object referenceModel = currentClazz.getMethod(getter).invoke(currentModel);
                if (referenceModel == null)
                    continue;

                if (Collection.class.isAssignableFrom(field.getType()))
                    continue;

                IModel createdModel = persist.process((IModel) referenceModel, fieldName, currentModel);
                //currentClazz.getMethod(setter, createdModel.getClass()).invoke(currentModel, createdModel);
            }
        }

        //main reference
        if (parrentModel != null) {
            Field field = parrentClazz.getDeclaredField(parrentField);
            Annotation annotation = field.getAnnotation(TableJoin.class);
            TableJoin joinAnnotation = (TableJoin) annotation;

            //set reference to parrent model
            String referenceName = joinAnnotation.mappedBy();
            String referenceSetter = "set"+Character.toUpperCase(referenceName.charAt(0)) + referenceName.substring(1);
            currentClazz.getMethod(referenceSetter, parrentModel.getClass()).invoke(currentModel, parrentModel);
        }
        persist.persist(currentModel);

        //secondary references
        for (Field field : currentClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableJoin.class)) {
                String fieldName = field.getName();
                String getter = "get"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                Object referenceValue = currentClazz.getMethod(getter).invoke(currentModel);
                if (referenceValue == null)
                    continue;

                if (!Collection.class.isAssignableFrom(field.getType()))
                    continue;

                Collection referenceList = (Collection) referenceValue;

                if (referenceList.isEmpty()) {
                    continue;
                }

                for (Object referenceModel: referenceList) {
                    IModel createdModel = persist.processChild((IModel) referenceModel, fieldName, currentModel);
                }
            }
        }
    }

}
