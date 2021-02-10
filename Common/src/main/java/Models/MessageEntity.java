package Models;

import java.io.Serializable;

public class MessageEntity implements Serializable {
    private ChatEntitiy chatEntitiy;
    private String msgContent;
    private String senderPhone;

    public MessageEntity(ChatEntitiy chatEntitiy, String msgContent, String senderPhone) {
        this.chatEntitiy = chatEntitiy;
        this.msgContent = msgContent;
        this.senderPhone = senderPhone;
    }

    public MessageEntity() {
    }

    public ChatEntitiy getChatEntitiy() {
        return chatEntitiy;
    }

    public void setChatEntitiy(ChatEntitiy chatEntitiy) {
        this.chatEntitiy = chatEntitiy;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }
}
