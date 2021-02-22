package Services;
import Models.CurrentUser;
import Models.LoginEntity;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface UserDao extends Remote {

   CurrentUser update( CurrentUser dto) throws SQLException,RemoteException;
    CurrentUser create(CurrentUser dto) throws  RemoteException, SQLException;
    int delete(String phoneNumber) throws RemoteException;
//    boolean isEmpty() throws SQLException, RemoteException;

    CurrentUser findByPhoneAndPassword(LoginEntity l) throws RemoteException;

}
