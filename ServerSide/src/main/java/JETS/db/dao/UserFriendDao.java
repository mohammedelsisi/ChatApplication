package JETS.db.dao;

import Models.FriendEntity;
import Services.UserFriendDaoInterface;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFriendDao  implements UserFriendDaoInterface {

     private static final String SearchByPhoneno =  " select phone_number from user where phone_number=?";
     private static final String AddFriend = "Insert into user_friend (user_phone_number , friend_number, friendship_status) Values (? , ?, ?)";
     private static final String FRIENDS_REQUESTS="SELECT * FROM user_friend,user WHERE user.PHONE_NUMBER=user_friend.FRIEND_NUMBER AND "+
             " friendship_status='PENDING' AND user_phone_number= ?";
     private static final String FRIEND_LIST="SELECT * FROM user WHERE user.PHONE_NUMBER IN (SELECT user_phone_number from user_friend "+
             "where friend_number=? and friendship_status=\"ACCEPTED\" union select friend_number from user_friend "+
             "where user_phone_number=? and  friendship_status=\"ACCEPTED\")";
     private static final String DELETE_FROM_USER_FRIEND="DELETE FROM user_friend WHERE user_phone_number=? and friend_number=?";
    private static final String ACCEPT_USER_FRIEND="update user_friend SET friendship_status=\"ACCEPTED\" WHERE user_phone_number=? and friend_number=?";
    private static final String GET_ONE = "SELECT * FROM user WHERE phone_number=?";

    private final Connection connection;
    public UserFriendDao(Connection connection) throws RemoteException {
        this.connection = connection;
    }

    public int  SearchbyPhoneno (String MyPoneNumber,String FriendPhoneNo){

        try (PreparedStatement stmt = this.connection.prepareStatement(SearchByPhoneno)){
            stmt.setString(1, FriendPhoneNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                    InsertInUserFriend(MyPoneNumber, FriendPhoneNo);
                return 1;
            }

        } catch (SQLException throwables) {
           return 2;
        }

        return 0;
    }

    @Override
    public List<FriendEntity> getFriendRequests(String myPhoneNumber) throws RemoteException,SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FRIENDS_REQUESTS)) {
            preparedStatement.setString(1, myPhoneNumber);
            List<FriendEntity> requests = new ArrayList<>();
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                FriendEntity request = new FriendEntity();
                requests.add(createUser(rs, request));
            }
            return requests;
        }
    }

    public void InsertInUserFriend(String MyPoneNumber,String FriendPhoneNo) throws SQLException {
        try (PreparedStatement stmt2 = this.connection.prepareStatement(AddFriend)){
            stmt2.setString(1,FriendPhoneNo);
            stmt2.setString(2,MyPoneNumber);
            stmt2.setString(3,"pending");
            stmt2.execute();

        }
    }



    @Override
    public void deleteRequest(String myPhoneNumber,String rejectPhoneNumber) throws RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_FROM_USER_FRIEND)) {
            statement.setString(1, myPhoneNumber);
            statement.setString(2,rejectPhoneNumber);
             statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<FriendEntity> getFriendList(String myPhoneNumber) throws SQLException, RemoteException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FRIEND_LIST)) {
            preparedStatement.setString(1, myPhoneNumber);
            preparedStatement.setString(2, myPhoneNumber);
            List<FriendEntity> friends = new ArrayList<>();
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                FriendEntity request = new FriendEntity();
                friends.add(createUser(rs, request));
            }
            return friends;
        }
    }

    @Override
    public void acceptRequest(String myPhoneNumber,String acceptedPhoneNumber) throws RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(ACCEPT_USER_FRIEND);) {
            statement.setString(1,myPhoneNumber);
            statement.setString(2,acceptedPhoneNumber);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
   public FriendEntity findFriendByPhoneNumber(String searchByPhoneno) {
       FriendEntity user = new FriendEntity();
           try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {

               statement.setString(1, searchByPhoneno);
               ResultSet rs = statement.executeQuery();
               if (rs.next()) {
                   user = createUser(rs, user);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return user;
       }

    public FriendEntity createUser(ResultSet rs, FriendEntity request) throws SQLException {
            request.setDisplayName(rs.getString("display_name"));
            request.setPhoneNumber(rs.getString("phone_number"));
            request.setBio(rs.getString("bio"));

            request.setStatus(rs.getString("status"));
            request.setUserPhoto(rs.getBytes("image"));

            return request;
        }
}
