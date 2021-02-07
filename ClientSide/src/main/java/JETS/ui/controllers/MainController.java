package JETS.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import JETS.ui.helpers.StageCoordinator;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public BorderPane leftBorderPane;
    public BorderPane rightBorderBane;
    public HBox hbox;
    public Button registerBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void registerAction(ActionEvent actionEvent) {

        StageCoordinator stageCoordinator=StageCoordinator.getInstance();
        stageCoordinator.switchToSignUPScene();
    }

}