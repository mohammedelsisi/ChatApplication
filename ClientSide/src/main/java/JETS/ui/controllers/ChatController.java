package JETS.ui.controllers;

import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.ChatEntitiy;
import Models.FriendEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public JFXTextArea messageField;
    public VBox chatsVbox;
    private Text textHolder = new Text();
    private double oldMessageFieldHigh;
    List<String> list = new ArrayList<>();
    @FXML
    private Label receiverName;
    @FXML
    private TabPane tabPane;
    ChatEntitiy chatEntitiy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        list.add(phone);
//        list.add("+201012123112");
//        list.add("+201012123113");
        FriendEntity freind1 = new FriendEntity("+201012123112","ahmedsaasd","asdasd","Available");
        try {
            freind1.setUserPhoto(new BufferedInputStream( new FileInputStream("RegPPic.png")).readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        FriendsManager.instance.friendList.put(freind1.getPhoneNumber(),freind1);
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
                  if (chatEntitiy.getId()==0){

                  }
                    messageField.setText("");
                }
//                sPane.vvalueProperty().bind(vBox.heightProperty());

            } else {
                messageField.setText("");
            }
        }
    }

    public void startChatAction(ActionEvent actionEvent) {
//        String phone= ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
        list.add("+201027579113");
        list.add("+201012123112");
        ChatEntitiy createdEntity = new ChatEntitiy(0,list,null);
       HBox hBox= StageCoordinator.getInstance().createSingleChatLayout(createdEntity);
       hBox.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
        receiverName.setText( FriendsManager.instance.getFriendName(   chatEntitiy.getParticipantsPhoneNumbers().get(1)));
        chatEntitiy= createdEntity;
       });

       chatsVbox.getChildren().add(hBox);
        tabPane.getSelectionModel().selectPrevious();
    }
}
