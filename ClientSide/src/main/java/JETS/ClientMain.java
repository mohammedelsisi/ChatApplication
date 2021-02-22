package JETS;

import JETS.ClientServices.ClientServicesFactory;
import JETS.net.ClientProxy;
import JETS.ui.helpers.ConfigurationHandler;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.CurrentUser;
import Models.LoginEntity;
import Services.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
                    System.out.println("You are already connected");
                    StageCoordinator.getInstance().switchToLoginScene();
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

    public void rememberMeAction(ActionEvent actionEvent) throws RemoteException {

    }

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

}
