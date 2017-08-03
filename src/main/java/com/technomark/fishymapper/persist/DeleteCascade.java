package com.technomark.fishymapper.persist;

import com.technomark.fishymapper.annotation.TableJoin;
import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.helper.HelperAnnotation;
import com.technomark.fishymapper.parser.ParserDelete;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by troy on 8/1/17.
 */
public class DeleteCascade extends ReferencesProcessor {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DeleteCascade(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public IModel process(IModel referenceModel, String fieldName, IModel currentModel) {
        try {
            TableJoin joinAnnotation = HelperAnnotation.getTableJoin(currentModel.getClass(), fieldName);
            if (!joinAnnotation.cascade())
                return null;

            ReferencesHandler hadler = new ReferencesHandler(referenceModel);
            hadler.process(this);
            return hadler.getModel();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IModel processChild(IModel referenceModel, String fieldName, IModel currentModel) {
        try {
            TableJoin joinAnnotation = HelperAnnotation.getTableJoin(currentModel.getClass(), fieldName);
            if (!joinAnnotation.cascade())
                return null;

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
        if (currentModel.getId() != null) {
            ParserDelete parser = new ParserDelete(currentModel, namedParameterJdbcTemplate);
            parser.execute();
        }
    }
}
