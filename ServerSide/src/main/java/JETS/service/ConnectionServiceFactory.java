package JETS.service;

import java.rmi.RemoteException;

public class ConnectionServiceFactory {
//    private static
    private static ConnectionService connectionService;
    public static ConnectionService getConnectionService ()  {
        if (connectionService == null) {
            try {
                connectionService = new ConnectionService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return connectionService;
    }
}
