package JETS.ui.helpers;

import Models.CurrentUser;

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
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }
    public void setCurrentUser( CurrentUser currentUser) {
        this.currentUser=currentUser;
    }



    public void signIn(String phoneNumber,String password){
    }

}
