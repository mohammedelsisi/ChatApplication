package JETS.ui.controllers;

import JETS.ClientMain;
import javafx.beans.value.ChangeListener;
import JETS.ui.helpers.ChatManager;
import JETS.ui.helpers.ModelsFactory;
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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;
import java.util.ArrayList;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

public class ChatController implements Initializable {

    public static ObservableList<FriendEntity> requestLists = FXCollections.observableArrayList();
    public static ObservableList<FriendEntity> friendsList =FXCollections.observableArrayList();
    public static TreeView<FriendEntity> treeViewFriends = new TreeView<>();
    public static TreeItem<FriendEntity> root = new TreeItem<FriendEntity>(new FriendEntity("Contacts"));
    public static TreeItem<FriendEntity> available = new TreeItem<>(new FriendEntity("Available"));
    private final Map<Integer, VBox> chatBoxesMap = new HashMap<>();
    public JFXTextArea messageField;
    @FXML
    public VBox contacts;
    @FXML
    public ComboBox statusComboBox;
    private Text textHolder = new Text();
    private double oldMessageFieldHigh;
    @FXML
    private ImageView profilePicImageView;
    public GridPane grid = new GridPane();
    public static Label invalidYourself;
    ListView<FriendEntity> listViewRequestList;
    ListView<FriendEntity> listViewFriendList=new ListView<>();
    public Dialog dialog = new Dialog();
    public Alert alert;
    public VBox chatsVbox;
    ChatEntitiy chatEntitiy;
    CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();
    ListView<FriendEntity> listView;
    int currentIdx;
    @FXML
    private StackPane spChatBoxes;
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
<<<<<<< HEAD
        String phone = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();

        loadRequestList();
        loadFriendList();



            listViewRequestList = new ListView(requestLists);
            listViewRequestList.setCellFactory(new Callback<ListView<FriendEntity>, ListCell<FriendEntity>>() {
                @Override
                public ListCell<FriendEntity> call(ListView<FriendEntity> friendListListView) {
                        ListCell<FriendEntity> cell=new ListCell<>(){
                        Button acceptButton=new Button("Accept");
                        Button rejectButton=new Button("Reject");
                        @Override
                        protected void updateItem(FriendEntity friendEntity, boolean b) {
                            super.updateItem(friendEntity,b);
                            if(!b) {

                                try {
                                    HBox hBox=new HBox(10);


                                    rejectButton.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            try {
                                                ClientMain.chatting.refuseRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(),friendEntity.getPhoneNumber());
                                                requestLists.remove(friendEntity);
                                            }catch (RemoteException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    acceptButton.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            try {
                                                ClientMain.chatting.acceptRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(),friendEntity.getPhoneNumber());
                                                requestLists.remove(friendEntity);
                                                friendsList.add(friendEntity);
                                            }catch (RemoteException e){
                                                e.printStackTrace();
                                            }
=======
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

                                    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(friendEntity.getUserPhoto())) {


                                    Image img=new Image(byteArrayInputStream);
                                    Circle circle = new Circle(25);
                                   circle.setFill(new ImagePattern(img));
                                   setGraphic(circle);

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
>>>>>>> SelectFriendsToMsg


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
<<<<<<< HEAD
                                    });
                                    Pane pane=new Pane();
                                    Image img;
                                    if(friendEntity.getPhotoPath()==null) {
                                        img = new Image(new FileInputStream("ClientSide/src/main/resources/Pics/ca.png"));
                                    }else{
                                        img=new Image(friendEntity.getPhotoPath().toURI().toURL().toExternalForm());
                                    }
                                    ImageView imageView=new ImageView(img);

                                    imageView.setFitWidth(50);
                                    imageView.setFitHeight(50);
                                    Label label=new Label();
                                    label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getPhoneNumber());
                                    label.setGraphic(imageView);
                                    hBox.getChildren().addAll(label,pane,acceptButton,rejectButton);
                                    hBox.setHgrow(pane, Priority.ALWAYS);
                                    hBox.setFillHeight(true);
                                    hBox.setAlignment(Pos.CENTER_LEFT);
                                    this.setGraphic(hBox);

                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }else {
                                setGraphic(null);
                                setText(null);
=======
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
>>>>>>> SelectFriendsToMsg
                            }
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
<<<<<<< HEAD
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
        SortedList<FriendEntity> sortedListFriends=new SortedList<>(friendsList, new Comparator<FriendEntity>() {
            @Override
            public int compare(FriendEntity o1, FriendEntity o2) {
                return o1.getStatus().compareTo(o2.getStatus());
            }
        });
         listViewFriendList.setItems(sortedListFriends);
         listViewFriendList.setCellFactory(new Callback<ListView<FriendEntity>, ListCell<FriendEntity>>() {
             HBox hBox=new HBox();
             @Override
             public ListCell<FriendEntity> call(ListView<FriendEntity> friendEntityListView) {


                 return new ListCell<>(){
                     @Override
                     protected void updateItem(FriendEntity friendEntity, boolean b) {
                         super.updateItem(friendEntity, b);
                         if(!b){
                             Label label = new Label();
                             Image img;
                             ImageView imageViewPic = new ImageView();
                             imageViewPic.setFitWidth(50);
                             imageViewPic.setFitHeight(50);
                             try {
                                 if (friendEntity.getPhotoPath() == null) {
                                     img = new Image(new FileInputStream("ClientSide/src/main/resources/Pics/ca.png"));
                                 } else {
                                     img = new Image(friendEntity.getPhotoPath().toURI().toURL().toExternalForm());
                                 }
                                 imageViewPic.setImage(img);
                             } catch (Exception ex) {
                                 ex.printStackTrace();
                             }
                             label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getStatus());
                             label.setGraphic(imageViewPic);

                             this.setGraphic(label);
                             //Image
                         }else{
                             setText(null);
                             setGraphic(null);
                         }
                     }
                 };

             }
         });
         contacts.getChildren().addAll(listViewFriendList);
         statusComboBox.setValue(ModelsFactory.getInstance().getCurrentUser().getStatusVal());
         statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
             @Override
             public void changed(ObservableValue observableValue, Object o, Object t1) {
                 // notify my friends during termination or sign out
                 ModelsFactory.getInstance().getCurrentUser().setStatus(t1.toString());
                 try {
                     System.out.println(t1.toString());
                     ClientMain.chatting.tellstatus(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), t1.toString());
                 }catch (RemoteException e){
                     e.printStackTrace();
                 }
             }
         });
    try {

        profilePicImageView.setImage(new Image(ModelsFactory.getInstance().getCurrentUser().getPhotoPath().toURI().toURL().toExternalForm()));
    }catch (Exception ex){
        ex.printStackTrace();
    }
