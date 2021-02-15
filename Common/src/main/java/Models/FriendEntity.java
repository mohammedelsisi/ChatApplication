package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.Serializable;

public class FriendEntity implements Serializable {
     transient private final StringProperty status = new SimpleStringProperty();
    transient private final StringProperty userPhoto = new SimpleStringProperty();
    private String phoneNumber;
    private String displayName;
    private String Bio;
    private  String statusVal ;

    private File photoPath;

    public File getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(File photoPath) {
        this.photoPath = photoPath;

    }
    public FriendEntity(String phoneNumber, String displayName, String bio, String status) {
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        Bio = bio;
        statusVal=status;
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

    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
    }



    @Override
    public int hashCode() {
        return Integer.parseInt(phoneNumber.substring(1));
    }

    @Override
    public boolean equals(Object obj) {
        return phoneNumber.equals(((FriendEntity)obj).phoneNumber);
    }
}
