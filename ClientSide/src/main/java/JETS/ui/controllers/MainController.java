package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.ClientServices.ClientServicesFactory;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.CurrentUser;
import Models.LoginEntity;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public BorderPane leftBorderPane;
    public BorderPane rightBorderBane;
    public HBox hbox;
    public Button registerBtn;
    public TextField phoneNumber;
    public PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void signUpAction(MouseEvent mouseEvent) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToSignUPScene();

    }

    public void signInAction(ActionEvent actionEvent) throws RemoteException {
        LoginEntity loginEntity = new LoginEntity(phoneNumber.getText(), password.getText());
        CurrentUser currentUser = ClientMain.userDAO.findByPhoneAndPassword(loginEntity);
        ModelsFactory.getInstance().setCurrentUser(currentUser);
        if (currentUser != null) {
            ClientMain.connectionInt.registerAsConnected(ClientServicesFactory.getClientServicesImp());
            StageCoordinator stageCoordinator = StageCoordinator.getInstance();
            stageCoordinator.switchToChatScene();
        } else {
            System.out.println("Not Valid ");
        }
    }
}
