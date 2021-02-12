package JETS.service;

import Models.MessageEntity;
import Services.ChatServiceInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImp extends UnicastRemoteObject implements ChatServiceInt {
    //    private final Map<Long, ChatEntitiy> chatsMap=new ConcurrentHashMap<>();
    public ChatServiceImp() throws RemoteException {
    }

    @Override
    public void sendMessage(MessageEntity messageEntity) throws RemoteException {
//        if (!chatsMap.containsKey(messageEntity.getChatEntitiy().getId())) {
//            ChatEntitiy chatEntitiy = messageEntity.getChatEntitiy();
//            chatsMap.put(chatEntitiy.getId(), chatEntitiy);
//        }

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
