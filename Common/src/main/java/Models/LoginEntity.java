package Models;

import java.io.Serializable;

public class LoginEntity implements Serializable {
    private String phoneNumber;
    private String Password;

    public LoginEntity(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        Password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
