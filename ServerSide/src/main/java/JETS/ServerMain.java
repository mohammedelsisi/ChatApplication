package JETS;

import JETS.db.DataSourceFactory;
import JETS.db.dao.UserDao;
import JETS.db.dao.UserFriendDao;
import JETS.service.ChattingImp;
import JETS.service.ChatDaoImp;
import JETS.service.ChatServiceImp;
import JETS.service.ConnectionService;
import JETS.service.ConnectionServiceFactory;
import JETS.ui.helpers.StageCoordinator;
import Services.ChatServiceInt;
import javafx.application.Application;
import javafx.stage.Stage;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

public class ServerMain extends Application {
    public static Connection connection;
    public static ServerDao serverDao;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void init() throws Exception {
        try {
            Connection conn = DataSourceFactory.getConnection();
            UserDao userDao = new UserDao(conn);

        } catch (SQLException | RemoteException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.initStage(primaryStage);
        stageCoordinator.switchToMainScene();
        primaryStage.show();
    }

}
