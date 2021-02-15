package JETS.ClientServices;

import JETS.ui.helpers.ChatManager;
import JETS.ui.helpers.ModelsFactory;
import Models.MessageEntity;
import Services.ClientServices;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientServicesImp extends UnicastRemoteObject implements ClientServices {

    protected ClientServicesImp() throws RemoteException {
    }

    @Override
    public String getPhoneNumber() throws RemoteException {
       return ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
    }

    @Override
    public void receive(MessageEntity messageEntity) throws RemoteException {
        ChatManager.getInstance().receiveResponse(messageEntity);

    }
}
