package JETS.service;

import Models.ChatEntitiy;
import Services.ChatDao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDaoImp extends UnicastRemoteObject implements ChatDao {
    public void setId(long id) {
        this.id = id;
    }

    long id;
    protected final Connection connection;
    private static final String INSERTChat = "INSERT INTO chat (chat_id,chat_name) VALUES (?,?)";
    private static final String INSERTParticipant = "INSERT INTO user_chat (user_phone_number,chat_chat_id) VALUES (?,?,?)";

    public ChatDaoImp( Connection connection, long x) throws RemoteException {
        this.connection = connection;
        this.id=x;
    }


    @Override
    public ChatEntitiy initiateChat(ChatEntitiy dto) throws RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERTChat);) {

            statement.setLong(1, id);
            statement.setString(2, dto.getChatName());
            dto.getParticipantsPhoneNumbers().forEach((e)->{
                try {
                    insertParticipant(e,id);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dto;
    }

    @Override
    public void insertParticipant(String phoneNumber,long chatId) throws RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERTParticipant)) {

            statement.setString(1, phoneNumber);
            statement.setLong(2, chatId);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
