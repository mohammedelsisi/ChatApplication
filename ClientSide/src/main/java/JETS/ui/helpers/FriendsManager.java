package JETS.ui.helpers;

import Models.FriendEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Map;

public class FriendsManager {

    private static final FriendsManager INSTANCE = new FriendsManager();
    private Map<String, FriendEntity> friendList = ModelsFactory.getInstance().getCurrentUser().getFriends();
    private Map<String, FriendEntity> participantsInChatList = ModelsFactory.getInstance().getCurrentUser().getParticipantsInGroup();
    public static FriendsManager getInstance() {
        return INSTANCE;
    }

    public String getFriendName(String phoneNumber) {
        //System.out.println(phoneNumber);
        if(ModelsFactory.getInstance().getCurrentUser().getParticipantsInGroup().containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).getDisplayName();
        }else {
            return friendList.get(phoneNumber).getDisplayName();
        }
    }

    public byte[] getFriendPhoto(String phoneNumber) {
        if(participantsInChatList.containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).getUserPhoto();
        }
        return friendList.get(phoneNumber).getUserPhoto();
    }

    public SimpleObjectProperty<byte[]> getFriendPhotoProperty(String phoneNumber) {
        if(participantsInChatList.containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).userPhotoProperty();
        }
        return friendList.get(phoneNumber).userPhotoProperty();
    }

    public StringProperty getFriendNameProperty(String phoneNumber) {
        if(participantsInChatList.containsKey(phoneNumber)){
            return participantsInChatList.get(phoneNumber).displayNameProperty();
        }
        return friendList.get(phoneNumber).displayNameProperty();
    }

}

