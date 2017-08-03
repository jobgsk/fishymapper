package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.annotation.TableJoin;
import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.parser.ParserGeneral;
import com.technomark.fishymapper.parser.ParserInsert;
import com.technomark.fishymapper.parser.ParserUpdate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by troy on 8/1/17.
 */
public class PersistantMerge extends ReferencesProcessor {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PersistantMerge(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public IModel process(IModel referenceModel, String fieldName, IModel currentModel) {
        try {
            ReferencesHandler hadler = new ReferencesHandler(referenceModel);
            hadler.process(this);
            IModel createdModel = hadler.getModel();

            Class<? extends IModel> currentClazz = currentModel.getClass();
            String setter = "set"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            currentClazz.getMethod(setter, createdModel.getClass()).invoke(currentModel, createdModel);

            return hadler.getModel();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IModel processChild(IModel referenceModel, String fieldName, IModel currentModel) {
        try {

            /*Class<? extends IModel> currentClazz = currentModel.getClass();
            Class<? extends IModel> referenceClazz = referenceModel.getClass();

            Field field = currentClazz.getDeclaredField(fieldName);
            Annotation annotation = field.getAnnotation(TableJoin.class);
            TableJoin joinAnnotation = (TableJoin) annotation;

            //set reference to parrent model
            String referenceName = joinAnnotation.mappedBy();
            String referenceSetter = "set"+Character.toUpperCase(referenceName.charAt(0)) + referenceName.substring(1);
            referenceClazz.getMethod(referenceSetter, currentModel.getClass()).invoke(referenceModel, currentModel);
            */

            ReferencesHandler hadler = new ReferencesHandler(referenceModel, currentModel, fieldName);
            hadler.process(this);
            return hadler.getModel();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void persist(IModel currentModel) throws SQLException {
        ParserGeneral parser;
        Long primaryKey;

        if (currentModel.getId() != null) {
            parser = new ParserUpdate(currentModel, namedParameterJdbcTemplate);
            parser.execute();
            primaryKey = currentModel.getId();
        }
        else {
            parser = new ParserInsert(currentModel, namedParameterJdbcTemplate);
            primaryKey = ((ParserInsert)parser).execute("id");
        }

        //IModel createdModel = ((IModel) model).clone();
        currentModel.setId(primaryKey);
        //currentModel = model;
    }

}
