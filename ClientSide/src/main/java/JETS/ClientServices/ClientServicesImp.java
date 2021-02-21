package JETS.ClientServices;

import JETS.ui.controllers.ChatController;
import JETS.ui.helpers.ChatManager;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import Models.MessageEntity;
import Services.ClientServices;
import javafx.application.Platform;

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
        ChatController a = StageCoordinator.getInstance().getScenes().get("Chat").getLoader().getController();
            a.showNotification(s);
//        Image img = new Image(getClass().getResource("/Pics/annimg.jpg").toString());
//        Notifications notification = Notifications.create()
//                .owner(StageCoordinator.getInstance().getScenes().get("Chat").getScene().getWindow())
//                .title("Announcement")
//                .text(s)
//                .darkStyle()
//                .graphic(new ImageView(img))
//                .position(Pos.BOTTOM_RIGHT);
//        Platform.runLater(() -> {
//            notification.show();
//        });
    }
}
