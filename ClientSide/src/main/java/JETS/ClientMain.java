package JETS;

import JETS.ui.helpers.ClientImp;
import JETS.ui.helpers.StageCoordinator;
import Models.CurrentUser;
import Services.Chatting;
import Services.ChatDao;
import Services.ChatServiceInt;
import Services.ConnectionInt;
import Services.DAOInterface;
import Services.UserFriendDaoInterface;
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

    public static Chatting chatting;

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
            Registry registry = LocateRegistry.getRegistry(6270);
            userDAO = (DAOInterface<CurrentUser>)registry.lookup("UserRegistrationService");
            connectionInt= (ConnectionInt) registry.lookup("ConnectionService");
            chatServiceInt= (ChatServiceInt) registry.lookup("ChatService");
            chatDao= (ChatDao) registry.lookup("ChatDao");
            chatting=(Chatting) registry.lookup("ChattingService");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }

}
