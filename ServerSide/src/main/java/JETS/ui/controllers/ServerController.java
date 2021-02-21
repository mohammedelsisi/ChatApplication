package JETS.ui.controllers;

import JETS.db.DataSourceFactory;
import JETS.db.dao.UserDao;
import JETS.db.dao.UserFriendDao;
import JETS.service.*;
import Services.ChatServiceInt;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private ToggleButton start;
    public Connection conn;
    public Registry reg = LocateRegistry.createRegistry(6253);
    public UserDao userDao;
    public  ConnectionService connectionService;
    public ChattingImp chattingImp;
    @FXML
    private TextArea announcement;

    public ServerController() throws RemoteException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void serverAction(){
        if(start.isSelected()== true){
            startServer();
        }else{
            stopServer();
        }

    }

    public void startServer() {

        try {
            Connection conn = DataSourceFactory.getConnection();
            UserDao userDao = new UserDao(conn);
            ConnectionService connectionService = ConnectionServiceFactory.getConnectionService();
            UserFriendDao userFriendDao = new UserFriendDao(conn);
            ChatServiceInt chatService = new ChatServiceImp();
            /*             method to get the last chat Id from database */
            ChatDaoImp chatDaoImp = new ChatDaoImp(conn);
            ChattingImp chattingImp = new ChattingImp(conn);
            //Registry reg = LocateRegistry.createRegistry(6270);
            reg.rebind("UserRegistrationService",userDao);
            reg.rebind("ConnectionService",connectionService);
            reg.rebind("ChatService", chatService);
            reg.rebind("UserFriendDao", userFriendDao);
            reg.rebind("ChatDao",chatDaoImp);
            reg.rebind("ChattingService", chattingImp);
            start.setText("Stop Service");
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }


    public void stopServer() {
        try {
            reg.unbind("UserRegistrationService");
            reg.unbind("ConnectionService");
            reg.unbind("ChattingService");
           /* UnicastRemoteObject.unexportObject(userDao,true);
            UnicastRemoteObject.unexportObject(connectionService,true);
            UnicastRemoteObject.unexportObject(chattingImp,true);*/
            start.setText("Start Service");
            conn.close();
            System.out.println("serverclosed");
        } catch (RemoteException | NotBoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void sentMessageNotification() throws FileNotFoundException {
           String announc = announcement.getText();
           ConnectionServiceFactory.getConnectionService().getConnectedClients().forEach((x,y)-> {
               try {
                   y.ReceiveAnnounc(announc);
               } catch (RemoteException e) {
                   e.printStackTrace();
               }
           });

    }

}

