package JETS.ui.helpers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.time.Duration;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

public class appNotifications {

    private static final appNotifications notifyApp = new appNotifications();

    public static appNotifications getInstance() {return notifyApp;}

    public void CanceAddl(){
        Dialog dialog = new Dialog();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15,15,15,15));
        grid.setVgap(25);
        grid.setHgap(10);

        Label mobilePhone = new Label("Enter Your Friend's Mobile Phone Number: ");
        Font fontlabel = Font.font("Cambria", FontWeight.BOLD, 15);
        mobilePhone.setFont(fontlabel);
        mobilePhone.setTextFill(Color.WHITE);
        GridPane.setConstraints(mobilePhone,0,0);

        TextField text1 = new TextField();
        text1.setStyle("-fx-background-color: #e3fafa; -fx-border-color: #076666");
        text1.setFont(Font.font("Cambria",FontWeight.BOLD,13));
        GridPane.setConstraints(text1,1,0);

        Button AddFriend = new Button("Add Friend");
        AddFriend.setStyle("-fx-background-color: #e3fafa; -fx-cursor: hand;");
        Font font = Font.font("Cambria", FontWeight.BOLD, 13);
        AddFriend.setFont(font);

        Button Cancel = new Button("Cancel");
        Cancel.setStyle("-fx-background-color: #e3fafa; -fx-cursor: hand;");
        Cancel.setFont(font);
        Cancel.setCancelButton(true);

        HBox hboxbtn = new HBox(20,AddFriend,Cancel);
        GridPane.setConstraints(hboxbtn,1,1);
        grid.getChildren().addAll(mobilePhone,text1,hboxbtn);
        grid.getStylesheets().add("x.css");
        dialog.getDialogPane().setContent(grid);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("logo.png").toString()));

    }


    public void okai(String msg, String title){

        Dialog dialog = new Dialog();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15,15,15,15));
        grid.setVgap(25);
        grid.setHgap(10);
        //message
        Label message = new Label(msg);
        Font fontlabel = Font.font("Cambria", FontWeight.BOLD, 15);
        message.setFont(fontlabel);
        message.setTextFill(Color.WHITE);
        GridPane.setConstraints(message,1,0);

        Button oK = new Button("OK");
        oK.setStyle("-fx-background-color: #e3fafa; -fx-cursor: hand;");
        Font font = Font.font("Cambria", FontWeight.BOLD, 13);
        oK.setFont(font);
        GridPane.setConstraints(oK,4,0);

        grid.getChildren().addAll(message,oK);

        grid.setStyle("-fx-background-color: #008080");
        dialog.getDialogPane().setContent(grid);
        dialog.setResizable(false);
        //dialogTitle
        dialog.setTitle(title);
        dialog.show();
//        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
//        stage.getIcons().add(new Image(this.getClass().getResource("logo.png").toString()));

        //error, info, notification
    }


    public void sideInfo(String message, String Title, javafx.util.Duration duration){
        TrayNotification tray = new TrayNotification();
        tray.setTitle(Title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(duration);
    }

    public void sideError(String message, String Title, javafx.util.Duration duration){
        TrayNotification tray = new TrayNotification();
        tray.setTitle(Title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.ERROR);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(duration);
    }





}
