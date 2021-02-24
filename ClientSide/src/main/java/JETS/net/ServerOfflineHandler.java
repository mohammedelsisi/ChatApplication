package JETS.net;

import JETS.ui.helpers.ConfigurationHandler;
import JETS.ui.helpers.StageCoordinator;
import JETS.ui.helpers.appNotifications;

public class ServerOfflineHandler {

    public static void handle(String message) {
        StageCoordinator.getInstance().switchToLoginScene();
        appNotifications.getInstance().errorBox(message, "Connection Error");
        ConfigurationHandler.getInstance().clearPassword();
    }
}
