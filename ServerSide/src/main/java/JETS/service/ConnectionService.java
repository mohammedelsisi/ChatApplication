package JETS.service;

import Services.ClientServices;
import Services.ConnectionInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionService extends UnicastRemoteObject implements ConnectionInt,Connector {
    private final Map<String, ClientServices> connectedClients = new ConcurrentHashMap<>();

    public ConnectionService() throws RemoteException {
        super();
    }

    @Override
    public boolean registerAsConnected(ClientServices client) throws RemoteException {
        connectedClients.put(client.getPhoneNumber(), client);
        return true;
    }

    @Override
    public boolean disconnect(ClientServices client) throws RemoteException {
        boolean isSucceed = false;
        String clientPhoneNumber = client.getPhoneNumber();
        if (connectedClients.containsKey(clientPhoneNumber)) {
            connectedClients.remove(clientPhoneNumber);
            isSucceed = true;
        }
        return isSucceed;
    }

    @Override
    public boolean isConnected(String clientPhoneNumber) {
        return connectedClients.containsKey(clientPhoneNumber);
    }
}
