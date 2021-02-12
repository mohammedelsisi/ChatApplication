package JETS.ClientServices;

import JETS.ui.helpers.ModelsFactory;
import Models.MessageEntity;
import Services.ClientServices;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientServicesImp extends UnicastRemoteObject implements ClientServices {

    protected ClientServicesImp() throws RemoteException {
    }

    @Override
    public void receive(MessageEntity messageEntity) throws RemoteException {

    }

    @Override
    public String getPhoneNumber() throws RemoteException {
       return ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
    }

}
