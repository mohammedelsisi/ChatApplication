package JETS.net;

import JETS.ClientDemo;
import JETS.ui.helpers.ConfigurationHandler;
import JETS.ui.helpers.ModelsFactory;
import JETS.ui.helpers.StageCoordinator;
import JETS.ui.helpers.appNotifications;
import Models.*;
import Services.*;

import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    private static String serverIP;
    public static String getServerIp(){
        return serverIP;
    }
    public static void  setServerIP(String ss){
        serverIP =ss;
    }
    private ClientProxy() {
        try {
            registry = LocateRegistry.getRegistry(ClientDemo.ipOfserver,9999);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ClientProxy getInstance() {
        return clientProxy;
    }


    @Override
    public ChatEntitiy initiateChat(ChatEntitiy chatEntitiy) throws RemoteException {
        ChatEntitiy chat = null;
        try {
            if (chatDao == null) {
                chatDao = (ChatDao) registry.lookup("ChatDao");
            }
            chat = chatDao.initiateChat(chatEntitiy);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return chat;
    }

    @Override
    public void insertParticipant(String phoneNumber, long chatId) throws RemoteException {
        try {
            if (chatDao == null) {
                chatDao = (ChatDao) registry.lookup("ChatDao");
            }
            chatDao.insertParticipant(phoneNumber, chatId);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
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
    public void sendMessage(MessageEntity messageEntity)  {
        try {
            if (chatServiceInt == null) {
                chatServiceInt = (ChatServiceInt) registry.lookup("ChatService");
            }
            chatServiceInt.sendMessage(messageEntity);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
    }


    @Override
    public FileEntity getFileData(FileEntity file, int messageId) throws RemoteException {
        FileEntity fileEntity = null;
        try {
            if (fileService == null) {
                fileService = (FileService) registry.lookup("FileService");
            }
            fileEntity = fileService.getFileData(file, messageId);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return fileEntity;
    }


    @Override
    public boolean registerAsConnected(ClientServices client) throws RemoteException {
        try {
            if (connectionInt == null) {
                connectionInt = (ConnectionInt) registry.lookup("ConnectionService");
            }
        } catch (NotBoundException | ConnectException e) {
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
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        } catch (RemoteException e) {
            ServerOfflineHandler.handle("Sorry, cannot continue your request.\ntry to login later.");
        }
        return false;
    }


    @Override
    public CurrentUser update(CurrentUser dto) throws SQLException, RemoteException {
        CurrentUser user = null;
        try {
            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
            user = userDAO.update(dto);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return user;
    }

    @Override
    public CurrentUser create(CurrentUser dto) throws SQLException {
        try {
            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
            return userDAO.create(dto);
        } catch (NotBoundException | RemoteException e) {
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
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return userDAO.delete(phoneNumber);
    }

    @Override
    public List<FriendEntity> getFriends(String PhoneNumber) throws RemoteException {
        List<FriendEntity> friends = null;
        try {
            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
            friends = chatting.getFriends(PhoneNumber);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return friends;
    }

    @Override
    public CurrentUser findByPhoneAndPassword(LoginEntity l) {
        try {
            if (userDAO == null) {
                userDAO = (UserDao) registry.lookup("UserRegistrationService");
            }
            return userDAO.findByPhoneAndPassword(l);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Server Is Down");
        }
    }

    @Override
    public List<FriendEntity> getFriendRequests(String myPhoneNumber) throws RemoteException {
        List<FriendEntity> friends = null;
        try {
            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
            friends = chatting.getFriendRequests(myPhoneNumber);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return friends;
    }

    @Override
    public int sendRequest(String senderPhoneNumber, String receiverPhoneNumber) throws RemoteException {
        try {
            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
        return chatting.sendRequest(senderPhoneNumber, receiverPhoneNumber);
    }

    @Override
    public void acceptRequest(String myphoneNumber, String acceptedphoneNumber) throws RemoteException {
        try {
            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
            chatting.acceptRequest(myphoneNumber, acceptedphoneNumber);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
    }

    @Override
    public void refuseRequest(String myphoneNumber, String rejectedphoneNumber) throws RemoteException {
        try {
            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
            chatting.refuseRequest(myphoneNumber, rejectedphoneNumber);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
    }

    @Override
    public void tellStatus(String phoneNumber, String status) throws RemoteException {
        try {
            if (chatting == null) {
                chatting = (Chatting) registry.lookup("ChattingService");
            }
            chatting.tellStatus(phoneNumber, status);
        } catch (NotBoundException e) {
            ServerOfflineHandler.handle("Oops! service not available right now.\ntry to login later.");
        } catch (ConnectException e) {
            ServerOfflineHandler.handle("Oops! server is down right now.\ntry to login later.");
        }
    }
}
