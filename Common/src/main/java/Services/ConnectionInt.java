package Services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionInt extends Remote {

    boolean registerAsConnected (ClientServices client) throws RemoteException;
    boolean disconnect (ClientServices client) throws RemoteException;
}
