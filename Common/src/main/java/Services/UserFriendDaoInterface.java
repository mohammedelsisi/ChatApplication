package Services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;


public interface UserFriendDaoInterface extends Remote {
    int SearchbyPhoneno (String MyPoneNumber,String FriendPhoneNo) throws SQLException , RemoteException;
}
