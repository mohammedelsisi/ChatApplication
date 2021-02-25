package JETS.ui.helpers;

import Models.FriendEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Map;

public class FriendsManager {

    private static final FriendsManager INSTANCE = new FriendsManager();

    private Map<String, FriendEntity> participantsInChatList = ModelsFactory.getInstance().getCurrentUser().getParticipantsInGroup();
    public static FriendsManager getInstance() {
        return INSTANCE;
    }

    private  Map<String, FriendEntity> getFriends (){
      return   ModelsFactory.getInstance().getCurrentUser().getFriends();
    }

    public String getFriendName(String phoneNumber) {
        //System.out.println(phoneNumber);
        if(ModelsFactory.getInstance().getCurrentUser().getParticipantsInGroup().containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).getDisplayName();
        }else {
            return getFriends().get(phoneNumber).getDisplayName();
        }
    }

    public byte[] getFriendPhoto(String phoneNumber) {
        if(participantsInChatList.containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).getUserPhoto();
        }
        return getFriends().get(phoneNumber).getUserPhoto();
    }

    public SimpleObjectProperty<byte[]> getFriendPhotoProperty(String phoneNumber) {
        if(participantsInChatList.containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).userPhotoProperty();
        }
        return getFriends().get(phoneNumber).userPhotoProperty();
    }

    public StringProperty getFriendNameProperty(String phoneNumber) {
        if(participantsInChatList.containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).displayNameProperty();
        }
        return getFriends().get(phoneNumber).displayNameProperty();
    }

}

