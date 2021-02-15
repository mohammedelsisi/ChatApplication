package JETS.ui.controllers;

import JETS.ui.helpers.ModelsFactory;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.validation.ValidationSupport;

import javax.xml.validation.Validator;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private AnchorPane chPassRoot;

    @FXML
    private JFXPasswordField pfOldPassword;

    @FXML
    private JFXPasswordField pfNewPassword;

    @FXML
    private JFXPasswordField pfConfirmedNewPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //common validation
        //only numbers allowed validator
        NumberValidator onlyNumbers = new NumberValidator();
        onlyNumbers.setMessage("only numbers are allowed");

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

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Password doesn't Match");
        pfConfirmedNewPassword.getValidators().add(requiredInputField);
        pfConfirmedNewPassword.textProperty().addListener((observableValue, oldvalue, newValue) -> {
            if (!newValue.equals(pfNewPassword.getText())){
                pfConfirmedNewPassword.validate();
            }
        });
        pfConfirmedNewPassword.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) pfConfirmedNewPassword.validate();
        });
    }



}
