package Services;

import Models.MessageEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientServices extends Remote {
    String getPhoneNumber() throws RemoteException;
    void receive(MessageEntity messageEntity) throws RemoteException;
}
