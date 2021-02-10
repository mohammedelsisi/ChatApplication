package Services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientServices extends Remote {
    String getPhoneNumber() throws RemoteException;
}
