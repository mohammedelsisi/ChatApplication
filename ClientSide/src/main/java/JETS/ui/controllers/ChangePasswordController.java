package JETS.ui.controllers;

import JETS.ui.helpers.ModelsFactory;
import Models.CurrentUser;
import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

public class ChangePasswordController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane chPassRoot;

    @FXML
    private  JFXPasswordField pfOldPassword ;

    @FXML
    private  JFXPasswordField pfNewPassword;

    @FXML
    private  JFXPasswordField pfConfirmedNewPassword;

   public CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();

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
            if (!newValue.equals(pfNewPassword.getText())){
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

    public  void handleChangePassword(Optional<ButtonType> clickedButton) {

        String oldPassword = null;
        String newPassword = null;
        String confirmNewPassword = null;

        if (clickedButton.get() == ButtonType.CANCEL) {
            return;
        } else if (clickedButton.get() == ButtonType.APPLY) {

            System.out.println( currentUser.getPassword());

            oldPassword = pfOldPassword.getText();
            System.out.println(oldPassword);
            if (!checkOldPassword(oldPassword)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Entered Password Doesn't match the old one.");
                alert.showAndWait();
            } else if (newPassword != confirmNewPassword) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Password Values must Be identical");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Change Password ");
                alert.setHeaderText(null);
                alert.setContentText("Password Updated Successfully!");
                alert.showAndWait();
            }

//            //regex to match the old password
//            validator.setRegexPattern("[1-5](\\.[0-9]{1,2}){0,1}|6(\\.0{1,2}){0,1}");
//            validator.setMessage("Please enter proper value");
//            validationField.getValidators().add(validator);
//            validationField.focusedProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue)
//                    validationField.validate();
//            });

        }
        }
        public boolean checkOldPassword (String oldPassword){
            String oldPasswordValue = ModelsFactory.getInstance().getCurrentUser().getPassword();
            if (oldPassword.equals(oldPasswordValue)) return true;
            return false;
        }


}
