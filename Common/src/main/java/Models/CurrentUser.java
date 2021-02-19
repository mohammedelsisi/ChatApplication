package Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CurrentUser implements Serializable {

    transient private StringProperty status = new SimpleStringProperty();
    transient private StringProperty displayNameProperty = new SimpleStringProperty();
    transient private SimpleObjectProperty<byte[]> userPhoto = new SimpleObjectProperty<>();
    private String phoneNumber;
    private String password;
    private String email;
    private String gender;



    private String DOB;
    private String country;
    private String Bio;
    private Map<String, FriendEntity> friends = new HashMap<>();

    public StringProperty displayNamePropertyProperty() {
        return displayNameProperty;
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

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getDisplayName() {
        return displayNameProperty.get();
    }

    public void setDisplayName(String displayNameProperty) {
        this.displayNameProperty.set(displayNameProperty);
    }

    public StringProperty displayNameProperty() {
        return displayNameProperty;
    }

    public Map<String, FriendEntity> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, FriendEntity> friends) {
        this.friends = friends;
    }
    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(displayNameProperty.get());
        s.writeObject(userPhoto.get());
        s.writeUTF(status.get());

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        displayNameProperty = new SimpleStringProperty(s.readUTF());
        userPhoto = new SimpleObjectProperty<>((byte[]) s.readObject());
        status = new SimpleStringProperty(s.readUTF());
    }

}
