package JETS.ui.helpers;

import Models.CurrentUser;
import Services.DAOInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class ModelsFactory {

    private static final ModelsFactory instance = new ModelsFactory();

    private CurrentUser currentUser;
    private DAOInterface<CurrentUser> userDAO;
    private ModelsFactory () {
        try {
            Registry registry = LocateRegistry.getRegistry(1234);
            DAOInterface<CurrentUser> userDAO = (DAOInterface<CurrentUser>)registry.lookup("DAOService");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ModelsFactory getInstance() {
        return instance;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }
    public void register(CurrentUser user){
        try {
           currentUser=userDAO.create(user);
        }catch (RemoteException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void signIn(String phoneNumber,String password){

    }
}
