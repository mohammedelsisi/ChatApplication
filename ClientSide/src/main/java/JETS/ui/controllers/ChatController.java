package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.ui.helpers.ChatManager;
import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.ChatEntitiy;
import Models.CurrentUser;
import Models.FriendEntity;
import Models.MessageEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;


public class ChatController implements Initializable {
    public static Label invalidYourself;
    public static ObservableList<FriendEntity> requestLists = FXCollections.observableArrayList();
    public static ObservableList<FriendEntity> friendsList = FXCollections.observableArrayList();
    public static TreeItem<FriendEntity> root = new TreeItem<FriendEntity>(new FriendEntity("Contacts"));
    public static TreeItem<FriendEntity> available = new TreeItem<>(new FriendEntity("Available"));
    private final Map<Integer, VBox> chatBoxesMap = new HashMap<>();
    public JFXTextArea messageField;
    @FXML
    public VBox contacts;
    public VBox chatsVbox;
    @FXML
    public ComboBox statusComboBox;
    public GridPane grid = new GridPane();
    public Dialog dialog = new Dialog();
    public Alert alert;
    ChatEntitiy chatEntitiy;
    CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
    int currentIdx;
    ListView<FriendEntity> listViewRequestList;
    ListView<FriendEntity> listViewFriendList = new ListView<>();
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
    private double oldMessageFieldHigh;
    private Screen screen;

