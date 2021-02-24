package JETS.net;

import JETS.ClientServices.ClientServicesFactory;
import JETS.ui.helpers.ModelsFactory;
import Models.*;
import Services.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ClientProxy implements UserDao, ConnectionInt, ChatServiceInt, ChatDao, Chatting, FileService {

    private static final ClientProxy clientProxy = new ClientProxy();
    public static Chatting chatting;
    private UserDao userDAO;
    private ConnectionInt connectionInt;
    private ChatServiceInt chatServiceInt;
    private ChatDao chatDao;
    private FileService fileService;
    private Registry registry;

    private ClientProxy() {
        try {
            registry = LocateRegistry.getRegistry(7979);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ClientProxy getInstance() {
        return clientProxy;
    }


    @Override
    public ChatEntitiy initiateChat(ChatEntitiy chatEntitiy) throws RemoteException {
        try {

            if (chatDao == null) {
                chatDao = (ChatDao) registry.lookup("ChatDao");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return chatDao.initiateChat(chatEntitiy);
    }

    @Override
    public void insertParticipant(String phoneNumber, long chatId) throws RemoteException {
        try {

            if (chatDao == null) {
                chatDao = (ChatDao) registry.lookup("ChatDao");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        chatDao.insertParticipant(phoneNumber, chatId);
    }

    @Override
    public Map<String, FriendEntity> loadParticipants(int chatId, String myPhoneNumber) throws RemoteException {
        try {
            if (chatDao == null) {
                chatDao = (ChatDao) registry.lookup("ChatDao");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return chatDao.loadParticipants(chatId,myPhoneNumber);
    }

    @Override
    public void sendMessage(MessageEntity messageEntity) throws RemoteException {
        try {

            if (chatServiceInt == null) {
                chatServiceInt = (ChatServiceInt) registry.lookup("ChatService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        chatServiceInt.sendMessage(messageEntity);
    }


    @Override
    public FileEntity getFileData(FileEntity file, int messageId) throws RemoteException {
        try {

            if (fileService == null) {
                fileService = (FileService) registry.lookup("FileService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return fileService.getFileData(file, messageId);
    }


    @Override
    public boolean registerAsConnected(ClientServices client) throws RemoteException {
        try {
            if (connectionInt == null) {
                connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        boolean connected = connectionInt.registerAsConnected(client);
        tellStatus(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), "AVAILABLE");
        return connected;
    }

    @Override
    public boolean disconnect(ClientServices client) {
        try {
            tellStatus(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber(), "OFFLINE");

            if (connectionInt == null) {
                connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            }
            return connectionInt.disconnect(client);

        } catch (NotBoundException | RemoteException s) {
            throw new RuntimeException("Cannot Reach Server");
        }

    }

    @Override
    public boolean isConnected(String clientPhoneNumber) {
        try {
            if (connectionInt == null) {
                connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            }
            return connectionInt.isConnected(clientPhoneNumber);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public CurrentUser update(CurrentUser dto) throws SQLException, RemoteException {
        try {

            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return userDAO.update(dto);
    }

    @Override
    public CurrentUser create(CurrentUser dto) throws SQLException {
        try {

            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
            return userDAO.create(dto);
        } catch (NotBoundException | RemoteException  e) {
            throw new RuntimeException("Sorry, You can't Register Now. Our Service is not available");
        }

    }

    @Override
    public int delete(String phoneNumber) throws RemoteException {
        try {

            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return userDAO.delete(phoneNumber);
    }

    @Override
    public List<FriendEntity> getFriends(String PhoneNumber) throws RemoteException {
        try {

            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return chatting.getFriends(PhoneNumber);
    }

    @Override
    public CurrentUser findByPhoneAndPassword(LoginEntity l) {
        try {
            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }

            return userDAO.findByPhoneAndPassword(l);
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException("Server Is Down");
        }
    }

    @Override
    public List<FriendEntity> getFriendRequests(String myPhoneNumber) throws RemoteException {
        try {

            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return chatting.getFriendRequests(myPhoneNumber);
    }

    @Override
    public int sendRequest(String senderPhoneNumber, String receiverPhoneNumber) throws RemoteException {
        try {

            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return chatting.sendRequest(senderPhoneNumber, receiverPhoneNumber);
    }

    @Override
    public void acceptRequest(String myphoneNumber, String acceptedphoneNumber) throws RemoteException {
        try {

            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        chatting.acceptRequest(myphoneNumber, acceptedphoneNumber);
    }

    @Override
    public void refuseRequest(String myphoneNumber, String rejectedphoneNumber) throws RemoteException {
        try {

            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        chatting.refuseRequest(myphoneNumber, rejectedphoneNumber);
    }

    @Override
    public void tellStatus(String phoneNumber, String status) throws RemoteException {
        try {

            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        chatting.tellStatus(phoneNumber, status);
    }
}
