package Services;

import Models.ChatEntitiy;
import Models.MessageEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServiceInt extends Remote {
    void sendMessage(MessageEntity messageEntity) throws RemoteException;
}
