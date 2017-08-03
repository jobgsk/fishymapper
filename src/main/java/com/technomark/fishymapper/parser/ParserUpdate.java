package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.parser.ParserGeneral;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;

/**
 * Created by troy on 6/28/17.
 */
//public class ParserUpdate<M> extends ParserGeneral<M> {
public class ParserUpdate extends ParserGeneral {

    private String UPDATE_SQL;
    private String WHERE_SQL;

    //public ParserUpdate(Class<M> clazz, M obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
    //    super(clazz, obj, jdbcTemplate);
    public ParserUpdate(IModel obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
        super(obj, jdbcTemplate);

        UPDATE_SQL = "UPDATE "+this.tableName+" SET ";
        WHERE_SQL = "id = "+obj.getId();
        buldQuery();
    }

    @Override
    public void addQuery(String name, int count) {
        if (count == 0) {
            UPDATE_SQL += name + " = :" + name;
        }
        else {
            UPDATE_SQL += ", " + name + " = :" + name;
        }
    }

    public String getQuery() {
        if (WHERE_SQL != null)
            return UPDATE_SQL + " WHERE " + WHERE_SQL;
        return UPDATE_SQL;
    }

    public void setWhere(String where) {
        WHERE_SQL = where;
    }

    @Override
    public void execute() {
        //System.out.println(getSelectQuery());
        this.namedParameterJdbcTemplate.update(getQuery(), getParameters());
    }

}
