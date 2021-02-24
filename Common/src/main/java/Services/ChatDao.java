package Services;

import Models.ChatEntitiy;
import Models.FriendEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ChatDao extends Remote {
    ChatEntitiy initiateChat(ChatEntitiy chatEntitiy) throws RemoteException;
   void insertParticipant(String phoneNumber,long chatId) throws RemoteException;
   Map<String,FriendEntity> loadParticipants(int chatId, String myPhoneNumber) throws RemoteException;
}
