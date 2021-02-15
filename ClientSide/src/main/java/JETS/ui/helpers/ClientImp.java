package JETS.ui.helpers;

import JETS.ui.controllers.ChatController;
import Models.FriendEntity;
import Services.CallBack;
import javafx.application.Platform;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImp extends UnicastRemoteObject implements CallBack, Serializable {
    transient ChatController chatController;
    public ClientImp(ChatController chatController) throws RemoteException{
        this.chatController=chatController;
    }
    public ClientImp() throws RemoteException{

    }
    @Override
    public void notifyRejection(FriendEntity user) throws RemoteException {
        System.out.println(user.getDisplayName()+" rejected your request");
    }

    @Override
    public void notifyAcceptance(FriendEntity user) throws RemoteException {
        System.out.println(user.getDisplayName()+" accepted your request");
        ChatController.friendsList.add(user);
    }

    @Override
    public void notifyOnOff(FriendEntity user) throws RemoteException {
        //Show Notification
        Runnable r=new Runnable() {
            @Override
            public void run() {
                ChatController.friendsList.remove(user);
                ChatController.friendsList.add(user);
            }
        };
        Platform.runLater(r);
    }

    @Override
    public void notifyRequest(FriendEntity user) throws RemoteException {
         ChatController.requestLists.add(user);
    }
}
