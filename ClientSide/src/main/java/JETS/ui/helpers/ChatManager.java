package JETS.ui.helpers;

import JETS.SavingChat.MessageType;
import JETS.ui.controllers.ChatController;
import Models.MessageEntity;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
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
        int chatID = message.getChatEntitiy().getId();
        ChatController chat = StageCoordinator.getInstance().getScenes().get("Chat").getLoader().getController();
        if(!chat.getChatHistory().containsKey(chatID)) {
            chat.getChatHistory().put(chatID,new ArrayList<MessageType>());
        }
        chat.getChatHistory().get(chatID).add(new MessageType(message.getSenderPhone(), message.getMsgContent(), "right"));

        Platform.runLater(() -> {
            if (RESPONSES.containsKey(chatID)) {
                System.out.println(chatID);
                RESPONSES.get(chatID).set(message);
            } else {
                SimpleObjectProperty<MessageEntity> simpleMsg = createNewChatResponse(chatID);
                simpleMsg.set(message);

                chat.createChatLayout(simpleMsg);
            }
        });
    }

}
