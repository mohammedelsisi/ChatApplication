package JETS;

import JETS.ClientServices.ClientServicesFactory;
import JETS.ClientServices.ClientServicesImp;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.ChatEntitiy;
import Models.CurrentUser;
import Models.MessageEntity;
import Services.ChatServiceInt;
import Services.ConnectionInt;
import Services.DAOInterface;
import javafx.application.Application;
import javafx.stage.Stage;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClientMain extends Application {
    public static DAOInterface<CurrentUser> userDAO;
    public static ConnectionInt connectionInt;

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
            Registry registry = LocateRegistry.getRegistry(6253);
            userDAO = (DAOInterface<CurrentUser>)registry.lookup("UserRegistrationService");
            connectionInt= (ConnectionInt) registry.lookup("ConnectionService");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }

}
