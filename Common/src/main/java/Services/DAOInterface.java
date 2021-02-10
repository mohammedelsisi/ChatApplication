package Services;
import Models.LoginEntity;
import Models.dto.DataTransferObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DAOInterface <T extends DataTransferObject> extends Remote {

    T findById(long id) throws SQLException, RemoteException;
    T update(T dto) throws SQLException,RemoteException;
    T create(T dto) throws  RemoteException, SQLException;
    int delete(long id) throws RemoteException;
//    boolean isEmpty() throws SQLException, RemoteException;
    List<T> getFriends (long id) throws RemoteException, SQLException;
    T findByPhoneAndPassword(LoginEntity l) throws RemoteException;

}
