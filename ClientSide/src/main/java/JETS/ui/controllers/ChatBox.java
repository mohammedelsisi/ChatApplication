package JETS.ui.controllers;

import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import Models.CurrentUser;
import Models.MessageEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ChatBox extends GridPane {

    public ChatBox(MessageEntity messageEntity) {
        super();
        CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
        Circle userImage = null;
        if (messageEntity.getSenderPhone().equals(currentUser.getPhoneNumber())) {
            this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            createUserNameLabel(currentUser.displayNameProperty());
            userImage = createUserPhoto(currentUser.userPhotoProperty());
        } else {
            FriendsManager friendsManager = FriendsManager.getInstance();
            this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            createUserNameLabel(friendsManager.getFriendNameProperty(messageEntity.getSenderPhone()));
            userImage = createUserPhoto(friendsManager.getFriendPhotoProperty(messageEntity.getSenderPhone()));
        }

        this.getColumnConstraints().addAll(createCommonColumnConstraints(3));
        this.getRowConstraints().addAll(createCommonRowConstraints(3, new float[]{-1.0f, 20.0f, -1.0f}));

        Group imagesContainer = new Group();
        Circle status = createUserStatus();
        imagesContainer.getChildren().addAll(userImage, status);
        this.add(imagesContainer, 0, 0, 1, 3);
        this.setValignment(imagesContainer, VPos.TOP);
        this.setHalignment(imagesContainer, HPos.CENTER);

        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setMinSize(20.0, 50.0);
        messageContainer.setStyle("-fx-background-color: #008080; -fx-shape:  \"M367.64,1058.34c-15.98,0-28.98-13-28.98-28.98V855.64c0-15.98,13-28.98,28.98-28.98h1363.7v202.7c0,15.98-13,28.98-28.98,28.98H367.64z\"\"M367.64,1058.34c-15.98,0-28.98-13-28.98-28.98V855.64c0-15.98,13-28.98,28.98-28.98h1363.7v202.7c0,15.98-13,28.98-28.98,28.98H367.64z\"; -fx-background-insets: -5 -10 -5 -5;");
        messageContainer.setScaleX(-1.0);
        messageContainer.setPadding(new Insets(0, 10, 0, 10));
        this.add(messageContainer, 1, 1, 2, 1);
        this.setVgrow(messageContainer, Priority.ALWAYS);
        this.setMargin(messageContainer, new Insets(10.0, 10.0, 5.0, 10.0));

        Label message = new Label(messageEntity.getMsgContent());
        message.setScaleX(-1.0);
        message.setFont(Font.font("Arial", 13.0));
        message.setWrapText(true);
        message.setTextFill(Color.WHITE);
        message.setAlignment(Pos.BASELINE_CENTER);
        messageContainer.getChildren().add(message);
        messageContainer.setVgrow(message, Priority.ALWAYS);


        FontIcon messageSeen = new FontIcon();
        messageSeen.setIconLiteral("mdi2c-check");
        messageSeen.setIconSize(20);
        messageSeen.setTextAlignment(TextAlignment.CENTER);
        messageSeen.setWrappingWidth(40.0);
        this.add(messageSeen, 1, 2, 1, 1);
        this.setValignment(messageSeen, VPos.CENTER);


        Text messageDate = new Text("DD-MM-YY HH:MI:SS AM");
        messageDate.setStrokeWidth(0);
        messageDate.setFont(Font.font("Calibri", 12.0));
        this.add(messageDate, 2, 2, 1, 1);
        this.setHalignment(messageDate, HPos.RIGHT);
        this.setValignment(messageDate, VPos.CENTER);
        this.setMargin(messageDate, new Insets(0, 5, 0, 5));

        this.setPadding(new Insets(5.0));
    }

    private Circle createUserPhoto(SimpleObjectProperty<byte[]> userPhoto) {
        Circle avatar = new Circle(27);
        avatar.setLayoutX(38.0);
        avatar.setLayoutY(27.0);
        avatar.setStrokeWidth(0.0);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(userPhoto.get())) {
            avatar.setFill(new ImagePattern(new Image(inputStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userPhoto.addListener((obs, oldVal, newVal) -> {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(newVal)) {
                avatar.setFill(new ImagePattern(new Image(inputStream)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return avatar;
    }

    private Circle createUserStatus() {
        Circle status = new Circle(6.0);
        status.setLayoutX(22.0);
        status.setLayoutY(48.0);
        status.setStrokeWidth(0.0);
        status.setFill(Color.web("#1fff32"));
        return status;
    }

    private Label createUserNameLabel(StringProperty usernameProperty) {
        Label userName = new Label();
        userName.textProperty().bind(usernameProperty);
        userName.setFont(Font.font("Calibri", FontWeight.BOLD, 12.0));
        this.add(userName, 1, 0, 2, 1);
        this.setHalignment(userName, HPos.LEFT);
        this.setValignment(userName, VPos.TOP);
        this.setHgrow(userName, Priority.ALWAYS);
        this.setHgap(5.0);
        return userName;
    }

    private ColumnConstraints[] createCommonColumnConstraints(int number) {
        ColumnConstraints[] constraints = new ColumnConstraints[number];
        for (int i = 0; i < number; i++) {
            ColumnConstraints tempConstraint = new ColumnConstraints();
            tempConstraint.setHgrow(Priority.SOMETIMES);
            constraints[i] = tempConstraint;
        }
        return constraints;
    }

    private RowConstraints[] createCommonRowConstraints(int number, float[] minHeight) {
        RowConstraints[] constraints = new RowConstraints[number];
        for (int i = 0; i < number; i++) {
            RowConstraints tempConstraint = new RowConstraints();
            tempConstraint.setVgrow(Priority.SOMETIMES);
            if (minHeight[i] != -1.0f) {
                tempConstraint.setMinHeight(minHeight[i]);
            }
            constraints[i] = tempConstraint;
        }
        return constraints;
    }
}
