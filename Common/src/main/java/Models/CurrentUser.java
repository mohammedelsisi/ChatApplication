package Models;

import Models.dto.DataTransferObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CurrentUser implements DataTransferObject {
    private long id;

    private String phoneNumber;

    private String password;
    private String email;
    private String gender;
    private int age;
    private String country;
    private String Bio;
    private String displayName;
    transient private final StringProperty displayNameProperty = new SimpleStringProperty();
    transient private final StringProperty userPhoto = new SimpleStringProperty();
    transient private final StringProperty status = new SimpleStringProperty();

    public String getUserPhoto() {
        return userPhoto.get();
    }

    public void setId(long id) {
        this.id = id;
    }

    public StringProperty userPhotoProperty() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto.set(userPhoto);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
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

    public void setDisplayName(String displayName) {
        this.displayNameProperty.set(displayName);
        this.displayName=displayName;
    }
    public String getDisplayName() {
        return displayName;
    }


    @Override
    public long getId() {
        return id;
    }
}
