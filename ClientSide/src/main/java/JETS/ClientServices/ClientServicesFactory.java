package JETS.ClientServices;

import java.rmi.RemoteException;

public class ClientServicesFactory {

    private static ClientServicesImp clientServicesImp;

    private ClientServicesFactory() {
    }

    public static ClientServicesImp getClientServicesImp() throws RemoteException {
        if (clientServicesImp == null)
            clientServicesImp = new ClientServicesImp();
        return clientServicesImp;
    }
}
