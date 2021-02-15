package JETS.ui.helpers;

import JETS.ui.controllers.CountryCodeData;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommonValidations {
    private boolean validDisplayName = false;
    private boolean validPhoneNumber= false;
    private boolean validEmail = false;
    private boolean validPassword = false;
    private boolean firstTimeChkPass=true; //to be changed



    public static boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance(); // create the EmailValidator instance
        return validator.isValid(email); // check for valid email addresses using isValid method
    }

    public static void updateUserInfo(){
        //to be implemented
    }

}
