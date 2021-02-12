package JETS.ui.helpers;

import Models.MessageEntity;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {
    private static final ChatManager CHAT_MANAGER = new ChatManager();
    private final Map<Long, SimpleObjectProperty<MessageEntity>> RESPONSES;

    private ChatManager() {
        RESPONSES = new ConcurrentHashMap<>();
    }

    public static ChatManager getInstance() {
        return CHAT_MANAGER;
    }

    public SimpleObjectProperty<MessageEntity> createNewChatResponse(long chatID) {
        SimpleObjectProperty<MessageEntity> response = new SimpleObjectProperty<>();
        RESPONSES.put(chatID, response);
        return response;
    }

    public void receiveResponse(MessageEntity message) {
        Long chatID = message.getChatEntitiy().getId();
        if (RESPONSES.containsKey(chatID)) {
            RESPONSES.get(chatID).set(message);
        } else {
            StageCoordinator.getInstance().createChatLayout(message.getChatEntitiy());
            createNewChatResponse(chatID);
        }
    }
}
