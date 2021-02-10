package JETS.ClientServices;

import JETS.ui.helpers.ModelsFactory;
import Services.ClientServices;

import java.rmi.RemoteException;

public class ClientServicesImp implements ClientServices {

    @Override
    public String getPhoneNumber() throws RemoteException {
       return ModelsFactory.getInstance().getCurrentUser().getPhoneNumber();
    }
}
