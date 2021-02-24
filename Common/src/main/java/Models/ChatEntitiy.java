package Models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChatEntitiy implements Serializable {
    private int id;
    private List<String> participantsPhoneNumbers;
    private String chatName;

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public ChatEntitiy() {

    }

    public ChatEntitiy(int id, List<String> phoneNumber,String chatName) {
        this.id = id;
        this.participantsPhoneNumbers = phoneNumber;
        this.chatName=chatName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getParticipantsPhoneNumbers() {
        return participantsPhoneNumbers;
    }

    public void setParticipantsPhoneNumbers(List<String> participantsPhoneNumbers) {
        this.participantsPhoneNumbers = participantsPhoneNumbers;
    }
}
