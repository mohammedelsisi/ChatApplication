package JETS;

import JETS.db.DataSourceFactory;
import JETS.db.dao.UserDao;
import JETS.db.dao.UserFriendDao;
import JETS.service.ChattingImp;
import JETS.service.ChatDaoImp;
import JETS.service.ChatServiceImp;
import JETS.service.ConnectionService;
import JETS.service.ConnectionServiceFactory;
import Services.ChatServiceInt;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

public class ServerMain extends Application {
  //  public static Connection connection;

    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage){

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Server.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
