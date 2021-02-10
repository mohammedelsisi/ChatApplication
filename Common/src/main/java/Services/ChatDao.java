package Services;

import Models.ChatEntitiy;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatDao extends Remote {
    ChatEntitiy initiateChat(ChatEntitiy chatEntitiy) throws RemoteException;
   void insertParticipant(String phoneNumber,long chatId) throws RemoteException;

}
