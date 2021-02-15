package Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;

public class FriendEntity implements Serializable {

    private transient final StringProperty status = new SimpleStringProperty();
    private transient SimpleObjectProperty<byte[]> userPhoto = new SimpleObjectProperty<>();
    private transient SimpleStringProperty displayName = new SimpleStringProperty();
    private String phoneNumber;
    private String bio;

    public FriendEntity(String phoneNumber, String displayName, String bio, String status) {
        this.phoneNumber = phoneNumber;
        setDisplayName(displayName);
        this.bio = bio;
        setStatus(status);
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
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
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

