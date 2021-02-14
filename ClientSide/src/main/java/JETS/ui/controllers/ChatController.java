package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.ChatEntitiy;
import Models.CurrentUser;
import Models.FriendEntity;
import Models.MessageEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.ButtonBar.ButtonData.OTHER;


public class ChatController implements Initializable {
    public static ObservableList<FriendEntity> requestLists = FXCollections.observableArrayList();
    public static List<FriendEntity> friendsList = new ArrayList<>();
    public static TreeView<FriendEntity> treeViewFriends = new TreeView<>();
    public static TreeItem<FriendEntity> root = new TreeItem<FriendEntity>(new FriendEntity("Contacts"));
    public static TreeItem<FriendEntity> available = new TreeItem<>(new FriendEntity("Available"));
    public JFXTextArea messageField;
    @FXML
    public VBox contacts;
    public VBox chatsVbox;
    ChatEntitiy chatEntitiy;
    CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
    ListView<FriendEntity> listView;
    @FXML
    private ImageView imgView;
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
//    public static TreeItem<FriendEntity> busy=new TreeItem("Busy");
//    public static TreeItem<FriendEntity> away=new TreeItem("Away");
//    public static TreeItem<FriendEntity> offline=new TreeItem("Offline");

    public static int AddFriend(String myfriendNum) throws SQLException, RemoteException {
        //String myphoneNumber = new CurrentUser().getPhoneNumber();
        String myphoneNumber = ("+201122344444");
        String myfriendPhoneNo = myfriendNum;

        int x = ClientMain.userFriendDaoInterface.SearchbyPhoneno(myphoneNumber, myfriendPhoneNo);
        System.out.println(myphoneNumber);
        System.out.println(myfriendPhoneNo);
        System.out.println(x);
        return x;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        circleView.setFill(new ImagePattern(new Image(new ByteArrayInputStream(currentUser.getUserPhoto()))));

        currentUser.userPhotoProperty().addListener((obs, oldVal, newVal) -> {

            circleView.setFill(new ImagePattern(new Image(new ByteArrayInputStream(newVal))));

        });


        String phone = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
        loadRequestList();
        loadFriendList();
        treeViewFriends.setShowRoot(false);
        treeViewFriends.setCellFactory(new Callback<TreeView<FriendEntity>, TreeCell<FriendEntity>>() {
            @Override
            public TreeCell<FriendEntity> call(TreeView<FriendEntity> friendEntityTreeView) {
                return new TreeCell<FriendEntity>() {
                    @Override
                    protected void updateItem(FriendEntity friendEntity, boolean b) {
                        super.updateItem(friendEntity, b);
                        if (!b) {

                            this.setText(friendEntity.getDisplayName());
                            if (friendEntity.getPhoneNumber() != null) {
                                try {

                                    Image img = new Image(new FileInputStream("RegPPic.png"));
                                    ImageView imageView = new ImageView(img);

                                    imageView.setFitWidth(50);
                                    imageView.setFitHeight(50);
                                    setGraphic(imageView);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        contacts.getChildren().add(treeViewFriends);


        listView = new ListView(requestLists);
        listView.setCellFactory(new Callback<ListView<FriendEntity>, ListCell<FriendEntity>>() {
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
                                            ClientMain.userFriendDaoInterface.deleteRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), friendEntity.getPhoneNumber());
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
                                            ClientMain.userFriendDaoInterface.acceptRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), friendEntity.getPhoneNumber());
                                            requestLists.remove(friendEntity);
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                Pane pane = new Pane();
                                Image img = new Image(new FileInputStream("RegPPic.png"));
                                ImageView imageView = new ImageView(img);

                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                Label label = new Label();
                                label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getPhoneNumber());
                                label.setGraphic(imageView);
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


    }

    public void sendMessage(KeyEvent keyEvent) throws RemoteException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!messageField.getText().isBlank()) {
                if (keyEvent.isAltDown()) {
                    messageField.appendText("\n");
                } else {
                    String text = messageField.getText().trim();
                    if (chatEntitiy.getId() == 0) {
                        chatEntitiy = ClientMain.chatDao.initiateChat(chatEntitiy);
                    }
                    MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText(), currentUser.getPhoneNumber());
                    messagesContainer.getChildren().add(new ChatBox(msg));
                    ClientMain.chatServiceInt.sendMessage(msg);
                    messageField.clear();
                }

            } else {
                messageField.clear();
            }
        }

    }

    public void requestFriend() throws SQLException, RemoteException {

        Dialog dialog = new Dialog();
        //  dialog.setTitle();
        dialog.setResizable(false);

        Label label1 = new Label("Enter Your Friend's Phone Number: ");
        TextField text1 = new TextField();
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Add Friend", OTHER);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.getDialogPane().getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> resultOfAddFriend = dialog.showAndWait();
        String myFriendphoneNo = text1.getText();

        if (resultOfAddFriend.get() == buttonTypeOk) {
            AddFriend(myFriendphoneNo);
        }


    }

    @FXML
    public void requestsHandle() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(listView);
        dialogPane.setPrefWidth(400);
        dialogPane.setPrefHeight(400);

        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(messageField.getScene().getWindow());
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.CLOSE);
        alert.showAndWait();

    }

    public void loadRequestList() {

        try {
            requestLists.setAll((ArrayList) ClientMain.userFriendDaoInterface.getFriendRequests(currentUser.getPhoneNumber()));

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            //e.printStackTrace();
            requestLists.clear();
        }
    }

    public void loadFriendList() {
        try {
            root.getChildren().addAll(available);

            friendsList.addAll((ArrayList) ClientMain.userFriendDaoInterface.getFriendList(currentUser.getPhoneNumber()));
            for (FriendEntity friendEntity : friendsList) {
                currentUser.getFriends().put(friendEntity.getPhoneNumber(), friendEntity);
                available.getChildren().add(new TreeItem<>(friendEntity));
                System.out.println(friendEntity.getDisplayName());

            }
            treeViewFriends.setRoot(root);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            // requestLists.clear();
        }
    }

    public void startChatAction(ActionEvent actionEvent) {
        List<String> choosenFriends = new ArrayList<>();
        choosenFriends.add(currentUser.getPhoneNumber());
        choosenFriends.add(treeViewFriends.getSelectionModel().getSelectedItem().getValue().getPhoneNumber());
        System.out.println(FriendsManager.getInstance().getFriendName(treeViewFriends.getSelectionModel().getSelectedItem().getValue().getPhoneNumber()));
        // treeViewFriends.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ChatEntitiy createdEntity = new ChatEntitiy(0, choosenFriends, null);
        HBox hBox = StageCoordinator.getInstance().createChatLayout(createdEntity);
        System.out.println("dasdas");
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            receiverName.setText(FriendsManager.getInstance().getFriendName(createdEntity.getParticipantsPhoneNumbers().get(1)));
            chatEntitiy = createdEntity;
        });

        chatsVbox.getChildren().add(hBox);
        tabPane.getSelectionModel().selectPrevious();

    }
}
