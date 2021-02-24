package JETS.ui.helpers;

import JETS.db.DataSourceFactory;
import JETS.db.dao.UserDao;
import JETS.db.dao.UserFriendDao;
import JETS.service.*;
import Services.ChatServiceInt;
import Services.FileService;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;

public class ServiceController {
    private static final ServiceController serviceController = new ServiceController();
    Registry reg;
    UserDao userDao;
    ConnectionService connectionService;
    ChattingImp chattingImp;
    UserFriendDao userFriendDao;
    ChatServiceInt chatService;
    ChatDaoImp chatDaoImp;
    FileService fileService;
    private Connection conn;

    private ServiceController() {
        try {

            reg = LocateRegistry.createRegistry(9999);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ServiceController getServiceController() {
        return serviceController;
    }

    public void initializeService() {
        try {

            conn = DataSourceFactory.getConnection();

            userDao = new UserDao(conn);
            connectionService = ConnectionServiceFactory.getConnectionService();
            userFriendDao = new UserFriendDao(conn);
            chatService = new ChatServiceImp();
            chatDaoImp = new ChatDaoImp(conn);
            fileService = new FileServiceImpl();
            chattingImp = new ChattingImp(userFriendDao, userDao);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void startService() {
        try {

            reg.rebind("UserRegistrationService", userDao);
            reg.rebind("ConnectionService", connectionService);
            reg.rebind("ChatService", chatService);
            reg.rebind("UserFriendDao", userFriendDao);
            reg.rebind("ChatDao", chatDaoImp);
            reg.rebind("ChattingService", chattingImp);
            reg.rebind("FileService", fileService);

        } catch (IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void stopService() {
        try {
            reg.unbind("UserRegistrationService");
            reg.unbind("ConnectionService");
            reg.unbind("ChatService");
            reg.unbind("UserFriendDao");
            reg.unbind("ChatDao");
            reg.unbind("ChattingService");
            reg.unbind("FileService");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void terminateService() {
        try {

            UnicastRemoteObject.unexportObject(userDao, true);
            UnicastRemoteObject.unexportObject(connectionService, true);
            UnicastRemoteObject.unexportObject(chattingImp, true);
            UnicastRemoteObject.unexportObject(chatService, true);
            UnicastRemoteObject.unexportObject(chatDaoImp, true);
            UnicastRemoteObject.unexportObject(fileService, true);
            UnicastRemoteObject.unexportObject(userFriendDao, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }

    }
}
