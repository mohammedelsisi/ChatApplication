package JETS.db.dao.Impl;

import JETS.db.dao.MessageDao;
import Models.MessageEntity;

import java.sql.*;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    private static final String INSERT = "INSERT INTO MESSAGE (body, chat_chat_id, user_phone_number) VALUES ( ?, ?, ?)";
    private static final String RETRIEVE_MESSAGES = "SELECT * FROM MESSAGE WHERE CHAT_CHAT_ID = ?";
    private final Connection CONNECTION;

    public MessageDaoImpl(Connection connection) {
        this.CONNECTION = connection;
    }

    @Override
    public MessageEntity insertMessage(MessageEntity message) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(INSERT)) {
            statement.setString(1, message.getMsgContent());
            statement.setLong(2, message.getChatEntitiy().getId());
            statement.setString(3, message.getSenderPhone());
            statement.executeUpdate();
            message.setId(getLastMessageId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return message;
    }

    private int getLastMessageId() {
        int id = -1;
        try (CallableStatement statement = CONNECTION.prepareCall("SELECT LAST_INSERT_ID();")) {
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                id = result.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override
    public List<MessageEntity> getChatMessages(int chatId) {
        return null;
    }
}
