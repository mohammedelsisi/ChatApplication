package JETS.ui.controllers;


import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import JETS.ui.helpers.ModelsFactory;
import Models.ChatEntitiy;
import Models.FriendEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;





public class ChatController implements Initializable {
    public JFXTextArea messageField;
    private Text textHolder = new Text();
    private double oldMessageFieldHigh;
    List<String> list;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       String phone= ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
        list.add(phone);
        list.add("+201012123112");
        list.add("+201012123113");

        textHolder.textProperty().bind(messageField.textProperty());
        textHolder.setWrappingWidth(600);
        textHolder.layoutBoundsProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldMessageFieldHigh != newValue.getHeight() && newValue.getHeight() < 100) {
                oldMessageFieldHigh = newValue.getHeight();
                messageField.setPrefHeight(Math.max(oldMessageFieldHigh + 15, 50));
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
   //                 chatEntitiy.setParticipantsPhoneNumbers();
//                   .initiateChat(chatEntitiy);
                    messageField.setText("");
                }
                //sPane.vvalueProperty().bind(vBox.heightProperty());

            } else {
                messageField.setText("");
            }
        }
    }

    public void requestFriend(){
        Alert a1 = new Alert(Alert.AlertType.NONE,
                "Here is the info about Notepad", ButtonType.OK);
        a1.setTitle("About Us");
        
        a1.show();
    }
}
