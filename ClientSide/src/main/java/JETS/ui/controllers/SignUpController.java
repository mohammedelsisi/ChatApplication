package JETS.ui.controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private TextField phoneNumber;
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    @FXML
    private TextField emailAddress;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmedPassword;
    @FXML
    private TextField displayName;
    private boolean firstTimeChkPass=true;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Phone number validation
        phoneNumber.focusedProperty().addListener((arg0, oldValue, newValue) -> {

            if (!newValue) { //when focus lost
                if(!givenPhoneNumber_whenValid_thenOK(phoneNumber.getText())){

                    phoneNumber.setText("");
                }
            }
        });
        //Email Address validation
        emailAddress.focusedProperty().addListener((arg0, oldValue, newValue) -> {

            if (!newValue) { //when focus lost
                if(!isValidEmail(emailAddress.getText())){

                    emailAddress.setText("");
                }
            }
        });
        //DisplayName validation
        displayName.focusedProperty().addListener((arg0, oldValue, newValue) -> {

            if (!newValue) { //when focus lost
            }
        });
        //password validation "Enter at least 6 chars"
        password.focusedProperty().addListener((arg0, oldValue, newValue) -> {

            if (!newValue) { //when focus lost
                if(password.getText().length()<6){
                    password.setText("");
                }
                if(!firstTimeChkPass){
                    confirmedPassword.setText("");
                }
                firstTimeChkPass=false;
            }
        });
        //password validation
        confirmedPassword.focusedProperty().addListener((arg0, oldValue, newValue) -> {

            if (!newValue) { //when focus lost
                if(!password.getText().equals(confirmedPassword.getText())){
                    confirmedPassword.setText("");

                }
            }
        });
    }
    @FXML
    public void changePhoto(ActionEvent e){
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().add(
          new FileChooser.ExtensionFilter("Image","*.jpg","*.png","*.jpeg")
        );
        File file=chooser.showOpenDialog(displayName.getScene().getWindow());
    }
    public Boolean givenPhoneNumber_whenValid_thenOK(String phoneNumber)  {
       try {
           Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(phoneNumber,
                   Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
           return phoneNumberUtil.isValidNumber(phone);
       }catch ( NumberParseException e){
            //e.printStackTrace();
            return false;
       }
    }

        public static boolean isValidEmail(String email) {
            // create the EmailValidator instance
            EmailValidator validator = EmailValidator.getInstance();

            // check for valid email addresses using isValid method
            return validator.isValid(email);
        }
}