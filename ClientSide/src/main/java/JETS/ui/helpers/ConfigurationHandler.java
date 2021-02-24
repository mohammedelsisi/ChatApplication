package JETS.ui.helpers;

import JETS.ClientMain;
import JETS.ClientServices.ClientServicesFactory;
import Models.CurrentUser;
import Models.LoginEntity;
import com.mysql.cj.log.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigurationHandler {
    private static ConfigurationHandler configurationHandler = new ConfigurationHandler();

    public static ConfigurationHandler getInstance() {
        return configurationHandler;
    }

    //upon closing the window, store the user info in a property file(basiony)
    public void rememberMe(LoginEntity loginEntity) {
        try (OutputStream outputStream = new FileOutputStream("config.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty("User.Phone", loginEntity.getPhoneNumber());
            prop.setProperty("user.password", loginEntity.getPassword());

            // save properties to project root folder
            prop.store(outputStream, null);

        } catch (IOException e) {
            System.out.println("Could not create a file" + e.getMessage());
        }

    }

    public LoginEntity getLoginEntity() {
        LoginEntity loginEntity = null;

        if (Files.exists(Path.of("config.properties"))) {


            try (InputStream input = new FileInputStream(("config.properties"))) {

                Properties prop = new Properties();

                // load a properties file
                prop.load(input);

                // get the property value and assign then to the user.
                String userPassword = prop.getProperty("user.password");
                String userPhone = prop.getProperty("User.Phone");

                loginEntity = new LoginEntity(userPhone, userPassword);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loginEntity;
    }

    public void clearPassword() {

        rememberMe(new LoginEntity("", ""));
    }

}
