package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.net.ClientProxy;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.appNotifications;
import Models.CurrentUser;
import com.jfoenix.controls.JFXPasswordField;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

public class ChangePasswordController {

    public CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane chPassRoot;
    @FXML
    private JFXPasswordField pfOldPassword;
    @FXML
    private JFXPasswordField pfNewPassword;
    @FXML
    private JFXPasswordField pfConfirmedNewPassword;

    @FXML
    void initialize() {

        //Required field validator
        RequiredFieldValidator requiredInputField = new RequiredFieldValidator();
        requiredInputField.setMessage("* Filed Can not be Empty");

        pfOldPassword.getValidators().add(requiredInputField);
        pfOldPassword.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) pfOldPassword.validate();
        });

        pfNewPassword.disableProperty().bind((
                pfOldPassword.textProperty().isNotEmpty().not()));

        pfConfirmedNewPassword.disableProperty().bind((
                pfOldPassword.textProperty().isNotEmpty().and(
                        pfNewPassword.textProperty().isNotEmpty()).not()));


        pfConfirmedNewPassword.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(pfNewPassword.getText())) {
                RequiredFieldValidator validator = new RequiredFieldValidator();
                validator.setMessage("Password doesn't Match");
                pfConfirmedNewPassword.getValidators().add(requiredInputField);
                pfConfirmedNewPassword.validate();
            }
        });
        pfConfirmedNewPassword.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) pfConfirmedNewPassword.validate();
        });

    }

    public void handleChangePassword() throws SQLException, RemoteException {

        String oldPassword = pfOldPassword.getText();
        String newPassword = pfNewPassword.getText();
        String confirmNewPassword = pfConfirmedNewPassword.getText();

        if (!checkOldPassword(oldPassword)) {
            appNotifications.getInstance().errorBox("Entered Password Doesn't match the old one.","Wrong old password");
        } else if (!newPassword.equals(confirmNewPassword)) {
            appNotifications.getInstance().errorBox("Password Values must Be identical","Doesn't Match!");

        } else {

            currentUser.setPassword(newPassword);
            CurrentUser user = ClientProxy.getInstance().update(currentUser);
            if(user != null) {
                ModelsFactory.getInstance().setCurrentUser(user);
                appNotifications.getInstance().okai("wohooo! Password Updated Successfully!","Successfully Changed");
            }
            pfOldPassword.getScene().getWindow().hide();
        }

    }


    public boolean checkOldPassword(String oldPassword) {
        String oldPasswordValue = ModelsFactory.getInstance().getCurrentUser().getPassword();
        if (oldPassword.equals(oldPasswordValue)) return true;
        return false;
    }


    public void cancelPane(ActionEvent actionEvent) {
        pfOldPassword.getScene().getWindow().hide();
    }
}
