package Services;
import Models.LoginEntity;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DAOInterface <T extends Serializable> extends Remote {

    T findById(String phoneNumber) throws SQLException, RemoteException;
    T update(T dto) throws SQLException,RemoteException;
    T create(T dto) throws  RemoteException, SQLException;
    int delete(String phoneNumber) throws RemoteException;
//    boolean isEmpty() throws SQLException, RemoteException;
    List<T> getFriends (String phoneNumber) throws RemoteException, SQLException;
    T findByPhoneAndPassword(LoginEntity l) throws RemoteException;

}
