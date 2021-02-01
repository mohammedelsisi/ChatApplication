package mypkg.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory {


    private static MysqlDataSource mysqlDataSource;


    public static DataSource getMysqlDataSource(Properties file, String database) {
        if (mysqlDataSource == null) {
            mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUser(file.getProperty("userName"));
            mysqlDataSource.setPassword(file.getProperty("password"));
            mysqlDataSource.setDatabaseName(database);
            return mysqlDataSource;
        }
        return null;
    }

    public static DataSource getMysqlDataSource(String userName, String password, String database) {
        if (mysqlDataSource == null) {
            mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUser(userName);
            mysqlDataSource.setPassword(password);
            mysqlDataSource.setDatabaseName(database);
            return mysqlDataSource;
        }
        return null;
    }
    private DataSourceFactory(){

    }

}

