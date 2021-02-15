package JETS.ui.controllers;
import JETS.ClientMain;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.CurrentUser;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jfoenix.controls.JFXTextArea;
import com.mysql.cj.log.Log;
import com.neovisionaries.i18n.CountryCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Border;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.Chronology;
import java.util.*;

public class SignUpController implements Initializable {
    public JFXTextArea bio;
    private String code;
    public static List<CountryCodeData> countryCodesList=new ArrayList<>();
    @FXML
    private ComboBox countryCode;
    @FXML
    DatePicker datePicker;
    @FXML
    private TextField phoneNumber;
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    @FXML
    private TextField emailAddress;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmedPassword;
    @FXML
    private TextField displayName;
    @FXML
    private ComboBox gender;
    private boolean firstTimeChkPass=true;
    @FXML
     private ImageView photoImageView;
    File photoPath;
    //REGISTRATION VALIDATION ATTRIBUTES
    boolean isPhoneNumberCorrect=false;
    boolean isPasswordCorrect=false;
    boolean isNameCorrect=false;
    boolean isEmailCorrect=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Phone number validation
        LocalDate minDate = LocalDate.now().minusYears(80);
        LocalDate maxDate = LocalDate.now().minusYears(18) ;
        datePicker.setValue(maxDate);
        loadCountryCodes();
        Collections.sort(countryCodesList);
        countryCode.getItems().addAll(countryCodesList);
        countryCode.setVisibleRowCount(6);
        countryCode.getSelectionModel().select(63);
        countryCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                CountryCodeData countryCodeData=(CountryCodeData) t1;
                code="+"+countryCodeData.getCode();
                phoneNumber.setText("");
                isPasswordCorrect=false;

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
                    isPhoneNumberCorrect=false;
                }else{
                    passValidation(phoneNumber);
                    isPhoneNumberCorrect=true;
                }

        });
        //Email Address validation
        emailAddress.textProperty().addListener((arg0, oldValue, newValue) -> {


                if(!isValidEmail(newValue)){
                    showError(emailAddress,"Invalid Email");
                    isEmailCorrect=false;
                }else {
                    passValidation(emailAddress);
                    isEmailCorrect=true;
                }

        });
        //DisplayName validation
        displayName.textProperty().addListener((arg0, oldValue, newValue) -> {

                if(displayName.getText().length()<3){
                    showError(displayName,"Password must be at least 3 characters");
                    isNameCorrect=false;
                }else{
                    passValidation(displayName);
                    isNameCorrect=true;
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
                    isPasswordCorrect=false;
                }else{
                    passValidation(confirmedPassword);
                    isPasswordCorrect=true;
                }

        });


    }
    @FXML
    public void changePhoto(ActionEvent e){
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().add(
          new FileChooser.ExtensionFilter("Image","*.jpg","*.png","*.jpeg")
        );
         photoPath=chooser.showOpenDialog(displayName.getScene().getWindow());
         try {

             if (photoPath!=null) {
                 photoImageView.setImage(new Image(photoPath.toURI().toURL().toExternalForm()));
             }
         }catch (MalformedURLException ex){

         }
    }
    @FXML
    public void registerHandle(ActionEvent e){
        if (isPhoneNumberCorrect&&isEmailCorrect&&isNameCorrect&&isPasswordCorrect&&!gender.getValue().toString().equals("Gender")){
            CurrentUser user=new CurrentUser();
            user.setPhoneNumber(code+phoneNumber.getText());
            user.setEmail(emailAddress.getText());
            user.setDisplayName(displayName.getText());
            user.setPassword(password.getText());
            user.setGender(gender.getValue().toString().toUpperCase());
            user.setAge(Period.between(datePicker.getValue(),LocalDate.now()).getYears());
            user.setBio(bio.getText());
            user.setPhotoPath(photoPath);
            ModelsFactory.getInstance().register(user);
//            ClientMain.userDAO.create(user);
            //change scene
        }
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
        private void loadCountryCodes(){
            Set<String> set = phoneNumberUtil.getSupportedRegions();

            String[] arr = set.toArray(new String[set.size()]);

            for (int i = 0; i < arr.length; i++) {

                countryCodesList.add(new CountryCodeData(PhoneNumberUtil.getInstance().getCountryCodeForRegion(arr[i]),CountryCode.getByCode(arr[i]).getName()));
            }
         }

    public void SignInAction(MouseEvent mouseEvent) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToLoginScene();
    }
}