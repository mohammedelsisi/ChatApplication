package JETS;

import JETS.db.DataSourceFactory;
import JETS.db.dao.UserDao;
import JETS.service.ChatServiceImp;
import JETS.service.ConnectionService;
import JETS.service.ConnectionServiceFactory;
import Services.ChatServiceInt;
import javafx.application.Application;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

public class ServerMain extends Application {
    public static Connection connection;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void init() throws Exception {
        try {
            Connection conn = DataSourceFactory.getConnection();
            UserDao userDao = new UserDao(conn);
            ConnectionService connectionService = ConnectionServiceFactory.getConnectionService();
            ChatServiceInt chatService = new ChatServiceImp();
            Registry reg = LocateRegistry.createRegistry(6253);
            reg.rebind("UserRegistrationService",userDao);
            reg.rebind("ConnectionService",connectionService);
            reg.rebind("ChatService", chatService);
        } catch (SQLException | RemoteException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.show();
    }

}
