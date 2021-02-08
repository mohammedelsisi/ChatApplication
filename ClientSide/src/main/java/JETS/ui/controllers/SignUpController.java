package JETS.ui.controllers;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mysql.cj.log.Log;
import com.neovisionaries.i18n.CountryCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.util.*;

public class SignUpController implements Initializable {
    private String code;
    public static List<CountryCodeData> countryCodesList=new ArrayList<>();
    @FXML
    private ComboBox countryCode;
    @FXML
    DatePicker datePicker=new DatePicker();

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
        LocalDate minDate = LocalDate.now().minusYears(80);
        LocalDate maxDate = LocalDate.now().minusYears(18) ;
        datePicker.setValue(maxDate);
        foo();
        countryCode.getItems().addAll(countryCodesList);
        countryCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                CountryCodeData countryCodeData=(CountryCodeData) t1;
                code="+"+countryCodeData.getCode();
            }
        });


        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));

                    }});
        phoneNumber.textProperty().addListener((arg0, oldValue, newValue) -> {


                if(!givenPhoneNumber_whenValid_thenOK(code+newValue)){
                    showError(phoneNumber,"Invalid phone number");
                }else{
                    passValidation(phoneNumber);
                }

        });
        //Email Address validation
        emailAddress.textProperty().addListener((arg0, oldValue, newValue) -> {


                if(!isValidEmail(newValue)){
                    showError(emailAddress,"Invalid Email");
                }else {
                    passValidation(emailAddress);
                }

        });
        //DisplayName validation
        displayName.textProperty().addListener((arg0, oldValue, newValue) -> {

                if(displayName.getText().length()<3){
                    showError(displayName,"Password must be at least 3 characters");
                }else{
                    passValidation(displayName);
                }

        });
        //password validation "Enter at least 6 chars"
        password.textProperty().addListener((arg0, oldValue, newValue) -> {


                if(newValue.length()<6){
                    showError(password,"Password must be at least 6 characters");
                }else {
                    passValidation(password);
                }
                if(!firstTimeChkPass){
                    confirmedPassword.setText("");

                }
            firstTimeChkPass=false;

        });
        //password validation
        confirmedPassword.textProperty().addListener((arg0, oldValue, newValue) -> {


                if(!password.getText().equals(newValue)){

                    showError(confirmedPassword,"Password mismatch");
                }else{
                    passValidation(confirmedPassword);
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

        private static void showError(TextField textField,String msg){
            Tooltip t=new Tooltip(msg);

            textField.setStyle("-fx-border-color: red; -fx-border-radius: 4px; -fx-border-width: 2px;");

            textField.setTooltip(t);

        }
        private static void passValidation(TextField textField){
            textField.setStyle("-fx-border-color: green; -fx-border-radius: 4px; -fx-border-width: 2px;");
            textField.setTooltip(null);
        }
        public  static void foo(){
            Set<String> set = PhoneNumberUtil.getInstance().getSupportedRegions();

            String[] arr = set.toArray(new String[set.size()]);

            for (int i = 0; i < arr.length; i++) {

                countryCodesList.add(new CountryCodeData(PhoneNumberUtil.getInstance().getCountryCodeForRegion(arr[i]),CountryCode.getByCode(arr[i]).getName()));
            }
         }
}