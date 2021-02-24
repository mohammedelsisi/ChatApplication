package JETS.SavingChat;

public class MessageType {
    private String senderPhone;
    private String msg;
    private String direction;

    public MessageType(String senderName, String msg, String direction) {
        this.senderPhone = senderName;
        this.msg = msg;
        this.direction = direction;
    }
    public MessageType() {

    }
    public String getSenderPhone() {
        return senderPhone;
    }

    public String getMsg() {
        return msg;
    }

    public String getDirection() {
        return direction;
    }

    public void setSenderPhone(String sender) {
        this.senderPhone = senderPhone;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
