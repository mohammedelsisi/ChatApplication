package JETS.ClientServices;

import JETS.ui.helpers.ChatManager;
import JETS.ui.helpers.ModelsFactory;
import Models.MessageEntity;
import Services.ClientServices;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientServicesImp extends UnicastRemoteObject implements ClientServices {

    protected ClientServicesImp() throws RemoteException {
    }

    @Override
    public String getPhoneNumber() throws RemoteException {
       return ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
    }

    @Override
    public void receive(MessageEntity messageEntity) throws RemoteException {
        ChatManager.getInstance().receiveResponse(messageEntity);

    }

    @Override
    public void ReceiveAnnounc(String s) throws RemoteException {
        Image img = null;
//        try {
//            img = new Image(new FileInputStream("E:\\ITI\\WARNINGSemiFinal\\ChatApplication\\ServerSide\\src\\main\\resources\\views\\annimg.jpg"));
//            Notifications notification = Notifications.create();
//            notification.title("Announcement");
//            notification.text(s);
//            notification.graphic(new ImageView(img));
//            notification.darkStyle();
//            notification.position(Pos.BASELINE_RIGHT);
//            Platform.runLater(()->{
//
//                notification.show();
//            });

        System.out.println(s);


    }
}
