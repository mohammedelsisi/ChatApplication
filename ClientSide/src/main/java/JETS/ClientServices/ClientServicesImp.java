package JETS.ClientServices;

import JETS.ui.helpers.ModelsFactory;
import Services.ClientServices;

import java.rmi.RemoteException;

public class ClientServicesImp implements ClientServices {

    private static ClientServices clientServices;

    public static ClientServices getInstatnce (){
        if (clientServices == null){
            clientServices = new ClientServicesImp();
        }
        return clientServices;
    }

    private ClientServicesImp (){

    }

    @Override
    public String getPhoneNumber() throws RemoteException {
       return ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
    }
}
