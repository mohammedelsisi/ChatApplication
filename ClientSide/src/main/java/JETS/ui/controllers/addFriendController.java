package JETS.ui.controllers;

import JETS.net.ClientProxy;
import JETS.ui.helpers.ModelsFactory;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.rmi.RemoteException;

public class addFriendController {
    public JFXPasswordField phoneNumber;
    String myFriendPhoneNo = phoneNumber.getText();
    String myPhoneNumber = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();

    public void cancelingRequest(ActionEvent event) {
        phoneNumber.getScene().getWindow().hide();
    }

    public void addingFriend(ActionEvent event) {

        if(myFriendPhoneNo.equals(myPhoneNumber)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INVALID Trial");
            alert.setHeaderText(null);
            alert.setContentText("You Cannot add your Account");
            alert.showAndWait();
        }
        else if(myFriendPhoneNo.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INVALID Trial");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Mobile No.");
            alert.showAndWait();
        }else {

            ifexistAdd();
        }


    }
      public void ifexistAdd (){
          try {
              int x=0;
               x = ClientProxy.getInstance().sendRequest(myPhoneNumber, myFriendPhoneNo);
              if (x==1){
                  Alert alert = new Alert(Alert.AlertType.NONE);
                  alert.setTitle("Request Sent");
                  alert.setHeaderText(null);
                  alert.setContentText(" Request Sent Successfully To your Friend ;) ");
                  alert.showAndWait();
              }else if(x==0){
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("INVALID Trial");
                  alert.setHeaderText(null);
                  alert.setContentText("There Is No User Exit With This Credentials");
                  alert.showAndWait();
              }
          } catch (RemoteException e) {
              e.printStackTrace();
          }

      }



}
