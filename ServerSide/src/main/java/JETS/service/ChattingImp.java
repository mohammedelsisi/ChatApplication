package JETS.service;

import JETS.ServerMain;
import JETS.db.dao.UserDao;
import JETS.db.dao.UserFriendDao;
import Models.ChatEntitiy;
import Models.FriendEntity;
import Services.Chatting;
import Services.ClientServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ChattingImp extends UnicastRemoteObject implements Chatting {
    private ObservableMap<String, ClientServices> clients = ConnectionServiceFactory.getConnectionService().getConnectedClients();
    UserFriendDao friendImp;
    UserDao userDao;
    public ChattingImp( UserFriendDao friendDao, UserDao userDao) throws RemoteException {
         this.friendImp = friendDao;
        this.userDao=userDao;
    }

    //Exit

    //friendImp
    public List<FriendEntity> getFriends(String phoneNumber){
        try {
            return friendImp.getFriendList(phoneNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public int sendRequest(String senderPhoneNumber, String receiverPhoneNumber) throws RemoteException{
          //update DB
          //notify user if he is online
       int result= friendImp.SearchbyPhoneno(senderPhoneNumber,receiverPhoneNumber);
       if(result==1){
           if(clients.containsKey(receiverPhoneNumber)) {
               clients.get(receiverPhoneNumber).notifyRequest(friendImp.findFriendByPhoneNumber(senderPhoneNumber));
           }
       }
       return result;
    }
    public void acceptRequest(String myphoneNumber,String acceptedphoneNumber)  {
        try {
            friendImp.acceptRequest(myphoneNumber, acceptedphoneNumber);

            if(clients.containsKey(acceptedphoneNumber)) {

                clients.get(acceptedphoneNumber).notifyAcceptance(friendImp.findFriendByPhoneNumber(myphoneNumber));
            }
        }catch (RemoteException e){
        e.printStackTrace();
        }
    }
    public void refuseRequest(String myphoneNumber,String rejectedphoneNumber)  {
        try {
            friendImp.deleteRequest(myphoneNumber,rejectedphoneNumber);
            if(clients.containsKey(rejectedphoneNumber)) {
                clients.get(rejectedphoneNumber).notifyRejection(friendImp.findFriendByPhoneNumber(myphoneNumber));
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    public List<FriendEntity> getFriendRequests(String myPhoneNumber){
        try {
        return  friendImp.getFriendRequests(myPhoneNumber);
    }catch (Exception e){
        e.printStackTrace();
    }
        return null;
    }
    //callback FriendDao
    public void tellStatus(String phoneNumber,String status) throws RemoteException{
        userDao.updateUserStatus(phoneNumber,status);
        FriendEntity user=friendImp.findFriendByPhoneNumber(phoneNumber);
        List<FriendEntity> arr = new ArrayList<FriendEntity>();
        try {
            arr = friendImp.getFriendList(phoneNumber);
        }catch (Exception e){
            e.printStackTrace();
        }

        for(int i=0; i<arr.size(); i++){

            if(clients.containsKey(arr.get(i).getPhoneNumber())){
                //clients.get(arr.get(i).getId()).resetFriendList();

                clients.get(arr.get(i).getPhoneNumber()).notifyOnOff(user);

            }
        }
    }







}
