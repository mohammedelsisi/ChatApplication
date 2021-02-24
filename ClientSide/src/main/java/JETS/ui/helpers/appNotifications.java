package JETS.ui.helpers;

import JETS.ClientServices.ClientServicesFactory;
import JETS.net.ClientProxy;
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
import javafx.stage.Window;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Predicate;

import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

public class appNotifications {

    private static final appNotifications notifyApp = new appNotifications();

    public static appNotifications getInstance() {
        return notifyApp;
    }

    public void CanceAddl() {
        Dialog dialog = new Dialog();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setVgap(25);
        grid.setHgap(10);

        Label mobilePhone = new Label("Enter Your Friend's Mobile Phone Number: ");
        Font fontlabel = Font.font("Cambria", FontWeight.BOLD, 15);
        mobilePhone.setFont(fontlabel);
        mobilePhone.setTextFill(Color.WHITE);
        GridPane.setConstraints(mobilePhone, 0, 0);

        TextField text1 = new TextField();
        text1.setStyle("-fx-background-color: #e3fafa; -fx-border-color: #076666");
        text1.setFont(Font.font("Cambria", FontWeight.BOLD, 13));
        GridPane.setConstraints(text1, 1, 0);

        Button AddFriend = new Button("Add Friend");
        AddFriend.setStyle("-fx-background-color: #e3fafa; -fx-cursor: hand;");
        Font font = Font.font("Cambria", FontWeight.BOLD, 13);
        AddFriend.setFont(font);

        Button Cancel = new Button("Cancel");
        Cancel.setStyle("-fx-background-color: #e3fafa; -fx-cursor: hand;");
        Cancel.setFont(font);
        Cancel.setCancelButton(true);

        HBox hboxbtn = new HBox(20, AddFriend, Cancel);
        GridPane.setConstraints(hboxbtn, 1, 1);
        grid.getChildren().addAll(mobilePhone, text1, hboxbtn);
        grid.getStylesheets().add("x.css");
        dialog.getDialogPane().setContent(grid);
        // Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        // stage.getIcons().add(new Image(this.getClass().getResource("logo.png").toString()));

    }


    public void okai(String msg, String title) {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        Label a = new Label(msg);
        a.setWrapText(true);
        a.setTextFill(Color.WHITE);
        GridPane grid = new GridPane();
        grid.add(a, 2, 0);
        alert.getDialogPane().setPrefSize(400, 130);
        alert.getDialogPane().setStyle("-fx-background-color: #0e393c ; -fx-font-weight: BOLD; -fx-font-size: 15;-fx-font-family: Cambria; ");
        ButtonType btnok = new ButtonType("OK", OK_DONE);
        alert.getDialogPane().setContent(grid);
        alert.getButtonTypes().add(btnok);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Pics/logo.png").toString()));
        alert.showAndWait();

        //error, info, notification
    }

    public void cancel(String msg,String title){

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle(title);
            Label a  = new Label(msg);
            a.setWrapText(true);
            a.setTextFill(Color.WHITE);
            GridPane grid = new GridPane();
            grid.add(a,2,0);
            alert.getDialogPane().setPrefSize(400,130);
            alert.getDialogPane().setStyle("-fx-background-color: #1c9696 ; -fx-font-weight: BOLD; -fx-font-size: 15;-fx-font-family: Cambria; ");
            ButtonType btnok = new ButtonType("OK", OK_DONE);
            ButtonType cancel = new ButtonType("Cancel",CANCEL_CLOSE);
            alert.getDialogPane().setContent(grid);
            alert.getButtonTypes().add(btnok);
            alert.getButtonTypes().add(cancel);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/Pics/logo.png").toString()));
            alert.showAndWait();

    }

    public void sucessNotify(String message, String Title, javafx.util.Duration duration) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(Title);
        tray.setMessage(message);

        tray.setNotificationType(NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(duration);
    }

    public void sideInfo(String message, String Title, javafx.util.Duration duration) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(Title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(duration);
    }

    public void sideError(String message, String Title, javafx.util.Duration duration) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(Title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.ERROR);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(duration);
    }

    public void cancel(String msg, String title, Runnable runnable) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        Label a = new Label(msg);
        a.setWrapText(true);
        a.setTextFill(Color.WHITE);
        GridPane grid = new GridPane();
        grid.add(a, 2, 0);
        alert.getDialogPane().setPrefSize(400, 130);
        alert.getDialogPane().setStyle("-fx-background-color: #1c9696 ; -fx-font-weight: BOLD; -fx-font-size: 15;-fx-font-family: Cambria; ");
        alert.getDialogPane().setContent(grid);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Pics/logo.png").toString()));
        if (alert.showAndWait().get() == ButtonType.OK) {
            runnable.run();
        }
    }

    public void errorBox(String msg, String title) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        Label a = new Label(msg);
        a.setWrapText(true);
        a.setTextFill(Color.WHITE);
        GridPane grid = new GridPane();
        grid.add(a, 2, 0);
        alert.getDialogPane().setPrefSize(400, 130);
        alert.getDialogPane().setStyle("-fx-background-color: #0e393c ; -fx-font-weight: BOLD; -fx-font-size: 15;-fx-font-family: Cambria; ");
        alert.getDialogPane().setContent(grid);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Pics/logo.png").toString()));
        alert.showAndWait();

        //error, info, notification
    }
}
