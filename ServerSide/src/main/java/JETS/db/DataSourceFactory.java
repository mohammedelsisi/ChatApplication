package JETS.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import JETS.ServerMain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory {


    private static Connection mysqlConnection;

    public static Connection getConnection() throws SQLException, IOException {
        if (mysqlConnection == null) {
            InputStream fis = ServerMain.class.getResourceAsStream("/db.properties");
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

