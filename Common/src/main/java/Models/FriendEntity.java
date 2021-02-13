package Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class FriendEntity implements Serializable {
    transient private final StringProperty status = new SimpleStringProperty();

    public byte[] getUserPhoto() {
        return userPhoto.get();
    }

    public SimpleObjectProperty<byte[]> userPhotoProperty() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto.set(userPhoto);
    }

    transient private final SimpleObjectProperty<byte []> userPhoto = new SimpleObjectProperty<>();
    private String phoneNumber;
    private String displayName;
    private String Bio;

    public FriendEntity(String phoneNumber, String displayName, String bio, String status) {
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        Bio = bio;
        this.status.set(status);
    }
    public FriendEntity(String displayName){
        this.displayName = displayName;
    }

    public FriendEntity() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}
