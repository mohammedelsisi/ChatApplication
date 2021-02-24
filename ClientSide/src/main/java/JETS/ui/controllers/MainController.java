package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.ClientServices.ClientServicesFactory;
import JETS.net.ClientProxy;
import JETS.ui.helpers.ConfigurationHandler;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import JETS.ui.helpers.appNotifications;
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
        try {

            CurrentUser currentUser = ClientProxy.getInstance().findByPhoneAndPassword(loginEntity);


            if (currentUser != null) {
                if (ClientProxy.getInstance().isConnected(loginEntity.getPhoneNumber())) {
                    appNotifications.getInstance().errorBox("You are Already Connected, Can't login Twice", "Login Failed");
                } else {
                    ModelsFactory.getInstance().setCurrentUser(currentUser);
                    ClientProxy.getInstance().registerAsConnected(ClientServicesFactory.getClientServicesImp());
                    StageCoordinator stageCoordinator = StageCoordinator.getInstance();
                    stageCoordinator.switchToChatScene();
                    ConfigurationHandler.getInstance().rememberMe(loginEntity);
                    password.clear();
                }
            } else {
                String msg = "Please Enter Valid PhoneNumber & Password or please SignUP";
                String title = "USER NOT FOUND!";
                appNotifications.getInstance().okai(msg, title);
            }

        }catch (RuntimeException ss){
            appNotifications.getInstance().errorBox("Sorry, Our Service isn't Available at the Moment. Please, Come back later","Server Down");

        }
    }
}
