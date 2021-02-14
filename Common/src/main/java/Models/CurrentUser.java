package Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentUser implements Serializable {

    transient private StringProperty displayNameProperty = new SimpleStringProperty();

    public StringProperty displayNamePropertyProperty() {
        return displayNameProperty;
    }

    public void setDisplayNameProperty(String displayNameProperty) {
        this.displayNameProperty.set(displayNameProperty);
    }

    public byte[] getUserPhoto() {
        return userPhoto.get();
    }

    public SimpleObjectProperty<byte[]> userPhotoProperty() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto.set(userPhoto);
    }

    transient private SimpleObjectProperty<byte[]> userPhoto = new SimpleObjectProperty<>();
    transient private StringProperty status = new SimpleStringProperty();
    private String phoneNumber;
    private String password;
    private String email;
    private String gender;
    private int age;
    private String country;
    private String Bio;
    private String displayName;
    private Map<String, FriendEntity> friends = new HashMap<>();


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


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getDisplayNameProperty() {
        return displayNameProperty.get();
    }

    public StringProperty displayNameProperty() {
        return displayNameProperty;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayNameProperty.set(displayName);
        this.displayName = displayName;
    }

    public Map<String, FriendEntity> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, FriendEntity> friends) {
        this.friends = friends;
    }


    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(userPhoto.get());

    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        userPhoto = new SimpleObjectProperty<>((byte [])s.readObject());
    }

}
