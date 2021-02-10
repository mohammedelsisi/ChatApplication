package JETS.ui.controllers;

import Models.CurrentUser;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainChatController implements Initializable {

    CurrentUser currentUser;
    public Button ChStatus;
    public TextField tfDisplayName;
    public TextField tfStatus;


    public JFXComboBox cbStatus;
    public TextField tfDisplayNameresult;


    ObservableList statusList = FXCollections.observableArrayList("Online", "Offline", "Away", "Busy","Don't Disturb");
    ObservableList displayNames = FXCollections.observableArrayList();

    StringProperty statusProperty = new SimpleStringProperty("online");
    StringProperty disNameProperty = new SimpleStringProperty("user");

    public String getStatusProperty() {
        return statusProperty.get();
    }
    public StringProperty statusProperty() {
        return statusProperty;
    }
    public void setStatusProperty(String status) {
        this.statusProperty.set(status);
    }

    public String getDisNameProperty() {
        return disNameProperty.get();
    }
    public StringProperty disNamePropertyProperty() {
        return disNameProperty;
    }
    public void setDisNameProperty(String disNameProperty) {
        this.disNameProperty.set(disNameProperty);
    }

    public ObservableList getStatusList() {
        return statusList;
    }
    public ObservableList getDisplayNames() {
        return displayNames;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbStatus.setItems(statusList);
        setStatusProperty("online");
        cbStatus.setOnAction(e -> changeStatus());
        //binding text field property with our property.
        tfStatus.textProperty().bindBidirectional(currentUser.statusProperty());
        tfDisplayNameresult.textProperty().bindBidirectional(currentUser.displayNameProperty());
    }

    public void changeStatus() {
        //binding the current user status property to the  combobox selected item.
        currentUser.statusProperty().setValue(cbStatus.getValue().toString());
    }
    public void ChDisplayN() {
        //binding the current user display name property to the text field value.
        currentUser.displayNameProperty().setValue(tfDisplayName.getText());
    }
    public void changePic(ActionEvent event) {

    }

}
