package JETS.ui.helpers;

import JETS.ui.controllers.ChatController;
import Models.MessageEntity;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {
    private static final ChatManager CHAT_MANAGER = new ChatManager();
    private final Map<Integer, SimpleObjectProperty<MessageEntity>> RESPONSES;

    private ChatManager() {
        RESPONSES = new ConcurrentHashMap<>();
    }

    public static ChatManager getInstance() {
        return CHAT_MANAGER;
    }

    public SimpleObjectProperty<MessageEntity> createNewChatResponse(int chatID) {
        SimpleObjectProperty<MessageEntity> response = new SimpleObjectProperty<>();
        RESPONSES.put(chatID, response);
        return response;
    }

    public void receiveResponse(MessageEntity message) {
        Platform.runLater(() -> {
            int chatID = message.getChatEntitiy().getId();
            if (RESPONSES.containsKey(chatID)) {
                System.out.println(chatID);
                RESPONSES.get(chatID).set(message);
            } else {
                SimpleObjectProperty<MessageEntity> simpleMsg = createNewChatResponse(chatID);
                simpleMsg.set(message);

                ChatController chat = StageCoordinator.getInstance().getScenes().get("Chat").getLoader().getController();
                chat.createChatLayout(simpleMsg);
            }
        });
    }

}
