package JETS;

import JETS.ui.helpers.StageCoordinator;
import Models.CurrentUser;
import Services.ChatDao;
import Services.ChatServiceInt;
import Services.ConnectionInt;
import Services.DAOInterface;
import javafx.application.Application;
import javafx.stage.Stage;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;


public class ClientMain extends Application {
    public static DAOInterface<CurrentUser> userDAO;
    public static ConnectionInt connectionInt;
    public static ChatServiceInt chatServiceInt;
    public static ChatDao chatDao;

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
        try {
            Registry registry = LocateRegistry.getRegistry(6258);
            userDAO = (DAOInterface<CurrentUser>)registry.lookup("UserRegistrationService");
            connectionInt= (ConnectionInt) registry.lookup("ConnectionService");
            chatServiceInt= (ChatServiceInt) registry.lookup("ChatService");
            chatDao= (ChatDao) registry.lookup("ChatDao");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }

}
