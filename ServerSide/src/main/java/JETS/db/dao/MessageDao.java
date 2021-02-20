package JETS.db.dao;

import Models.MessageEntity;

import java.rmi.Remote;
import java.util.List;

public interface MessageDao {
    MessageEntity insertMessage(MessageEntity message);
    List<MessageEntity> getChatMessages(int chatId);
}
