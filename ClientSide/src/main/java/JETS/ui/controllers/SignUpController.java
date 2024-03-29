package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.net.ClientProxy;
import JETS.security.SecurityProvider;
import JETS.security.enums.HashTypes;
import JETS.ui.helpers.StageCoordinator;
import JETS.ui.helpers.appNotifications;
import Models.CurrentUser;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jfoenix.controls.JFXTextArea;
import com.neovisionaries.i18n.CountryCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {
    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){2,18}[a-zA-Z0-9]$";
    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);
    public static List<CountryCodeData> countryCodesList = new ArrayList<>();
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    public JFXTextArea bio;
    public Circle circlePP;
    @FXML
    DatePicker datePicker;
    File photoPath;
    String phone;
    //REGISTRATION VALIDATION ATTRIBUTES
    boolean isPhoneNumberCorrect = false;
    boolean isPasswordCorrect = false;
    boolean isNameCorrect = false;
    boolean isEmailCorrect = false;
    byte[] photoBytes;
    private String code;
    @FXML
    private ComboBox countryCode;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField emailAddress;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmedPassword;
    @FXML
    private TextField displayName;
    @FXML
    private ComboBox gender;
    private boolean firstTimeChkPass = true;

    public static boolean isValidName(final String username) {

        return (username.length()>3);
    }

    public static boolean isValidEmail(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

    private static void showError(TextField textField, String msg) {
        Tooltip t = new Tooltip(msg);
        textField.setStyle("-fx-border-color: red; -fx-border-radius: 4px; -fx-border-width: 2px;");
        textField.setTooltip(t);


    }

    private static void passValidation(TextField textField) {
        textField.setStyle("-fx-border-color: green; -fx-border-radius: 4px; -fx-border-width: 2px;");
        textField.setTooltip(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Phone number validation
        LocalDate minDate = LocalDate.now().minusYears(80);
        LocalDate maxDate = LocalDate.now().minusYears(18);
        datePicker.setValue(maxDate);
        loadCountryCodes();
        Collections.sort(countryCodesList);
        countryCode.getItems().addAll(countryCodesList);
        countryCode.setVisibleRowCount(6);
        countryCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                CountryCodeData countryCodeData = (CountryCodeData) t1;

                code = "+" + countryCodeData.getCode();
                phoneNumber.setText("");
                password.setText("");
                confirmedPassword.setText("");
                isPasswordCorrect = false;

            }
        });
        countryCode.getSelectionModel().select(63);

        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));

                    }
                });
        phoneNumber.textProperty().addListener((arg0, oldValue, newValue) -> {


            if (!givenPhoneNumber_whenValid_thenOK(code + newValue)) {
                showError(phoneNumber, "Invalid phone number");
                isPhoneNumberCorrect = false;
            } else {
                passValidation(phoneNumber);
                isPhoneNumberCorrect = true;
            }

        });
        //Email Address validation
        emailAddress.textProperty().addListener((arg0, oldValue, newValue) -> {


            if (!isValidEmail(newValue)) {
                showError(emailAddress, "Invalid Email");
                isEmailCorrect = false;
            } else {
                passValidation(emailAddress);
                isEmailCorrect = true;
            }

        });
        //DisplayName validation
        displayName.textProperty().addListener((arg0, oldValue, newValue) -> {

            if (!isValidName(displayName.getText())) {
                showError(displayName, "User Name must be at least 4 characters");
                isNameCorrect = false;
            } else {
                passValidation(displayName);
                isNameCorrect = true;
            }

        });
        //password validation "Enter at least 6 chars"
        password.textProperty().addListener((arg0, oldValue, newValue) -> {


            if (newValue.length() < 6) {
                showError(password, "Password must be at least 6 characters");
            } else {
                passValidation(password);
            }
            if (!firstTimeChkPass) {
                confirmedPassword.setText("");

            }
            firstTimeChkPass = false;

        });
        //password validation
        confirmedPassword.textProperty().addListener((arg0, oldValue, newValue) -> {


            if (!password.getText().equals(newValue)) {

                showError(confirmedPassword, "Password Mismatch");
                isPasswordCorrect = false;
            } else {
                passValidation(confirmedPassword);
                isPasswordCorrect = true;
            }

        });


    }

    @FXML
    public void changePhoto(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png", "*.jpeg")
        );

        File selectedPhoto = chooser.showOpenDialog(displayName.getScene().getWindow());
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(selectedPhoto))) {
            photoBytes = bufferedInputStream.readAllBytes();
            circlePP.setFill(new ImagePattern(new Image(new ByteArrayInputStream(photoBytes))));
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }


    }

    @FXML
    public void registerHandle(ActionEvent e) {
        try {


            if (phoneNumber.getText().isBlank() || password.getText().isBlank() || confirmedPassword.getText().isBlank() || gender.getValue().toString().equals("Gender") || displayName.getText().isBlank() || emailAddress.getText().isBlank()) {
                appNotifications.getInstance().okai("Please Fill all Registration Fields", "Registration Form");
                validateFields();
            } else if (isPhoneNumberCorrect && isEmailCorrect && isNameCorrect && isPasswordCorrect && !gender.getValue().toString().equals("Gender")) {
                phone = phoneNumber.getText();
                String country = ((CountryCodeData) (countryCode.getSelectionModel().getSelectedItem())).getCountryName();
                if (phone.length() == 11 && country.equals("Egypt")) phone = phone.substring(1);
                CurrentUser user = new CurrentUser();
                user.setPhoneNumber(code + phone);
                user.setEmail(emailAddress.getText());
                user.setDisplayName(displayName.getText());
                user.setPassword(password.getText());
                user.setGender(gender.getValue().toString().toUpperCase());
                user.setDOB(datePicker.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                user.setBio(bio.getText());
                user.setStatus("AVAILABLE");
                if (photoBytes==null){
                    if(user.getGender().equals("MALE")){


                        try (InputStream bufferedInputStream =getClass().getResourceAsStream("/Pics/Male.png")) {
                            photoBytes = bufferedInputStream.readAllBytes();
                        } catch (IOException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                    }else if (user.getGender().equals("FEMALE"))
                        try (InputStream bufferedInputStream =getClass().getResourceAsStream("/Pics/Female.png")) {
                        photoBytes = bufferedInputStream.readAllBytes();
                    } catch (IOException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                }
                user.setUserPhoto(photoBytes);
                user.setCountry(country);
                ClientProxy.getInstance().create(user);
                appNotifications.getInstance().okai("Registration Successfully, You Can Login Now to LongTalk", "Successful Registration");
                StageCoordinator.getInstance().switchToLoginScene();
                MainController controller = StageCoordinator.getInstance().getScenes().get("MainScene").getLoader().getController();
                controller.phoneNumber.setText(code + phone);
                controller.password.requestFocus();
            } else {
                appNotifications.getInstance().errorBox("Sorry, but there are some invalid information. Fill fields with right data till you make the fields green", "Registration Form");

            }

        } catch (SQLException ee) {
            if (ee.getErrorCode() == 1062) {
                appNotifications.getInstance().errorBox("This Number is already Registered", "Registration Form");

            }
        } catch (RuntimeException ss) {
            appNotifications.getInstance().errorBox(ss.getMessage(), "Service Down");

        }
    }

    public Boolean givenPhoneNumber_whenValid_thenOK(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(phoneNumber,

                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumber(phone);
        } catch (NumberParseException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private void loadCountryCodes() {
        Set<String> set = phoneNumberUtil.getSupportedRegions();

        String[] arr = set.toArray(new String[set.size()]);
        for (int i = 0; i < arr.length; i++) {
            countryCodesList.add(new CountryCodeData(PhoneNumberUtil.getInstance().getCountryCodeForRegion(arr[i]), CountryCode.getByCode(arr[i]).getName()));
        }
    }


    public void SignInAction(MouseEvent mouseEvent) {
        StageCoordinator stageCoordinator = StageCoordinator.getInstance();
        stageCoordinator.switchToLoginScene();
    }

    private void validateFields() {
        if (phoneNumber.getText().isBlank()) {
            phoneNumber.setText(" ");
            phoneNumber.setText("");
        }
        if (password.getText().isBlank()) {
            password.setText(" ");
            password.setText("");
        }
        if (confirmedPassword.getText().isBlank()) {
            confirmedPassword.setText(" ");
            confirmedPassword.setText("");
        }
        if (displayName.getText().isBlank()) {
            displayName.setText(" ");
            displayName.setText("");
        }
        if (emailAddress.getText().isBlank()) {
            emailAddress.setText("");
            emailAddress.setText(" ");
        }
    }

}