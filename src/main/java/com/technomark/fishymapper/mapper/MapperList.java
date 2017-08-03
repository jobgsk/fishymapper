package com.technomark.fishymapper.mapper;

import com.technomark.fishymapper.select.SelectEntity;
import com.technomark.fishymapper.dao.IModel;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapperList implements RowCallbackHandler {

    private SelectEntity parserMain;

    private List<ResultSet> resultList;

    public MapperList(SelectEntity parserMain) {
        super();
        this.resultList = new ArrayList<ResultSet>();
        this.parserMain = parserMain;
    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {
        resultList.add(rs);
        IModel model = parserMain.createModel(rs);
        parserMain.addModel(model);
    }


    public List<IModel> getList() {
        return parserMain.getModelList();
    }
}
