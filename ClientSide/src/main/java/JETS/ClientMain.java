package JETS;

import JETS.ClientServices.ClientServicesFactory;
import JETS.net.ClientProxy;
import JETS.ui.helpers.ConfigurationHandler;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import JETS.ui.helpers.appNotifications;
import Models.CurrentUser;
import Models.LoginEntity;
import Services.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;


public class ClientMain extends Application {

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
            CurrentUser currentUser = ClientProxy.getInstance().findByPhoneAndPassword(loginEntity);
            if (currentUser != null) {
                if (ClientProxy.getInstance().isConnected(loginEntity.getPhoneNumber())){
                    StageCoordinator.getInstance().switchToLoginScene();
                    appNotifications.getInstance().okai("You are Already Connected, Can't login Twice","Login Failed");
                }else {
                    ModelsFactory.getInstance().setCurrentUser(currentUser);
                    ClientProxy.getInstance().registerAsConnected(ClientServicesFactory.getClientServicesImp());
                    stageCoordinator.switchToChatScene();
                }
            } else {
                System.out.println("Server is Down");
                StageCoordinator.getInstance().switchToLoginScene();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        primaryStage.show();

    }


    @Override
    public void init() {

    }

    @Override
    public void stop() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(ClientServicesFactory.getClientServicesImp(),true);

    }

}
