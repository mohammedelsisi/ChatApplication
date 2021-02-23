package JETS.net;

import JETS.ClientServices.ClientServicesFactory;
import Models.*;
import Services.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class ClientProxy implements UserDao, ConnectionInt, ChatServiceInt, ChatDao, Chatting, FileService {

    public static Chatting chatting;
    private static final ClientProxy clientProxy = new ClientProxy();
    private UserDao userDAO;
    private ConnectionInt connectionInt;
    private ChatServiceInt chatServiceInt;
    private ChatDao chatDao;
    private FileService fileService;
    private Registry registry;

    private ClientProxy() {
        try {
            registry = LocateRegistry.getRegistry(5555);
        } catch (RemoteException  e) {
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
        return fileService.getFileData(file,messageId);
    }


    @Override
    public boolean registerAsConnected(ClientServices client) throws RemoteException {
        try {
//            UnicastRemoteObject.exportObject(ClientServicesFactory.getClientServicesImp(),5555);
            if (connectionInt == null) {
                connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return connectionInt.registerAsConnected(client);
    }

    @Override
    public boolean disconnect(ClientServices client)  {
        try {

            if (connectionInt == null) {
                connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            }
            return connectionInt.disconnect(client);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
      return false;
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
    public CurrentUser create(CurrentUser dto) throws RemoteException, SQLException {
        try {

            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return userDAO.create(dto);
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
    public CurrentUser findByPhoneAndPassword(LoginEntity l) throws RemoteException {
        try {
            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
        } catch (NotBoundException e) {
           throw new RuntimeException("Server Is Down");
        }
        return userDAO.findByPhoneAndPassword(l);
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
        return chatting.sendRequest(senderPhoneNumber,receiverPhoneNumber);
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
       chatting.acceptRequest(myphoneNumber,acceptedphoneNumber);
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
        chatting.refuseRequest(myphoneNumber,rejectedphoneNumber);
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
       chatting.tellStatus(phoneNumber,status);
    }
}