package mypkg;

import com.mysql.cj.jdbc.MysqlDataSource;
import mypkg.db.DataSourceFactory;

import mypkg.ui.helpers.StageCoordinator;
import javafx.application.Application;
import javafx.stage.Stage;


import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Main extends Application {
    public static Connection connection;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.initStage(primaryStage);
        stageCoordinator.switchToLoginScene();
        primaryStage.show();



    }

    @Override
    public void init() {
        Properties prop = new Properties();

        FileInputStream fis = null;
        MysqlDataSource mysqlDataSource= null;

        try {
            fis = new FileInputStream(Main.class.getResource("/db.properties").getFile());
            prop.load(fis);
            DataSource ds = DataSourceFactory.getMysqlDataSource(prop.getProperty("UN"),prop.getProperty("pass"),"m7mdschema");

            connection = ds.getConnection();
        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        /* Terminate Database & Network Connections  */
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
