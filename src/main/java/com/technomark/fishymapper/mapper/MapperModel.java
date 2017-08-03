package com.technomark.fishymapper.mapper;

import com.technomark.fishymapper.annotation.TableEntity;
import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.helper.HelperAnnotation;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by troy on 6/29/17.
 */
public class MapperModel<M extends IModel> implements RowMapper<IModel> {

    protected Class<M> clazz;
    public MapperModel(Class<M> clazz) {
        super();
        this.clazz = clazz;
    }

    public IModel mapRow(ResultSet rs, int rowNum) throws SQLException {

        Annotation tableAnnotation = clazz.getAnnotation(TableEntity.class);
        TableEntity dt = (TableEntity) tableAnnotation;
        String prefix = "__" + dt.name();
        IModel model = HelperAnnotation.createModel(clazz, prefix, rs);
        return model;
    }
}