=======
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


>>>>>>> SelectFriendsToMsg
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

<<<<<<< HEAD
                    //                 chatEntitiy.setParticipantsPhoneNumbers();
//                   .initiateChat(chatEntitiy);
                    messageField.setText("");
                }
                //sPane.vvalueProperty().bind(vBox.heightProperty());

//                    chatEntitiy.setParticipantsPhoneNumbers();
//                   .initiateChat(chatEntitiy);
                messageField.setText("");
            }
//                sPane.vvalueProperty().bind(vBox.heightProperty());


        } else {
            messageField.setText("");
=======
                    MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText().trim(), currentUser.getPhoneNumber());

                    vBox.getChildren().add(new ChatBox(msg));
                    ClientMain.chatServiceInt.sendMessage(msg);
                    messageField.clear();
                }

            } else {
                messageField.clear();
            }
>>>>>>> SelectFriendsToMsg
        }
    }

    }

    public void requestFriend() throws SQLException, RemoteException {
<<<<<<< HEAD
        
        invalidYourself = new Label();
        invalidYourself.setTextFill(Color.RED);
=======

        Dialog dialog = new Dialog();
>>>>>>> SelectFriendsToMsg
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
<<<<<<< HEAD
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

        }else if (myfriendNum.isEmpty()){
            invalidYourself.setText(" Please enter a valid Mobile No.");
            System.out.println("  Please enter a valid Mobile Number");
        }
        else {
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
=======
        String myFriendphoneNo = text1.getText();

        if (resultOfAddFriend.get() == buttonTypeOk) {
            AddFriend(myFriendphoneNo);
        }


>>>>>>> SelectFriendsToMsg
    }


    @FXML
    public void requestsHandle() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        DialogPane dialogPane = alert.getDialogPane();
<<<<<<< HEAD



        dialogPane.setContent(listViewRequestList);
=======
        dialogPane.setContent(listView);
>>>>>>> SelectFriendsToMsg
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
<<<<<<< HEAD
            requestLists.setAll(ClientMain.chatting.getFriendRequests(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));
=======
            requestLists.setAll((ArrayList) ClientMain.userFriendDaoInterface.getFriendRequests(currentUser.getPhoneNumber()));
>>>>>>> SelectFriendsToMsg

        } catch (RemoteException e) {
            e.printStackTrace();
<<<<<<< HEAD
=======
        } catch (SQLException e) {
            //e.printStackTrace();
            requestLists.clear();
>>>>>>> SelectFriendsToMsg
        }
    }

    public void loadFriendList() {
        try {

<<<<<<< HEAD

            friendsList.addAll(ClientMain.chatting.getFriends(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));
            for (FriendEntity friendEntity:friendsList){
                ModelsFactory.getInstance().getCurrentUser().getFriends().put(friendEntity.getPhoneNumber(),friendEntity);
               System.out.println(friendEntity.getDisplayName()+":"+friendEntity.getStatus());

            }

        }catch (RemoteException e){
            e.printStackTrace();
        }
    }


}
=======
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
}
>>>>>>> SelectFriendsToMsg