    public static void loadRequestList() {
        try {
            requestLists.setAll(ClientMain.chatting.getFriendRequests(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void loadFriendList() {
        try {


            friendsList.addAll(ClientMain.chatting.getFriends(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));
            for (FriendEntity friendEntity : friendsList) {
                ModelsFactory.getInstance().getCurrentUser().getFriends().put(friendEntity.getPhoneNumber(), friendEntity);
                System.out.println(friendEntity.getDisplayName() + ":" + friendEntity.getStatus());

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screen = Screen.getPrimary();
        showNotification("Helloooooooo");
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
                                            ClientMain.chatting.refuseRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), friendEntity.getPhoneNumber());
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
                                            ClientMain.chatting.acceptRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), friendEntity.getPhoneNumber());
                                            requestLists.remove(friendEntity);
                                            friendsList.add(friendEntity);
                                            currentUser.getFriends().put(friendEntity.getPhoneNumber(), friendEntity);
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                Pane pane = new Pane();
                                Image img;
                                Circle imageCircle = new Circle(50);
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
                            Circle imageCircle = new Circle(20);
                            Label label = new Label();
                            Circle status = new Circle(5);
                            if (friendEntity.getStatus().equals("AVAILABLE")) {
                                status.setFill(Color.LIME);
                            } else if (friendEntity.getStatus().equals("AWAY")) {
                                status.setFill(Color.GREENYELLOW);
                            } else if (friendEntity.getStatus().equals("BUSY")) {
                                status.setFill(Color.RED);
                            } else {
                                status.setFill(Color.GRAY);
                            }
                            byte[] userImage = null;
                            if ((userImage = friendEntity.getUserPhoto()) != null) {
                                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userImage)) {
                                    imageCircle.setFill(new ImagePattern(new Image(byteArrayInputStream)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getStatus());
                            label.setGraphic(imageCircle);
                            hBox.getChildren().addAll(label, status);


                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setFillHeight(true);
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
        statusComboBox.setValue(ModelsFactory.getInstance().getCurrentUser().getStatus());
        statusComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // notify my friends during termination or sign out
            ModelsFactory.getInstance().getCurrentUser().setStatus(newValue.toString());
            try {
                System.out.println(newValue.toString());
                ClientMain.chatting.tellstatus(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), newValue.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(currentUser.getUserPhoto())) {
            circleView.setFill(new ImagePattern(new Image(byteArrayInputStream)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(KeyEvent keyEvent) throws RemoteException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!messageField.getText().isBlank()) {
                if (keyEvent.isAltDown()) {
                    messageField.appendText("\n");
                } else {
                    VBox vBox = addChatToMap(currentIdx);

                    if (chatEntitiy.getId() == 0) {
                        chatEntitiy = ClientMain.chatDao.initiateChat(chatEntitiy);
                        SimpleObjectProperty<MessageEntity> msgProperty = ChatManager.getInstance().createNewChatResponse(chatEntitiy.getId());
                        VBox vBox2 = addChatToMap(currentIdx);
                        msgProperty.addListener((obs, old, newval) -> {
                            vBox2.getChildren().add(new ChatBox(newval));
                            System.out.println("testtest");
                        });
                    }

                    MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText().trim(), currentUser.getPhoneNumber());

                    vBox.getChildren().add(new ChatBox(msg));
                    ClientMain.chatServiceInt.sendMessage(msg);
                    messageField.clear();
                }

            } else {
                messageField.clear();
            }
        }

    }

    @FXML
    public void requestFriend() throws SQLException, RemoteException {

        invalidYourself = new Label();
        invalidYourself.setTextFill(Color.RED);
        //  dialog.setTitle();
        dialog.setResizable(false);

        Label label1 = new Label("Enter Your Friend's Phone Number: ");
        TextField text1 = new TextField();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(invalidYourself, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Add Friend", OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().setAll(buttonTypeOk);
        dialog.setOnCloseRequest(event -> {
            String myFriendphoneNo = text1.getText();
            if (AddFriend(myFriendphoneNo) == 0) {
                event.consume();
            }
        });
        Optional<ButtonType> resultOfAddFriend = dialog.showAndWait();
        Button btn = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        // dialog.setOnCloseRequest();
//
//        if (resultOfAddFriend.get()==buttonTypeOk)
//        {
//            AddFriend(myFriendphoneNo);
//        }
    }

    public int AddFriend(String myfriendNum) {
        int x = 0;
        String myphoneNumber = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
        String myfriendPhoneNo = myfriendNum;

        if (myphoneNumber.equals(myfriendPhoneNo)) {
            invalidYourself.setText(" Please enter a valid Mobile No.");
            System.out.println("You cannot add your account");

        } else if (myfriendNum.isEmpty()) {
            invalidYourself.setText(" Please enter a valid Mobile No.");
            System.out.println("  Please enter a valid Mobile Number");
        } else {
            try {
                x = ClientMain.chatting.sendRequest(myphoneNumber, myfriendPhoneNo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            System.out.println(myphoneNumber);
            System.out.println(myfriendPhoneNo);
            System.out.println(x);

        }
        return x;
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


        List<String> chosenFriends = new ArrayList<>();
        chosenFriends.add(currentUser.getPhoneNumber());
        chosenFriends.add(listViewFriendList.getSelectionModel().getSelectedItem().getPhoneNumber());
        System.out.println(FriendsManager.getInstance().getFriendName(listViewFriendList.getSelectionModel().getSelectedItem().getPhoneNumber()));
        // treeViewFriends.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ChatEntitiy createdEntity = new ChatEntitiy(0, chosenFriends, null);
        createChatLayout(createdEntity);
        tabPane.getSelectionModel().selectPrevious();

    }

    public void createChatLayout(ChatEntitiy createdEntity) {

        HBox hBox = StageCoordinator.getInstance().createChatLayout(createdEntity);
        chatsVbox.getChildren().add(hBox);
        int idx = chatsVbox.getChildren().lastIndexOf(hBox);
        VBox vBox = addChatToMap(idx);
        vBox.setStyle("-fx-background-color: #dcdcde");
        ScrollPane scrollPane = getScrollPane(vBox);
        spChatBoxes.getChildren().add(scrollPane);

        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            receiverName.setText(FriendsManager.getInstance().getFriendName(createdEntity.getParticipantsPhoneNumbers().get(1)));
            messageField.setDisable(false);
            chatEntitiy = createdEntity;
            scrollPane.toFront();
            currentIdx = idx;
        });
    }

    public void createChatLayout(SimpleObjectProperty<MessageEntity> messageEntity) {

        HBox hBox = StageCoordinator.getInstance().createChatLayout(messageEntity.get().getChatEntitiy());
        chatsVbox.getChildren().add(hBox);
        int idx = chatsVbox.getChildren().lastIndexOf(hBox);
        VBox vBox = addChatToMap(idx);
        vBox.setStyle("-fx-background-color: #dcdcde");
        ScrollPane scrollPane = getScrollPane(vBox);
        spChatBoxes.getChildren().add(scrollPane);
        vBox.getChildren().add(new ChatBox(messageEntity.get()));
        System.out.println(messageEntity.get().getChatEntitiy().getParticipantsPhoneNumbers());
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            receiverName.setText(FriendsManager.getInstance().getFriendName(messageEntity.get().getChatEntitiy().getParticipantsPhoneNumbers().get(0)));
            messageField.setDisable(false);
            chatEntitiy = messageEntity.get().getChatEntitiy();
            scrollPane.toFront();
            currentIdx = idx;
        });
        messageEntity.addListener((obs, old, newval) -> {

            vBox.getChildren().add(new ChatBox(newval));
            System.out.println("dada");
        });
    }

    private VBox addChatToMap(int id) {
        VBox vBox;
        if (!chatBoxesMap.containsKey(id)) {
            vBox = new VBox();
            chatBoxesMap.put(id, vBox);
        } else {
            vBox = chatBoxesMap.get(id);
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

    public void showNotification(String message) {
        Image img = new Image(getClass().getResource("/Pics/annimg.jpg").toString());
        Notifications.create()
                .owner(screen)
                .title("Announcement")
                .text(message)
                .darkStyle()
                .graphic(new ImageView(img))
                .position(Pos.BOTTOM_RIGHT)
                .show();
    }

}