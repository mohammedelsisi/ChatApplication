package JETS.ui.controllers;

import JETS.ClientMain;
import Services.UserFriendDaoInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;

import JETS.ClientMain;
import Models.CurrentUser;
import Services.UserFriendDaoInterface;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import JETS.ui.helpers.ModelsFactory;
import Models.ChatEntitiy;
import Models.FriendEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonBar.ButtonData.OTHER;


public class ChatController implements Initializable {
    public JFXTextArea messageField;
    @FXML
    public VBox contacts;
    private Text textHolder = new Text();
    private double oldMessageFieldHigh;
    public GridPane grid = new GridPane();
    public static Label invalidYourself;
    List<String> list;

    public static ObservableList<FriendEntity> requestLists= FXCollections.observableArrayList();
    public static List<FriendEntity> friendsList =new ArrayList<>();;
    ListView<FriendEntity> listView ;
    public static TreeView<FriendEntity> treeViewFriends=new TreeView<>();


    public static TreeItem<FriendEntity> root=new TreeItem<FriendEntity>(new FriendEntity("Contacts"));
    public static TreeItem<FriendEntity> available=new TreeItem<>(new FriendEntity("Available"));
//    public static TreeItem<FriendEntity> busy=new TreeItem("Busy");
//    public static TreeItem<FriendEntity> away=new TreeItem("Away");
//    public static TreeItem<FriendEntity> offline=new TreeItem("Offline");

    public Dialog dialog = new Dialog();
    public Alert alert;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String phone = ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();

//        list.add(phone);
//        list.add("+201012123112");
//        list.add("+201012123113");
        loadRequestList();
        loadFriendList();
        treeViewFriends.setShowRoot(false);
        treeViewFriends.setCellFactory(new Callback<TreeView<FriendEntity>, TreeCell<FriendEntity>>() {
            @Override
            public TreeCell<FriendEntity> call(TreeView<FriendEntity> friendEntityTreeView) {

                return new TreeCell<FriendEntity>(){
                    @Override
                    protected void updateItem(FriendEntity friendEntity, boolean b) {
                        super.updateItem(friendEntity,b);
                        if(!b ) {

                            this.setText(friendEntity.getDisplayName());
                            if(friendEntity.getPhoneNumber()!=null) {
                                try {

                                    Image img = new Image(new FileInputStream("ClientSide/src/main/resources/Pics/ca.png"));
                                    ImageView imageView = new ImageView(img);

                                    imageView.setFitWidth(50);
                                    imageView.setFitHeight(50);
                                    setGraphic(imageView);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
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
                                                ClientMain.userFriendDaoInterface.deleteRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(),friendEntity.getPhoneNumber());
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
                                                ClientMain.userFriendDaoInterface.acceptRequest(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(),friendEntity.getPhoneNumber());
                                                requestLists.remove(friendEntity);
                                            }catch (RemoteException e){
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                    Pane pane=new Pane();
                                    Image img=new Image(new FileInputStream("ClientSide/src/main/resources/Pics/ca.png"));
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

    public void sendMessage(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!messageField.getText().isBlank()) {
                if (keyEvent.isAltDown()) {
                    messageField.appendText("\n");
                } else {
                    String text = messageField.getText().trim();
                    /* has old chat ID */
                    ChatEntitiy chatEntitiy = new ChatEntitiy();

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
        }
    }


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

        }else if (myfriendNum.isEmpty()){
            invalidYourself.setText(" Please enter a valid Mobile No.");
            System.out.println("  Please enter a valid Mobile Number");
        }
        else {
            try {

                x = ClientMain.userFriendDaoInterface.SearchbyPhoneno(myphoneNumber, myfriendPhoneNo);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
    public void requestsHandle(){
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
    public static void loadRequestList(){
        try {
            requestLists.setAll((ArrayList)ClientMain.userFriendDaoInterface.getFriendRequests(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));

        }catch (RemoteException e){
            e.printStackTrace();
        }catch (SQLException e){
            //e.printStackTrace();
            requestLists.clear();
        }
    }
    public static void loadFriendList(){
        try {
            root.getChildren().addAll(available);

            friendsList.addAll((ArrayList)ClientMain.userFriendDaoInterface.getFriendList(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));
            for (FriendEntity friendEntity:friendsList){
                ModelsFactory.getInstance().getCurrentUser().getFriends().put(friendEntity.getPhoneNumber(),friendEntity);
                available.getChildren().add(new TreeItem<>(friendEntity));
               System.out.println(friendEntity.getDisplayName());

            }
            treeViewFriends.setRoot(root);
        }catch (RemoteException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
            // requestLists.clear();
        }
    }

}
