package com.technomark.fishymapper.dao;

//import org.jobgsk.testspring.dbmapper.IModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by troy on 6/28/17.
 */
public interface IGenericDAO<M extends IModel> {
    public M insert(final M obj) throws SQLException;
    public M update(final Long id, final M obj) throws SQLException;
    public void delete(final Long id) throws SQLException;

    public M getItem(final Long id) throws SQLException;
    public List<M> getList() throws SQLException;

}
