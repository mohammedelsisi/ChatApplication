package JETS.ui.controllers;

import JETS.net.ClientProxy;
import JETS.net.ServerOfflineHandler;
import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.appNotifications;
import Models.CurrentUser;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;

import java.rmi.RemoteException;

public class addFriendController {
    public JFXTextField phoneNumber;
    String myFriendPhoneNo;
    String myPhoneNumber = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();

    public void cancelingRequest(ActionEvent event) {
        phoneNumber.getScene().getWindow().hide();
    }

    public void addingFriend(ActionEvent event) {
        myFriendPhoneNo = phoneNumber.getText();
        if (myFriendPhoneNo.equals(myPhoneNumber)) {
            appNotifications.getInstance().errorBox("You Cannot add your Account", "INVALID TRIAL");

        } else if (myFriendPhoneNo.isBlank()) {
            appNotifications.getInstance().errorBox("Please Enter Mobile No.", "INVALID TRIAL");

        } else {

            ifexistAdd();
        }


    }

    public void ifexistAdd() {
        try {
            if (ModelsFactory.getInstance().getCurrentUser().getFriends() != null || FriendsManager.getInstance().getFriendName(myFriendPhoneNo) == null) {
                int x = ClientProxy.getInstance().sendRequest(myPhoneNumber, myFriendPhoneNo);
                if (x == 1) {
                    appNotifications.getInstance().okai(" Request Sent Successfully To your Friend ;)", "Request Sent");
                    phoneNumber.getScene().getWindow().hide();
                } else if (x == 0) {
                    appNotifications.getInstance().errorBox("There Is No User With This Credentials", "INVALID TRIAL");
                } else if (x == 2) {
                    appNotifications.getInstance().errorBox("You can't send request to this number", "INVALID TRIAL");
                }
            } else {
                appNotifications.getInstance().errorBox("This user is Already a friend.", "INVALID TRIAL");
            }
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Sorry, cannot continue your request. :(");
        }

    }


}
