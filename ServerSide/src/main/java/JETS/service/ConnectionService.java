package JETS.service;

import Services.ClientServices;
import Services.ConnectionInt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionService extends UnicastRemoteObject implements ConnectionInt {
    public ObservableMap<String, ClientServices> getConnectedClients() {
        return connectedClients;
    }

    private final ObservableMap<String, ClientServices> connectedClients = FXCollections.observableHashMap();

    public ConnectionService() throws RemoteException {
        super();
    }

    @Override
    public synchronized boolean registerAsConnected(ClientServices client) throws RemoteException {
        connectedClients.put(client.getPhoneNumber(), client);
        return true;
    }

    @Override
    public synchronized boolean disconnect(ClientServices client) throws RemoteException {
        boolean isSucceed = false;
        System.out.println(client.getPhoneNumber() + " disconnected");
        String clientPhoneNumber = client.getPhoneNumber();
        if (connectedClients.containsKey(clientPhoneNumber)) {
            connectedClients.remove(clientPhoneNumber);
            isSucceed = true;
        }
        return isSucceed;
    }
    @Override
    public synchronized boolean isConnected(String clientPhoneNumber) {

        return connectedClients.containsKey(clientPhoneNumber);
    }


    public ClientServices getClientService(String clientPhoneNumber) {
      return connectedClients.get(clientPhoneNumber);
    }
}
