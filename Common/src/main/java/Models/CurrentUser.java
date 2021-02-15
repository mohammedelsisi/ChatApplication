package Models;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentUser implements Serializable {

    transient private final StringProperty displayNameProperty = new SimpleStringProperty();
    transient private final StringProperty userPhoto = new SimpleStringProperty();
    transient private final StringProperty status = new SimpleStringProperty();
    private String phoneNumber;
    private String password;
    private String email;
    private String gender;
    private int age;
    private String country;
    private String Bio;
    private String displayName;
    private String statusVal;
    private File photoPath;

    public File getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(File photoPath) {
        this.photoPath = photoPath;

    }


    private Map<String,FriendEntity> friends=new HashMap<>();

    public String getUserPhoto() {
        return userPhoto.get();
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto.set(userPhoto);
    }

    public StringProperty userPhotoProperty() {
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

    public Map<String,FriendEntity> getFriends() {
        return friends;
    }

    public void setFriends(Map<String,FriendEntity> friends) {
        this.friends = friends;
    }
    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
    }

}
