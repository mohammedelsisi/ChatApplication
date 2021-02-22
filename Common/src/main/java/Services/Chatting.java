package Services;

import Models.FriendEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface Chatting extends Remote {

    public List<FriendEntity> getFriends(String PhoneNumber)throws RemoteException;
    public List<FriendEntity> getFriendRequests(String myPhoneNumber) throws RemoteException;
    public int sendRequest(String senderPhoneNumber, String receiverPhoneNumber)throws RemoteException;
    public void acceptRequest(String myphoneNumber,String acceptedphoneNumber)throws RemoteException;
    public void refuseRequest(String myphoneNumber,String rejectedphoneNumber) throws RemoteException;
    public void tellStatus(String phoneNumber,String status)  throws RemoteException;



    }
