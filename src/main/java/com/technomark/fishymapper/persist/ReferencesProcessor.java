package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.dao.IModel;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by troy on 8/1/17.
 */
public abstract class ReferencesProcessor {

    public abstract void persist(IModel currentModel) throws SQLException;

    public IModel exec(IModel currentModel) {
        try {
            ReferencesHandler hadler = new ReferencesHandler(currentModel);
            hadler.process(this);
            return hadler.getModel();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IModel process(IModel referenceModel, String fieldName, IModel currentModel) {
        try {
            ReferencesHandler hadler = new ReferencesHandler(referenceModel);
            hadler.process(this);
            return hadler.getModel();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IModel processChild(IModel referenceModel, String fieldName, IModel currentModel) {
        try {
            ReferencesHandler hadler = new ReferencesHandler(referenceModel, currentModel, fieldName);
            hadler.process(this);
            return hadler.getModel();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
