package JETS.ui.helpers;

import Models.FriendEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Map;

public class FriendsManager {

    private static final FriendsManager INSTANCE = new FriendsManager();
    private Map<String, FriendEntity> friendList = ModelsFactory.getInstance().getCurrentUser().getFriends();

    public static FriendsManager getInstance() {
        return INSTANCE;
    }

    public String getFriendName(String phoneNumber) {
        return friendList.get(phoneNumber).getDisplayName();
    }

    public byte[] getFriendPhoto(String phoneNumber) {
        return friendList.get(phoneNumber).getUserPhoto();
    }

    public SimpleObjectProperty<byte[]> getFriendPhotoProperty(String phoneNumber) {
        return friendList.get(phoneNumber).userPhotoProperty();
    }

    public StringProperty getFriendNameProperty(String phoneNumber) {
        return friendList.get(phoneNumber).displayNameProperty();
    }

}

