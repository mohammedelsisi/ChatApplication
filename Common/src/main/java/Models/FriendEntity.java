package Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FriendEntity implements Serializable {

    private transient SimpleObjectProperty<byte[]> userPhoto = new SimpleObjectProperty<>();
    private transient SimpleStringProperty displayName = new SimpleStringProperty();
    private String phoneNumber;
    private String bio;
    private String status;

    public FriendEntity(String phoneNumber, String displayName, String bio, String status) {
        this.phoneNumber = phoneNumber;
        setDisplayName(displayName);
        this.bio = bio;
        this.status = status;
    }

    public FriendEntity(String displayName) {
        setDisplayName(displayName);
    }

    public FriendEntity() {
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public SimpleStringProperty displayNameProperty() {
        return displayName;
    }

    public byte[] getUserPhoto() {
        return userPhoto.get();
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto.set(userPhoto);
    }

    public SimpleObjectProperty<byte[]> userPhotoProperty() {
        return userPhoto;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String statusVal) {
        this.status = statusVal;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(phoneNumber.substring(1));
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null) {
           isEqual = phoneNumber.equals(((FriendEntity) obj).phoneNumber);
        }
        return isEqual;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(userPhoto.get());
        s.writeUTF(displayName.get());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        userPhoto = new SimpleObjectProperty<>((byte[]) s.readObject());
        displayName = new SimpleStringProperty(s.readUTF());
    }
}

