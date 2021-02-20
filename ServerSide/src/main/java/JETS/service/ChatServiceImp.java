package JETS.service;

import JETS.db.DataSourceFactory;
import JETS.db.dao.Impl.FileDaoImpl;
import JETS.db.dao.Impl.MessageDaoImpl;
import Models.MessageEntity;
import Services.ChatServiceInt;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class ChatServiceImp extends UnicastRemoteObject implements ChatServiceInt {
    //    private final Map<Long, ChatEntitiy> chatsMap=new ConcurrentHashMap<>();
    public ChatServiceImp() throws RemoteException {
    }

    @Override
    public void sendMessage(MessageEntity messageEntity) throws RemoteException {
        try {
            new MessageDaoImpl(DataSourceFactory.getConnection()).insertMessage(messageEntity);
            if(messageEntity.getFile() != null) {
                new FileDaoImpl(DataSourceFactory.getConnection()).insertFile(messageEntity.getFile(), messageEntity.getId());
                messageEntity.getFile().setData(null);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageEntity.getChatEntitiy().getParticipantsPhoneNumbers().stream()
                .filter((e) -> !e.equals(messageEntity.getSenderPhone()))
                .filter((e) -> ConnectionServiceFactory.getConnectionService().isConnected(e))
                .map((e) -> ConnectionServiceFactory.getConnectionService().getClientService(e))
                .forEach((e) -> {
                    try {
                        e.receive(messageEntity);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                });
    }
}
