package com.technomark.fishymapper.parser;

import com.technomark.fishymapper.dao.IModel;
import com.technomark.fishymapper.parser.ParserGeneral;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.SQLException;

/**
 * Created by troy on 6/28/17.
 */
//public class ParserInsert<M> extends ParserGeneral<M> {
public class ParserInsert extends ParserGeneral {

    private String INSERT_SQL1;
    private String INSERT_SQL2;

    //public ParserInsert(Class<M> clazz, M obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
    //    super(clazz, obj, jdbcTemplate);
    public ParserInsert(IModel obj, NamedParameterJdbcTemplate jdbcTemplate) throws SQLException {
        super(obj, jdbcTemplate);

        INSERT_SQL1 = "INSERT INTO "+this.tableName+" (";
        INSERT_SQL2 = ") VALUES (";
        buldQuery();
    }

    @Override
    public void addQuery(String name, int count) {
        if (count == 0) {
            INSERT_SQL1 += name;
            INSERT_SQL2 += ":"+name;
        }
        else {
            INSERT_SQL1 += ", " + name;
            INSERT_SQL2 += ", :"+ name;
        }
    }

    public String getQuery() {
        return INSERT_SQL1 + INSERT_SQL2 + ");";
    }

    @Override
    public void execute() {
        this.namedParameterJdbcTemplate.update(getQuery(), getParameters());
    }


    public Long execute(String keyName) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        //System.out.println(getSelectQuery());
        namedParameterJdbcTemplate.update(getQuery(), getParameters(), keyHolder, new String[]{keyName});
        Long primaryKey = keyHolder.getKey().longValue();
        return primaryKey;
    }
}
