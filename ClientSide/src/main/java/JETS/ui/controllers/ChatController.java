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

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.ChatEntitiy;
import Models.CurrentUser;
import Models.FriendEntity;
import Models.MessageEntity;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Callback;

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
    public JFXTextArea messageField;
    @FXML
    public VBox contacts;
    private Text textHolder = new Text();
    private double oldMessageFieldHigh;
    List<String> list;
    ChatEntitiy chatEntitiy;
    CurrentUser currentUser = ModelsFactory.getInstance().getCurrentUser();

    public static ObservableList<FriendEntity> requestLists = FXCollections.observableArrayList();
    public static List<FriendEntity> friendsList = new ArrayList<>();
    ListView<FriendEntity> listView;
    public static TreeView<FriendEntity> treeViewFriends = new TreeView<>();


    public static TreeItem<FriendEntity> root = new TreeItem<FriendEntity>(new FriendEntity("Contacts"));
    public static TreeItem<FriendEntity> available = new TreeItem<>(new FriendEntity("Available"));
//    public static TreeItem<FriendEntity> busy=new TreeItem("Busy");
//    public static TreeItem<FriendEntity> away=new TreeItem("Away");
//    public static TreeItem<FriendEntity> offline=new TreeItem("Offline");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

                            textHolder.textProperty().bind(messageField.textProperty());
                            textHolder.setWrappingWidth(600);
                            textHolder.layoutBoundsProperty().addListener((observableValue, oldValue, newValue) -> {
                                if (oldMessageFieldHigh != newValue.getHeight() && newValue.getHeight() < 100) {
                                    oldMessageFieldHigh = newValue.getHeight();
                                    messageField.setPrefHeight(Math.max(oldMessageFieldHigh + 15, 50));
                                }
                            });

                        }
                    }
                };
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

                        MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText(), currentUser.getPhoneNumber());
                        ClientMain.chatServiceInt.sendMessage(msg);
                    } else {
                        MessageEntity msg = new MessageEntity(chatEntitiy, messageField.getText(), currentUser.getPhoneNumber());
                        ClientMain.chatServiceInt.sendMessage(msg);

                    }

                    messageField.setText("");
                }

            } else {
                messageField.setText("");
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

    public static void loadRequestList() {
        try {
            requestLists.setAll((ArrayList) ClientMain.userFriendDaoInterface.getFriendRequests(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            //e.printStackTrace();
            requestLists.clear();
        }
    }

    public static void loadFriendList() {
        try {
            root.getChildren().addAll(available);

            friendsList.addAll((ArrayList) ClientMain.userFriendDaoInterface.getFriendList(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()));
            for (FriendEntity friendEntity : friendsList) {
                ModelsFactory.getInstance().getCurrentUser().getFriends().put(friendEntity.getPhoneNumber(), friendEntity);
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

}
