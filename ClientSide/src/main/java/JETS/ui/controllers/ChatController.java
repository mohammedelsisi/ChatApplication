package JETS.ui.controllers;

import JETS.ClientServices.ClientServicesFactory;
import JETS.SavingChat.MessageType;
import JETS.SavingChat.SavingSession;
import JETS.bot.BotManager;
import JETS.net.ClientProxy;
import JETS.net.ServerOfflineHandler;
import JETS.ui.helpers.*;
import Models.*;
import com.jfoenix.controls.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class ChatController implements Initializable {
    public JFXButton btnChangeUserPic;
    public Label lblUserName;

    private final Map<Integer, List<MessageType>> chatHistory = new HashMap<>();
    private final ObservableList<FriendEntity> requestLists = FXCollections.observableArrayList();
    private final ObservableList<FriendEntity> friendsList = FXCollections.observableArrayList();
    private final Map<ChatEntitiy, VBox> chatBoxesMap = new HashMap<>();
    public JFXTextArea messageField;
    @FXML
    public VBox contacts;
    public VBox chatsVbox;
    @FXML
    public ComboBox statusComboBox;
    public GridPane grid = new GridPane();
    public Dialog dialog = new Dialog();
    public Alert alert;
    public AnchorPane MainAnchorPane;
    ChatEntitiy chatEntitiy;
    CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
    ListView<FriendEntity> listViewRequestList;
    ListView<FriendEntity> listViewFriendList = new ListView<>();
    private BotManager chatBot = new BotManager();
    @FXML
    private FontIcon fileButton;
    @FXML
    private StackPane spChatBoxes;
    @FXML
    private Circle circleView;
    @FXML
    private Label receiverName;
    @FXML
    private TabPane tabPane;
    @FXML
    private VBox messagesContainer;
    private Text textHolder = new Text();
    @FXML
    private HBox chatControllersContainer;
    private double oldMessageFieldHigh;
    private File attachedFile = null;
    @FXML
    private JFXToggleButton botToggle;
    @FXML
    private HBox HBDisplayName;
    @FXML
    private JFXDatePicker DPDatePicker;
    @FXML
    private JFXTextField tFDisplayName;
    @FXML
    private JFXTextField tFEmailAddress;
    @FXML
    private JFXComboBox<String> cbGender;
    @FXML
    private JFXTextArea TABio;
    @FXML
    private JFXButton btnChangePassword;
    @FXML
    private JFXButton btnSaveChanges;

    public Map<Integer, List<MessageType>> getChatHistory() {
        return chatHistory;
    }

    public void loadRequestList() {
        try {
            List<FriendEntity> friends = ClientProxy.getInstance().getFriendRequests(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber());
            if (friends != null) {
                requestLists.setAll(friends);
            }
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Sorry, cannot continue your request. :(");
        }
    }

    /*
    to take the user data from the specified field and store it in the current object
     */

    public void loadFriendList() {
        try {
            List<FriendEntity> friends = ClientProxy.getInstance().getFriends(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber());
            if (friends != null) {
                friendsList.addAll(friends);
                for (FriendEntity friendEntity : friendsList) {
                    ModelsFactory.getInstance().getCurrentUser().getFriends().put(friendEntity.getPhoneNumber(), friendEntity);
                }
            }
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Sorry, cannot continue your request. :(");
        }
    }

    /*
    this method will show the put up for changing password
     */
    @FXML
    void HandleChangePassword(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/changePasswordPane.fxml"));
            Parent passwordPane = fxmlLoader.load();
            Scene passwordScene = new Scene(passwordPane, 471, 248);
            Stage newWindow = new Stage();
            newWindow.initStyle(StageStyle.UNDECORATED);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle("Change Password");
            newWindow.setScene(passwordScene);
            newWindow.setResizable(false);
            newWindow.setX(500);
            newWindow.setY(200);
            newWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUserName.setText(currentUser.getDisplayName());
        circleView.setFill(new ImagePattern(new Image(new ByteArrayInputStream(currentUser.getUserPhoto()))));

        currentUser.userPhotoProperty().addListener((obs, oldVal, newVal) -> {

            circleView.setFill(new ImagePattern(new Image(new ByteArrayInputStream(newVal))));

        });

        loadRequestList();
        loadFriendList();

        listViewRequestList = new ListView(requestLists);

        listViewRequestList.setCellFactory(new Callback<ListView<FriendEntity>, ListCell<FriendEntity>>() {
            @Override
            public ListCell<FriendEntity> call(ListView<FriendEntity> friendListListView) {
                ListCell<FriendEntity> cell = new ListCell<>() {
                    Button acceptButton = new Button("Accept");
                    Button rejectButton = new Button("Reject");

                    @Override
                    protected void updateItem(FriendEntity friendEntity, boolean b) {
                        super.updateItem(friendEntity, b);
                        if (!b) {

                            try {
                                HBox hBox = new HBox(10);


                                rejectButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        try {
                                            ClientProxy.getInstance().refuseRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), friendEntity.getPhoneNumber());
                                            requestLists.remove(friendEntity);
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                acceptButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        try {
                                            ClientProxy.getInstance().acceptRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), friendEntity.getPhoneNumber());
                                            requestLists.remove(friendEntity);
                                            friendsList.add(friendEntity);
                                            currentUser.getFriends().put(friendEntity.getPhoneNumber(), friendEntity);
                                            appNotifications.getInstance().sucessNotify("You and " + friendEntity.getDisplayName() + " Are Now Friends", "Congratulations", Duration.seconds(5));

                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                Pane pane = new Pane();
                                Circle imageCircle = new Circle(20);
                                byte[] userImage = null;
                                if ((userImage = friendEntity.getUserPhoto()) != null) {
                                    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userImage)) {
                                        imageCircle.setFill(new ImagePattern(new Image(byteArrayInputStream)));
                                    }
                                }

//                                imageView.setFitWidth(50);
//                                imageView.setFitHeight(50);
                                Label label = new Label();
                                label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getPhoneNumber());
                                label.setGraphic(imageCircle);
                                hBox.getChildren().addAll(label, pane, acceptButton, rejectButton);
                                hBox.setHgrow(pane, Priority.ALWAYS);
                                hBox.setFillHeight(true);
                                hBox.setSpacing(5.0);
                                hBox.setAlignment(Pos.CENTER_LEFT);
                                this.setGraphic(hBox);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        textHolder.textProperty().bind(messageField.textProperty());
        textHolder.setWrappingWidth(600);
        textHolder.layoutBoundsProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldMessageFieldHigh != newValue.getHeight() && newValue.getHeight() < 100) {
                oldMessageFieldHigh = newValue.getHeight();
                messageField.setPrefHeight(Math.max(oldMessageFieldHigh + 15, 50));
            }
        });
        SortedList<FriendEntity> sortedListFriends = new SortedList<>(friendsList, new Comparator<FriendEntity>() {
            @Override
            public int compare(FriendEntity o1, FriendEntity o2) {
                return o1.getStatus().compareTo(o2.getStatus());
            }
        });
        listViewFriendList = new ListView<>(sortedListFriends);
        listViewFriendList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listViewFriendList.setCellFactory(new Callback<ListView<FriendEntity>, ListCell<FriendEntity>>() {

            @Override
            public ListCell<FriendEntity> call(ListView<FriendEntity> friendEntityListView) {


                return new ListCell<>() {
                    {
                        prefWidthProperty().bind(friendEntityListView.widthProperty().subtract(2));
                        setMaxWidth(Control.USE_PREF_SIZE);
                    }

                    @Override
                    protected void updateItem(FriendEntity friendEntity, boolean b) {
                        super.updateItem(friendEntity, b);

                        if (!b) {
                            HBox hBox = new HBox();
                            Group imagesContainer = new Group();
                            Circle imageCircle = new Circle(20);
                            Label label = new Label();
                            Circle status = new Circle(5);
                            status.setLayoutX(12.0);
                            status.setLayoutY(15.0);
                            status.setStrokeWidth(1.0);
                            status.setStroke(Color.WHITE);
                            if (friendEntity.getStatus().equals("AVAILABLE")) {
                                status.setFill(Color.LIME);
                            } else if (friendEntity.getStatus().equals("AWAY")) {
                                status.setFill(Color.GREENYELLOW);
                            } else if (friendEntity.getStatus().equals("BUSY")) {
                                status.setFill(Color.RED);
                            } else {
                                status.setFill(Color.GRAY);
                            }
                            imagesContainer.getChildren().addAll(imageCircle, status);
                            byte[] userImage = null;
                            if ((userImage = friendEntity.getUserPhoto()) != null) {
                                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userImage)) {
                                    imageCircle.setFill(new ImagePattern(new Image(byteArrayInputStream)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getStatus());
                            hBox.getChildren().addAll(imagesContainer, label);


                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setFillHeight(true);
                            hBox.setSpacing(5.0);
                            hBox.setPadding(new Insets(10));
                            this.setGraphic(hBox);
                            //Image
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };

            }
        });
        contacts.getChildren().addAll(listViewFriendList);
        statusComboBox.setValue("AVAILABLE");
        statusComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // notify my friends during termination or sign out
            ModelsFactory.getInstance().getCurrentUser().setStatus(newValue.toString());
            try {
                System.out.println(newValue.toString());
                ClientProxy.getInstance().tellStatus(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), newValue.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentUser.getUserPhoto())) {
            circleView.setFill(new ImagePattern(new Image(byteArrayInputStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //retrieving current user data and displaying it inside the update info tab
        CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
        System.out.println(currentUser.getDOB());
        System.out.println(DPDatePicker.getValue());
        DPDatePicker.setValue(LocalDate.parse(currentUser.getDOB()));
        tFDisplayName.setText(currentUser.getDisplayName());
        tFEmailAddress.setText(currentUser.getEmail());
        TABio.setText(currentUser.getBio());
        ObservableList<String> genderOptions = FXCollections.observableArrayList("MALE", "FEMALE");
        String gender = currentUser.getGender();
        cbGender.getItems().addAll(genderOptions);
        cbGender.getSelectionModel().select(gender);
    }

    public void sendMessage(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!messageField.getText().isBlank() || attachedFile != null) {
                if (keyEvent.isAltDown()) {
                    messageField.appendText("\n");
                } else {
                    try {
                        if (chatEntitiy != null) {

                            VBox vBox = addChatToMap(chatEntitiy);
                            MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText().trim(), currentUser.getPhoneNumber());

                            if (!chatHistory.containsKey(chatEntitiy.getId())) {
                                chatHistory.put(chatEntitiy.getId(), new ArrayList<MessageType>());
                            }
                            chatHistory.get(chatEntitiy.getId()).add(new MessageType(msg.getSenderPhone(), msg.getMsgContent(), "left"));


//                        if (chatEntitiy != null) {
//                            MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText().trim(), currentUser.getPhoneNumber());

                            if (attachedFile != null) {
                                msg.setFile(new FileEntity(attachedFile.getName(), FileManager.readFile(attachedFile)));
                                attachedFile = null;
                                chatControllersContainer.getChildren().remove(0);
                            }
                            vBox.getChildren().add(new ChatBox(msg));

                            ClientProxy.getInstance().sendMessage(msg);
                            messageField.clear();
                        }

                    } catch (RemoteException e) {
                        ServerOfflineHandler.handle("Sorry, cannot continue your request. :(");
                    }
                }
            } else {
                messageField.clear();
            }
        }

    }

    private void sendMessage(String message, ChatEntitiy chat) {
        try {
            VBox vBox = addChatToMap(chat);
            MessageEntity msg = new MessageEntity(chat, message, currentUser.getPhoneNumber());
            vBox.getChildren().add(new ChatBox(msg));
            ClientProxy.getInstance().sendMessage(msg);
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Bot cannot send messages right now.");
        }
    }


    @FXML
    public void requestFriend() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/AddFriend.fxml"));
            Parent addFriendPane = fxmlLoader.load();
            Scene addFriendScene = new Scene(addFriendPane, 471, 195);
            Stage newWindow = new Stage();
            newWindow.initStyle(StageStyle.UNDECORATED);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle("ADD FRIEND");
            newWindow.setScene(addFriendScene);
            newWindow.setResizable(false);
            newWindow.setX(500);
            newWindow.setY(200);
            newWindow.show();
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Bot cannot send messages right now.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void requestsHandle() {
        Alert alert = new Alert(Alert.AlertType.NONE);

        DialogPane dialogPane = alert.getDialogPane();


        dialogPane.setContent(listViewRequestList);
        dialogPane.setPrefWidth(400);
        dialogPane.setPrefHeight(400);

        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(messageField.getScene().getWindow());
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void startChatAction(ActionEvent actionEvent) {


        List<String> chooseFriends = new ArrayList<>();
        chooseFriends.add(currentUser.getPhoneNumber());
        listViewFriendList.getSelectionModel().getSelectedItems().forEach((e) -> {
            chooseFriends.add(e.getPhoneNumber());
            System.err.println(e.getPhoneNumber());
        });
        boolean createChat = true;
        if (chooseFriends.size() == 2) {
            List<ChatEntitiy> chats = chatBoxesMap.keySet().stream().filter((e -> e.getParticipantsPhoneNumbers().size() == 2)).collect(Collectors.toList());

            for (ChatEntitiy chatEntitiy : chats) {
                if (chooseFriends.get(1).equals(getReceiverPhones(chatEntitiy.getParticipantsPhoneNumbers()))) {
                    createChat = false;
                    tabPane.getSelectionModel().selectPrevious();
                    break;
                }
            }
            if (createChat) {
                ChatEntitiy createdEntity = new ChatEntitiy(0, chooseFriends, null);
                try {

                    createdEntity = ClientProxy.getInstance().initiateChat(createdEntity);
                    if (createdEntity != null) {

                        createChatLayout(createdEntity);
                        tabPane.getSelectionModel().selectPrevious();
                    }

                } catch (RemoteException es) {
                    ServerOfflineHandler.handle("Sorry, Cannot continue your request :(");
                }
            }
        } else {

            ChatEntitiy createdEntity = new ChatEntitiy(0, chooseFriends, null);
            try {

                createdEntity = ClientProxy.getInstance().initiateChat(createdEntity);
                if (createdEntity != null) {

                    createChatLayout(createdEntity);
                    tabPane.getSelectionModel().selectPrevious();
                }

            } catch (RemoteException e) {
                ServerOfflineHandler.handle("Sorry, Cannot continue your request :(");
            }
        }


    }

    public void createChatLayout(ChatEntitiy createdEntity) {

        HBox hBox = StageCoordinator.getInstance().createChatLayout(createdEntity);
        chatsVbox.getChildren().add(hBox);
        VBox vBox = addChatToMap(createdEntity);
        vBox.setStyle("-fx-background-color: #dcdcde");
        ScrollPane scrollPane = getScrollPane(vBox);
        spChatBoxes.getChildren().add(scrollPane);


        SimpleObjectProperty<MessageEntity> msgProperty = ChatManager.getInstance().createNewChatResponse(createdEntity.getId());

        msgProperty.addListener((obs, oldVal, newVal) -> {
            vBox.getChildren().add(new ChatBox(newVal));
            if (botToggle.isSelected()) {

                sendMessage(chatBot.getResponse(newVal.getMsgContent()), chatEntitiy);

            }
        });


        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            receiverName.setText(getReciversNames(createdEntity));
            messageField.setDisable(false);
            fileButton.setDisable(false);
            chatEntitiy = createdEntity;
            scrollPane.toFront();
        });
        receiverName.setText(getReciversNames(createdEntity));
        messageField.setDisable(false);
        scrollPane.toFront();
        fileButton.setDisable(false);
        chatEntitiy = createdEntity;
    }

    public void createChatLayout(SimpleObjectProperty<MessageEntity> messageEntity) {
        int chatId = messageEntity.get().getChatEntitiy().getId();
        try {
            Map<String, FriendEntity> participantsInGroupChat = ClientProxy.getInstance().loadParticipants(chatId, currentUser.getPhoneNumber());
            for (FriendEntity s : participantsInGroupChat.values()) {
                System.out.println(s.getPhoneNumber());
            }
            currentUser.getParticipantsInGroup().putAll(participantsInGroupChat);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        HBox hBox = StageCoordinator.getInstance().createChatLayout(messageEntity.get().getChatEntitiy());
        chatsVbox.getChildren().add(hBox);
        VBox vBox = addChatToMap(messageEntity.get().getChatEntitiy());

        vBox.setStyle("-fx-background-color: #dcdcde");
        ScrollPane scrollPane = getScrollPane(vBox);
        spChatBoxes.getChildren().add(scrollPane);
        vBox.getChildren().add(new ChatBox(messageEntity.get()));
        if (botToggle.isSelected()) {
            sendMessage(chatBot.getResponse(messageEntity.get().getMsgContent()), messageEntity.get().getChatEntitiy());
        }



        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            receiverName.setText(getReciversNames(messageEntity.get().getChatEntitiy()));
            messageField.setDisable(false);
            fileButton.setDisable(false);
            chatEntitiy = messageEntity.get().getChatEntitiy();
            scrollPane.toFront();

        });
        messageEntity.addListener((obs, oldVal, newVal) -> {
            vBox.getChildren().add(new ChatBox(newVal));
            if (botToggle.isSelected()) {
                sendMessage(chatBot.getResponse(newVal.getMsgContent()), messageEntity.get().getChatEntitiy());
            }
        });
    }

    private VBox addChatToMap(ChatEntitiy createdEntity) {
        VBox vBox;
        if (!chatBoxesMap.containsKey(createdEntity)) {
            vBox = new VBox();
            chatBoxesMap.put(createdEntity, vBox);
        } else {
            vBox = chatBoxesMap.get(createdEntity);
        }
        return vBox;
    }

    private ScrollPane getScrollPane(VBox vBox) {
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.vvalueProperty().bind(vBox.heightProperty());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }


    @FXML
    void SaveUpdateChanges(ActionEvent event) {
        try {

            boolean isValidDisplayName = false;
            boolean isValidEmailAddress = false;
            boolean isValidDateOfBirth = false;
            boolean isValidBio = false;

            //display name validation
            if ( tFDisplayName.getText().isBlank()) {
                appNotifications.getInstance().errorBox("Display name can't be Empty ", "InValid Display Name!");
                return;
            } else {
                currentUser.setDisplayName(tFDisplayName.getText());
                isValidDisplayName = true;
            }

            //check that we have a valid email
            String emailValue = tFEmailAddress.getText();
            boolean validEmail = SignUpController.isValidEmail(emailValue);
            if (!validEmail ) {
                appNotifications.getInstance().errorBox( "Please enter a valid Email Address ", "Invalid Email Address!");
                return;
            } else {
                currentUser.setEmail(emailValue);
                isValidEmailAddress = true;
            }


//            validating date of birth

            LocalDate minAllowedDate = LocalDate.now().minusYears(12);
            LocalDate maxAllowedDate = LocalDate.now().minusYears(80);

            LocalDate dateOfBirth = DPDatePicker.getValue();
            if (dateOfBirth.isBefore(maxAllowedDate) || dateOfBirth.isAfter (minAllowedDate )) {
                appNotifications.getInstance().errorBox("Date of birth must be between ( 12 and 80) ", "Birth date is not Valid");
                return;

            }else{
                currentUser.setDOB(dateOfBirth.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            //validating the user BIo
//            if ( TABio.getText().isBlank()){
//                appNotifications.getInstance().errorBox("Bio can't be Empty ", "Bio is not Valid");
//            }else {
//                currentUser.setBio(TABio.getText());
//                isValidBio = true;
//            }

//            currentUser.setDisplayName(tFEmailAddress.getText()!=null?tFEmailAddress.getText():currentUser.getDisplayName());
//            currentUser.setEmail(tFEmailAddress.getText() != null ? tFEmailAddress.getText() : currentUser.getEmail());
//            currentUser.setBio(TABio.getText() != null ? TABio.getText() : currentUser.getBio());
//            currentUser.setGender(cbGender.getSelectionModel().getSelectedItem() != null ? cbGender.getSelectionModel().getSelectedItem() : currentUser.getGender());
//            currentUser.setDOB(DPDatePicker.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
//            lblUserName.setText(tFDisplayName.getText());
            //set the date of birth = DPDatePicker.getValue;

            if (isValidDisplayName && isValidEmailAddress && isValidDateOfBirth ){

                ClientProxy.getInstance().update(currentUser);//update the user info in the database

                //showing the update completed successfully notification
                appNotifications.getInstance().okai("Information Updated Successfully", "Updated Information");
            }

        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Oops, Server isn't available.");
        } catch (SQLException e) {
           appNotifications.getInstance().errorBox("Please Validate your data","Invalid data");
        }
    }


    public void handleSignOut(Event event) {
        appNotifications.getInstance().cancel("Do you want to sign out?", "R U Leaving?", () -> {
            StageCoordinator.getInstance().switchToLoginScene();
            ConfigurationHandler.getInstance().clearPassword();
            ClientProxy.getInstance().disconnect(ClientServicesFactory.getClientServicesImp());
            ModelsFactory.getInstance().createNewUser();
        });
    }

    @FXML
    private void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        attachedFile = fileChooser.showOpenDialog(spChatBoxes.getScene().getWindow());
        if (attachedFile != null) {
            if (convertToMb(attachedFile.length()) > 64.0) {
                attachedFile = null;
                appNotifications.getInstance().errorBox("Maximum file size 64MB.", "File Size Reached");
            } else {
                Group container = new Group();
                HBox fileDialog = new HBox(10);
                fileDialog.setAlignment(Pos.CENTER);
                fileDialog.setPadding(new Insets(10));
                fileDialog.setStyle("-fx-background-color: #52ACA8; -fx-background-radius: 15px;");
                Label label = new Label(attachedFile.getName());
                label.setTextFill(Color.WHITE);
                HBox.setHgrow(label, Priority.ALWAYS);
                FontIcon icon = new FontIcon("mdi2c-close");
                icon.setIconColor(Color.WHITE);
                icon.setIconSize(20);
                icon.setWrappingWidth(25);
                icon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    chatControllersContainer.getChildren().remove(container);
                    attachedFile = null;
                });
                fileDialog.getChildren().addAll(label, icon);
                container.getChildren().add(fileDialog);
                chatControllersContainer.getChildren().clear();
                chatControllersContainer.getChildren().add(container);
            }
        }
    }

    private double convertToMb(long byteSize) {
        return byteSize / Math.pow(1024.0, 2.0);
    }


    private String getReciversNames(ChatEntitiy chatEntitiy) {
        List<String> participants = chatEntitiy.getParticipantsPhoneNumbers()
                .stream().filter((e) -> !e.equals(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()))
                .collect(Collectors.toList());
        StringBuilder receiverNames = new StringBuilder(FriendsManager.getInstance().getFriendName(participants.get(0)));
        for (int i = 1; i < participants.size(); i++) {
            receiverNames.append(", ");
            receiverNames.append(FriendsManager.getInstance().getFriendName(participants.get(i)));
        }
        return receiverNames.toString();
    }

    public void showNotification(String message) {

        String title = "LONGTALK CHAT";
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndWait();
    }




    public ObservableList<FriendEntity> getRequestList() {
        return requestLists;
    }

    public ObservableList<FriendEntity> getFriendsList() {
        return friendsList;
    }

    private String getReceiverPhones(List<String> participants) {

        return participants.stream().filter((e) -> !e.equals(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()))
                .collect(Collectors.toList()).get(0);

    }

    @FXML
    public void saveChat(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML","*.html"));
        File savedPath=fileChooser.showSaveDialog(chatsVbox.getScene().getWindow());
        if(chatEntitiy!=null){
            new SavingSession().saveChat(chatEntitiy.getId(),chatHistory.get(chatEntitiy.getId()),chatEntitiy.getParticipantsPhoneNumbers(),savedPath);
        }
    }
    public void ChangeUserPic(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png", "*.jpeg")
        );

        File selectedPhoto = chooser.showOpenDialog(btnChangeUserPic.getScene().getWindow());
        if(selectedPhoto !=null){
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(selectedPhoto))) {
                byte[] photoBytes = bufferedInputStream.readAllBytes();
                circleView.setFill(new ImagePattern(new Image(new ByteArrayInputStream(photoBytes))));
                currentUser.setUserPhoto(photoBytes);
                ClientProxy.getInstance().update(currentUser);
            } catch (IOException | SQLException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }else {
          appNotifications.getInstance().okai("No Photo Selected invalid, please insert Photo"," Insert Photo!!");
        }

    }
}

