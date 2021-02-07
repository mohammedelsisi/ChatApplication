package Models;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CurrentUser {

    private String phoneNumber;
    private String password;
    private String email ;
    private String gender;
    private String firstName;
    private String lastName;
    private int age;
    private String country;
    private String Bio;
    private final StringProperty displayName = new SimpleStringProperty();
    private final StringProperty userPhoto = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();



    public String getUserPhoto() {
        return userPhoto.get();
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getDisplayName() {
        return displayName.get();
    }

    public StringProperty displayNameProperty() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

}
