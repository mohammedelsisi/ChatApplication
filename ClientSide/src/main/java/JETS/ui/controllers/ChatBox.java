package JETS.ui.controllers;

import JETS.net.ClientProxy;
import JETS.net.ServerOfflineHandler;
import JETS.ui.helpers.FileManager;
import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.appNotifications;
import Models.CurrentUser;
import Models.FileEntity;
import Models.MessageEntity;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
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
import javafx.stage.DirectoryChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.rmi.RemoteException;

public class ChatBox extends GridPane {

    private final MessageEntity MESSAGE;
    private boolean isResponse;

    public ChatBox(MessageEntity messageEntity) {
        super();
        CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
        MESSAGE = messageEntity;
        isResponse = !MESSAGE.getSenderPhone().equals(currentUser.getPhoneNumber());
        Circle userImage = null;
        if (!isResponse) {
            this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            createUserNameLabel(currentUser.displayNameProperty());
            userImage = createUserPhoto(currentUser.userPhotoProperty());
        } else {
            FriendsManager friendsManager = FriendsManager.getInstance();
            this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            createUserNameLabel(friendsManager.getFriendNameProperty(MESSAGE.getSenderPhone()));
            userImage = createUserPhoto(friendsManager.getFriendPhotoProperty(MESSAGE.getSenderPhone()));
        }

        this.getColumnConstraints().addAll(createCommonColumnConstraints(3, new Priority[]{Priority.NEVER, Priority.SOMETIMES, Priority.SOMETIMES}));
        this.getRowConstraints().addAll(createCommonRowConstraints(3, new float[]{-1.0f, 20.0f, -1.0f}, new Priority[]{Priority.SOMETIMES, Priority.ALWAYS, Priority.SOMETIMES}));

        Group imagesContainer = new Group();
        Circle status = createUserStatus();
        imagesContainer.getChildren().addAll(userImage, status);
        this.add(imagesContainer, 0, 0, 1, 3);
        setValignment(imagesContainer, VPos.TOP);
        setHalignment(imagesContainer, HPos.CENTER);

        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setMinSize(20.0, 50.0);
        messageContainer.setStyle("-fx-background-color: #008080; -fx-shape:  \"M367.64,1058.34c-15.98,0-28.98-13-28.98-28.98V855.64c0-15.98,13-28.98,28.98-28.98h1363.7v202.7c0,15.98-13,28.98-28.98,28.98H367.64z\"\"M367.64,1058.34c-15.98,0-28.98-13-28.98-28.98V855.64c0-15.98,13-28.98,28.98-28.98h1363.7v202.7c0,15.98-13,28.98-28.98,28.98H367.64z\"; -fx-background-insets: -5 -10 -5 -5;");
        messageContainer.setScaleX(-1.0);
        messageContainer.setPadding(new Insets(0, 10, 0, 10));
        this.add(messageContainer, 1, 1, 2, 1);
        setVgrow(messageContainer, Priority.ALWAYS);
        setMargin(messageContainer, new Insets(10.0, 10.0, 5.0, 10.0));

        FileEntity fileEntity = MESSAGE.getFile();
        if (fileEntity != null) {
            messageContainer.getChildren().add(createFileDisplay(MESSAGE.getFile()));
        }
        Label message = new Label(MESSAGE.getMsgContent());
        message.setScaleX(-1.0);
        message.setFont(Font.font("Arial", 13.0));
        message.setWrapText(true);
        message.setTextFill(Color.WHITE);
        message.setAlignment(Pos.BASELINE_CENTER);
        messageContainer.getChildren().add(message);
        VBox.setVgrow(message, Priority.ALWAYS);


        FontIcon messageSeen = new FontIcon();
        messageSeen.setIconLiteral("mdi2c-check");
        messageSeen.setIconSize(20);
        messageSeen.setTextAlignment(TextAlignment.CENTER);
        messageSeen.setWrappingWidth(40.0);
        this.add(messageSeen, 1, 2, 1, 1);
        setValignment(messageSeen, VPos.CENTER);


        Text messageDate = new Text("DD-MM-YY HH:MI:SS AM");
        messageDate.setStrokeWidth(0);
        messageDate.setFont(Font.font("Calibri", 12.0));
        this.add(messageDate, 2, 2, 1, 1);
        setHalignment(messageDate, HPos.RIGHT);
        setValignment(messageDate, VPos.CENTER);
        setMargin(messageDate, new Insets(0, 5, 0, 5));

        this.setMaxHeight(Double.MAX_VALUE);
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
        setHalignment(userName, HPos.LEFT);
        setValignment(userName, VPos.TOP);
        setHgrow(userName, Priority.ALWAYS);
        this.setHgap(5.0);
        return userName;
    }

    private HBox createFileDisplay(FileEntity file) {
        HBox fileContainer = new HBox(5.0);
        fileContainer.setAlignment(Pos.CENTER);
        fileContainer.setPadding(new Insets(5));
        fileContainer.setStyle("-fx-background-color: #52ACA8; -fx-background-radius: 5px;");
        FontIcon fileIcon = new FontIcon("mdi2f-file-outline");
        fileIcon.setIconColor(Color.WHITE);
        fileIcon.setIconSize(35);
        Label filename = new Label(file.getName());
        filename.setAlignment(Pos.BASELINE_CENTER);
        filename.setTextFill(Color.WHITE);
        filename.setWrapText(true);
        filename.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
        JFXButton downloadBtn = new JFXButton("Download");
        downloadBtn.setTextFill(Color.web("#fff8f8"));
        filename.setScaleX(-1);
        downloadBtn.setScaleX(-1);
        if (!isResponse) {
            downloadBtn.setScaleZ(-1);
        }
        downloadBtn.addEventHandler(ActionEvent.ACTION, (e) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Save File");
            File saveFile = directoryChooser.showDialog(getScene().getWindow());
            if (saveFile != null) {
                Path savePath = saveFile.toPath().resolve(file.getName());
                try {
                    FileEntity fileEntity = ClientProxy.getInstance().getFileData(file, MESSAGE.getId());
                if (fileEntity != null) {
                    FileManager.writeFile(savePath.toFile(), fileEntity.getData());
                }
                } catch (RemoteException remoteException) {
                    ServerOfflineHandler.handle("Sorry, cannot continue your request. :(");
                }
            }
        });
        fileContainer.getChildren().addAll(fileIcon, filename, downloadBtn);
        VBox.setMargin(fileContainer, new Insets(5));
        return fileContainer;
    }

    private ColumnConstraints[] createCommonColumnConstraints(int number, Priority[] prioritiesHGrow) {
        ColumnConstraints[] constraints = new ColumnConstraints[number];
        if (prioritiesHGrow.length == number) {
            for (int i = 0; i < number; i++) {
                ColumnConstraints tempConstraint = new ColumnConstraints();
                tempConstraint.setHgrow(prioritiesHGrow[i]);
                constraints[i] = tempConstraint;
            }
        }
        return constraints;
    }

    private RowConstraints[] createCommonRowConstraints(int number, float[] minHeight, Priority[] prioritiesVGrow) {
        RowConstraints[] constraints = new RowConstraints[number];
        if (prioritiesVGrow.length == number) {
            for (int i = 0; i < number; i++) {
                RowConstraints tempConstraint = new RowConstraints();
                tempConstraint.setVgrow(prioritiesVGrow[i]);
                if (minHeight[i] != -1.0f) {
                    tempConstraint.setMinHeight(minHeight[i]);
                }
                constraints[i] = tempConstraint;
            }
        }
        return constraints;
    }
}
