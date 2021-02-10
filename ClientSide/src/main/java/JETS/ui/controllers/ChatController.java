package JETS.ui.controllers;

import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ChatController {
    public Group AddFriend;

    public void switchToSecondary(KeyEvent keyEvent) {
    }

    public void requestFriend(MouseEvent mouseEvent){
        Alert a1 = new Alert(Alert.AlertType.NONE,
                "Here is the info about Notepad", ButtonType.OK);

        a1.setGraphic(new ImageView(new Image("download.png")));
        a1.setTitle("About Us");
        a1.show();
    }
}
