package Services;

import Models.FriendEntity;
import Models.MessageEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientServices extends Remote {
    String getPhoneNumber() throws RemoteException;
    void receive(MessageEntity messageEntity) throws RemoteException;
    void ReceiveAnnounc(String s ) throws RemoteException;
    void notifyRejection(FriendEntity user) throws RemoteException;
    void notifyAcceptance(FriendEntity user) throws RemoteException;
    void notifyOnOff(FriendEntity user) throws RemoteException;
    void notifyRequest(FriendEntity user) throws RemoteException;
}
