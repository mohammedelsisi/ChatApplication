package JETS;

import JETS.ClientServices.ClientServicesFactory;
import JETS.ui.helpers.ClientImp;
import JETS.ui.helpers.ConfigurationHandler;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.CurrentUser;
import Models.LoginEntity;
import Services.Chatting;
import Services.ChatDao;
import Services.ChatServiceInt;
import Services.ConnectionInt;
import Services.DAOInterface;
import Services.UserFriendDaoInterface;
import com.mysql.cj.log.Log;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.Properties;


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

        try {

            //read the properties file and based on the result switch to the proper scene(Basiony)
            LoginEntity loginEntity = ConfigurationHandler.getInstance().getLoginEntity();
            CurrentUser currentUser = ClientMain.userDAO.findByPhoneAndPassword(loginEntity);
            if (currentUser != null) {
                ModelsFactory.getInstance().setCurrentUser(currentUser);
                ClientMain.chatting.register(new ClientImp(), currentUser.getPhoneNumber());
                ClientMain.connectionInt.registerAsConnected(ClientServicesFactory.getClientServicesImp());
                stageCoordinator.switchToChatScene();
            } else {
                StageCoordinator.getInstance().switchToLoginScene();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        primaryStage.show();

    }

    public void rememberMeAction(ActionEvent actionEvent) throws RemoteException {

    }

    @Override
    public void init() {
        try {
            Registry registry = LocateRegistry.getRegistry(6272);
            userDAO = (DAOInterface<CurrentUser>) registry.lookup("UserRegistrationService");
            connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            chatServiceInt = (ChatServiceInt) registry.lookup("ChatService");
            chatDao = (ChatDao) registry.lookup("ChatDao");
            chatting = (Chatting) registry.lookup("ChattingService");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }

}
