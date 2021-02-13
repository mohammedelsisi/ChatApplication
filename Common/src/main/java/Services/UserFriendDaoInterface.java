package Services;

import Models.FriendEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public interface UserFriendDaoInterface extends Remote {
    int SearchbyPhoneno (String MyPoneNumber,String FriendPhoneNo) throws SQLException , RemoteException;
    public List<FriendEntity> getFriendRequests(String myPhoneNumber) throws RemoteException,SQLException ;
    public  void deleteRequest(String myPhoneNumber,String rejectPhoneNumber) throws RemoteException;
    public  void acceptRequest(String myPhoneNumber,String phoneNumber) throws RemoteException;
    public List<FriendEntity> getFriendList(String myPhoneNumber) throws SQLException , RemoteException;
}
