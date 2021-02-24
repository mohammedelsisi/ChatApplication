package Services;

import Models.FriendEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public interface UserFriendDaoInterface extends Remote {
    int SearchbyPhoneno(String MyPoneNumber, String FriendPhoneNo) throws SQLException, RemoteException;

    List<FriendEntity> getFriendRequests(String myPhoneNumber) throws RemoteException, SQLException;

    void deleteRequest(String myPhoneNumber, String rejectPhoneNumber) throws RemoteException;

    void acceptRequest(String myPhoneNumber, String phoneNumber) throws RemoteException;

    List<FriendEntity> getFriendList(String myPhoneNumber) throws SQLException, RemoteException;

    void InsertInUserFriend(String MyPoneNumber, String FriendPhoneNo) throws SQLException, RemoteException;
}
