package JETS.db.dao;

import Services.UserFriendDaoInterface;

import javax.sql.RowSet;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFriendDao extends UnicastRemoteObject implements UserFriendDaoInterface {

     private static final String SearchByPhoneno =  " select phone_number from user where phone_number=?";
     private static final String AddFriend = "Insert into user_friend (user_phone_number , friend_number, friendship_status) Values (? , ?, ?)";
     private final Connection connection;

    public UserFriendDao(Connection connection) throws RemoteException {
        this.connection = connection;
    }

    public int  SearchbyPhoneno (String MyPoneNumber,String FriendPhoneNo){
        int find =0;
        try (PreparedStatement stmt = this.connection.prepareStatement(SearchByPhoneno) ; PreparedStatement stmt2 = this.connection.prepareStatement(AddFriend)){
            this.connection.setAutoCommit(false);
            stmt.setString(1, FriendPhoneNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                stmt2.setString(1,FriendPhoneNo);
                stmt2.setString(2,MyPoneNumber);
                stmt2.setString(3,"pending");
                stmt2.execute();
                return 1;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

}
