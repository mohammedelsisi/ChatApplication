package JETS.service;

import JETS.db.dao.UserFriendDao;
import Models.ChatEntitiy;
import Models.FriendEntity;
import Services.ChatDao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDaoImp extends UnicastRemoteObject implements ChatDao {
//    public void setId(long id) {
//        this.id = id;
//    }


   // long id;
    protected final Connection connection;
   // private static final String INSERTChat = "INSERT INTO chat (chat_id,chat_name) VALUES (?,?)";
    private static final String INSERTChat = "INSERT INTO chat (chat_id,chat_name) VALUES (?,?)";
    private static final String INSERTParticipant = "INSERT INTO user_chat (user_phone_number,chat_chat_id) VALUES (?,?)";
    private static final String GET_LCHAT_ID = "Select MAX(chat_id) maxId from chat ";
    private static final String LOAD_PARTICIPANTS="select * from user where phone_number in (select user_phone_number from chatschema.user_chat where chat_chat_id=? and user_phone_number!=?)"+
            "and phone_number not IN (SELECT user_phone_number from user_friend " +
            " where friend_number=? and friendship_status= \"ACCEPTED\" union select friend_number from user_friend " +
            "  where user_phone_number=? and  friendship_status=\"ACCEPTED\") " ;
    public ChatDaoImp(Connection connection) throws RemoteException {
        this.connection = connection;
    //    this.id =getChatId();
    }


    @Override
    public synchronized ChatEntitiy initiateChat(ChatEntitiy dto) throws RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERTChat)) {
            //id++;
            //statement.setLong(1, id);
            int id=getChatId()+1;

            System.out.println("start");
            statement.setInt(1, id);
            statement.setString(2, dto.getChatName());
            statement.executeUpdate();
            System.out.println("end");

            dto.setId(id);
            dto.getParticipantsPhoneNumbers().forEach((e) -> {
                try {
                    insertParticipant(e, id);
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
    public void insertParticipant(String phoneNumber, long chatId) throws RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERTParticipant)) {

            statement.setString(1, phoneNumber);
            statement.setLong(2, chatId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private int getChatId() {
        try (PreparedStatement statement = this.connection.prepareStatement(GET_LCHAT_ID)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getInt("maxId");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
     public Map<String,FriendEntity> loadParticipants(int chatId,String myPhoneNumber){
         Map<String,FriendEntity> participants = new HashMap<>();
         try (PreparedStatement preparedStatement = connection.prepareStatement(LOAD_PARTICIPANTS)) {
             preparedStatement.setInt(1, chatId);
             preparedStatement.setString(2, myPhoneNumber);
             preparedStatement.setString(3, myPhoneNumber);
             preparedStatement.setString(4, myPhoneNumber);
             ResultSet rs = preparedStatement.executeQuery();

             while (rs.next()) {
                 FriendEntity friendEntity = new FriendEntity();
                 friendEntity=UserFriendDao.createUser(rs, friendEntity);
                 participants.put(friendEntity.getPhoneNumber(),friendEntity);
             }

         }catch (Exception e){
             e.printStackTrace();
         }
         return participants;
     }
}
