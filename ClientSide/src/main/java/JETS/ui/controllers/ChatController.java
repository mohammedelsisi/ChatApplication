package JETS.ui.controllers;

import JETS.ClientMain;
import JETS.ui.helpers.ClientImp;
import Services.CallBack;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;


import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import JETS.ui.helpers.ModelsFactory;
import Models.ChatEntitiy;
import Models.FriendEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
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

import java.util.*;

import java.util.ArrayList;

import java.rmi.RemoteException;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.ButtonBar.ButtonData.OTHER;



import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonBar.ButtonData.OTHER;



public class ChatController implements Initializable {
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

    public static ObservableList<FriendEntity> requestLists= FXCollections.observableArrayList();

    public static ObservableList<FriendEntity> friendsList =FXCollections.observableArrayList();
    ListView<FriendEntity> listViewRequestList;
    ListView<FriendEntity> listViewFriendList=new ListView<>();



    public static TreeItem<FriendEntity> root=new TreeItem<FriendEntity>(new FriendEntity("Contacts"));
    public static TreeItem<FriendEntity> available=new TreeItem<>(new FriendEntity("Available"));


    public Dialog dialog = new Dialog();
    public Alert alert;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

                                        }
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
        SortedList<FriendEntity> sortedListFriends=new SortedList<>(friendsList, new Comparator<FriendEntity>() {
            @Override
            public int compare(FriendEntity o1, FriendEntity o2) {
                return o1.getStatusVal().compareTo(o2.getStatusVal());
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
                             label.setText(friendEntity.getDisplayName() + "\n" + friendEntity.getStatusVal());
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
                 ModelsFactory.getInstance().getCurrentUser().setStatusVal(t1.toString());
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
    public void requestsHandle(){
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
    public static void loadRequestList(){
        try {
            requestLists.setAll(ClientMain.chatting.getFriendRequests(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));

        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    public static void loadFriendList(){
        try {


            friendsList.addAll(ClientMain.chatting.getFriends(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));
            for (FriendEntity friendEntity:friendsList){
                ModelsFactory.getInstance().getCurrentUser().getFriends().put(friendEntity.getPhoneNumber(),friendEntity);
               System.out.println(friendEntity.getDisplayName()+":"+friendEntity.getStatusVal());

            }

        }catch (RemoteException e){
            e.printStackTrace();
        }
    }


}