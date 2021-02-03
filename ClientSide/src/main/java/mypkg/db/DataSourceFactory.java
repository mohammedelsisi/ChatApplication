package mypkg.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import mypkg.Main;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory {


    private static Connection mysqlConnection;

    public static Connection getConnection() throws SQLException, IOException {
        if (mysqlConnection == null) {
            FileInputStream fis = new FileInputStream(Main.class.getResource("/db.properties").getFile());
            Properties prop = new Properties();
            prop.load(fis);
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUser(prop.getProperty("username"));
            ds.setPassword(prop.getProperty("password"));;
            ds.setDatabaseName(prop.getProperty("database"));
            mysqlConnection=ds.getConnection();
        }
        return mysqlConnection;
    }
    private DataSourceFactory() {
    }

}

