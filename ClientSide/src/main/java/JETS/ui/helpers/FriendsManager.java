package JETS.ui.helpers;

import Models.FriendEntity;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class FriendsManager {

  public   static FriendsManager instance = new FriendsManager();
  public   Map<String, FriendEntity> friendList = new HashMap<>();

    public  String getFriendName (String phoneNumber){
        return friendList.get(phoneNumber).getDisplayName();
    }

    public byte[] getFriendPhoto (String phoneNumber){
        return friendList.get(phoneNumber).getUserPhoto();
    }

}

