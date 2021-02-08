package JETS.ui.controllers;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
