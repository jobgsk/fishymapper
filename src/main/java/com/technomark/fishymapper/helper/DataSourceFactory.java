package com.technomark.fishymapper.helper;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by troy on 8/1/17.
 */
public class DataSourceFactory {

    private static DataSourceFactory dsFactory = null;
    private BasicDataSource dataSource = null;

    private DataSourceFactory() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/properties"));

            Class.forName(props.getProperty("ds.database-driver"));

            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(props.getProperty("ds.database-driver"));
            dataSource.setUrl(props.getProperty("ds.url"));
            dataSource.setUsername(props.getProperty("ds.username"));
            dataSource.setPassword(props.getProperty("ds.password"));

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public static DataSourceFactory getInstance() {
        if (dsFactory == null) {
            dsFactory = new DataSourceFactory();
        }
        return dsFactory;
    }
}
