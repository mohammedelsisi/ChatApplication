package JETS.ui.controllers;

import JETS.ui.helpers.ModelsFactory;
import Models.ChatEntitiy;
import Models.CurrentUser;
import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

import static JETS.ui.controllers.SignUpController.*;

public class ChatController implements Initializable {
    public JFXTextArea messageField;
    public JFXButton btnChangePassword;

    private final Text textHolder = new Text();
    private double oldMessageFieldHigh;
    List<String> list;

    //REGISTRATION VALIDATION ATTRIBUTES
    boolean validPhoneNumber = false;
    boolean validPassword = false;
    boolean ValidDisplayName = false;
    boolean validEmailAddress = false;
    JFXDialog jfxDialog = new JFXDialog();
    public CurrentUser user = ModelsFactory.getInstance().getCurrentUser();
    SignUpController signUpController = new SignUpController();

    @FXML
    private Tab MIuserInfo;

    @FXML
    private HBox HBDisplayName;

    @FXML
    private JFXTextField tFDisplayName;

    @FXML
    private JFXTextField tFEmailAddress;

    @FXML
    private JFXComboBox cbGender;

    @FXML
    private DatePicker tfdatePicker;

    @FXML
    private JFXTextArea TABio;

    @FXML
    private JFXButton btnUpdateInfo11;

    @FXML
    private VBox messagesContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tFEmailAddress.focusedProperty().addListener((observableValue, aBoolean, newValue) -> {
            user.setEmail(tFEmailAddress.getText());
        });

       //common validation
        //only numbers allowed validator
        NumberValidator onlyNumbers = new NumberValidator();
        onlyNumbers.setMessage("only numbers are allowed");

        //Required field validator
         RequiredFieldValidator requiredInputField = new RequiredFieldValidator();
        requiredInputField.setMessage("Can not be Empty");

        String phone = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
//        list.add(phone);
//        list.add("+201012123112");
//        list.add("+201012123113");
//
//        textHolder.textProperty().bind(messageField.textProperty());
//        textHolder.setWrappingWidth(600);
//        textHolder.layoutBoundsProperty().addListener((observableValue, oldValue, newValue) -> {
//            if (oldMessageFieldHigh != newValue.getHeight() && newValue.getHeight() < 100) {
//                oldMessageFieldHigh = newValue.getHeight();
////                messageField.setPrefHeight(Math.max(oldMessageFieldHigh + 15, 50));
//            }
//        });


        //setting the date picker initial value
        LocalDate minDate = LocalDate.now().minusYears(80);
        LocalDate maxDate = LocalDate.now().minusYears(18);
        tfdatePicker.setValue(maxDate);

        cbGender.getItems().add("Male");
        cbGender.getItems().add("Female");

        //Email Address validation(handling email validation by calling the method from the signup controller class)
        boolean realEmail = isValidEmail(tFEmailAddress.getText());
        tFEmailAddress.textProperty().addListener((arg0, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                showError(tFEmailAddress,"Not a valid Email Address");
               // set the check value equal to false
            } else if (newValue.isEmpty()){
                tFEmailAddress.getValidators().add(requiredInputField);
                tFEmailAddress.validate();
            } else {
                passValidation(tFEmailAddress);
                //pass the email value to the property.
            }
        });

    }

    public void sendMessage(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!messageField.getText().isBlank()) {
                if (keyEvent.isAltDown()) {
                    messageField.appendText("\n");
                } else {
                    String text = messageField.getText().trim();
                    /* has old chat ID */
                    ChatEntitiy chatEntitiy = new ChatEntitiy();
//                    chatEntitiy.setParticipantsPhoneNumbers();
//                   .initiateChat(chatEntitiy);
                    messageField.setText("");
                }
//                sPane.vvalueProperty().bind(vBox.heightProperty());

            } else {
                messageField.setText("");
            }
        }
    }


    //Retrieving the user data from the database and displaying it to in the update info GUI.
    public void showUserData(Event event) {
        tFDisplayName.setText(ModelsFactory.getInstance().getCurrentUser().getDisplayName());
        tFEmailAddress.setText(ModelsFactory.getInstance().getCurrentUser().getEmail());
        cbGender.getSelectionModel().select(user.getGender());
        TABio.setText(ModelsFactory.getInstance().getCurrentUser().getBio());
    }


    //saving the user information to the current user object and then passing it to the database
    public void SaveUpdateChanges(ActionEvent event) {
        System.out.println(tFDisplayName.getText());
       user.setBio(TABio.getText());
       user.setEmail(tFEmailAddress.getText());
       user.setGender(cbGender.getSelectionModel().getSelectedItem().toString());
        user.setAge(Period.between(tfdatePicker.getValue(),LocalDate.now()).getYears());
       // user.setDisplayName(tFDisplayName.getText());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Changes Saved ");
        alert.setHeaderText(null);
        alert.setContentText("Information Updated Successfully!");
        alert.showAndWait();

    }

    public void handleChangePassword(ActionEvent event) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ChangePassword.fxml"));
                DialogPane passwordDialogPane = fxmlLoader.load();
                Dialog<ButtonType> passwordDialog = new Dialog<>();
                passwordDialog.setDialogPane(passwordDialogPane);
               Optional<ButtonType> userChoice= passwordDialog.showAndWait();

               if(userChoice.get() == ButtonType.APPLY){
                   Alert alert = new Alert(Alert.AlertType.INFORMATION);
                   alert.setTitle("Change Password ");
                   alert.setHeaderText(null);
                   alert.setContentText("Password Updated Successfully!");
                   alert.showAndWait();
               }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
    }

    public void switchToSecondary(KeyEvent event) {
        //this method was added since i found it in the fxml and was showing and error
        //has to be checked with my colleagues to be removed if possible
    }

}