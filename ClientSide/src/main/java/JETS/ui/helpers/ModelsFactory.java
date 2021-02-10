package JETS.ui.helpers;

import JETS.ClientMain;
import Models.CurrentUser;
import Services.DAOInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class ModelsFactory {

    private static final ModelsFactory instance = new ModelsFactory();

    private CurrentUser currentUser;
//    private DAOInterface<CurrentUser> userDAO;
    private ModelsFactory () {

    }

    public static ModelsFactory getInstance() {
        return instance;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }
    public CurrentUser register(CurrentUser user){

        try {
            currentUser=  ClientMain.userDAO.create(user);
        }catch (RemoteException | SQLException e){
            e.printStackTrace();
        }
        return  currentUser;

    }
    public void signIn(String phoneNumber,String password){

    }
}
