package JETS;

import JETS.ui.helpers.StageCoordinator;
import javafx.application.Application;
import javafx.stage.Stage;


import java.sql.*;


public class ClientMain extends Application {


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


    }

    @Override
    public void stop() {

    }

}
