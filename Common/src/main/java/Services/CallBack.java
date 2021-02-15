package Services;

import Models.FriendEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallBack extends Remote {

//    void recieve(String msg,int receiver, int sendId) throws RemoteException
//    void recieveGroup(String msg, int groupId, int sender) throws RemoteException
//    void resetFriendList() throws RemoteException;
//    void receiveFile(String ip, int receiver, int sender) throws RemoteException;
//
//    void notifyUsers(String msg) throws RemoteException;
    void notifyRejection(FriendEntity user) throws RemoteException;
    void notifyAcceptance(FriendEntity user) throws RemoteException;
    void notifyOnOff(FriendEntity user) throws RemoteException;
    void notifyRequest(FriendEntity user) throws RemoteException;
}