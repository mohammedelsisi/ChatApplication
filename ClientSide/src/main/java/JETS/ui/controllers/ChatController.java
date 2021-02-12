package JETS.ui.controllers;


import JETS.ClientMain;
import Models.CurrentUser;
import Services.UserFriendDaoInterface;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Group;
import javafx.scene.control.*;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.ButtonBar.ButtonData.OTHER;



public class ChatController implements Initializable {
    public JFXTextArea messageField;
    private Text textHolder = new Text();
    private double oldMessageFieldHigh;
    List<String> list;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
       String phone= ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();

//        list.add(phone);
//        list.add("+201012123112");
//        list.add("+201012123113");


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

//                    chatEntitiy.setParticipantsPhoneNumbers();
//                   .initiateChat(chatEntitiy);
                    messageField.setText("");
                }
//                sPane.vvalueProperty().bind(vBox.heightProperty());


            } else {
                messageField.setText("");
            }
        }

    public void requestFriend() throws SQLException, RemoteException {

        Dialog dialog = new Dialog();
      //  dialog.setTitle();
        dialog.setResizable(false);

        Label label1 = new Label("Enter Your Friend's Phone Number: ");
        TextField text1 = new TextField();
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Add Friend", OTHER);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.getDialogPane().getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> resultOfAddFriend = dialog.showAndWait();
        String myFriendphoneNo = text1.getText();

        if (resultOfAddFriend.get()==buttonTypeOk)
        {
            AddFriend(myFriendphoneNo);
        }




    }

    public static int AddFriend(String myfriendNum) throws SQLException, RemoteException {
        //String myphoneNumber = new CurrentUser().getPhoneNumber();
        String myphoneNumber = ("+201122344444");
        String myfriendPhoneNo = myfriendNum;

      int x =  ClientMain.userFriendDaoInterface.SearchbyPhoneno(myphoneNumber,myfriendPhoneNo);
     System.out.println(myphoneNumber);
     System.out.println(myfriendPhoneNo);
      System.out.println(x);
      return x;
    }

}
