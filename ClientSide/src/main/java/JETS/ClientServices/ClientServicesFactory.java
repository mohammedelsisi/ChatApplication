package JETS.ClientServices;

import java.rmi.RemoteException;

public class ClientServicesFactory {

    private static ClientServicesImp clientServicesImp;

    private ClientServicesFactory() {
    }

    public static ClientServicesImp getClientServicesImp() {
        try{

            if (clientServicesImp == null) {
                clientServicesImp = new ClientServicesImp();
            }
        }catch (RemoteException e) {
            e.printStackTrace();
        }
        return clientServicesImp;
    }
}
