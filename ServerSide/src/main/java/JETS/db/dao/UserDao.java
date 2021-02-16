package JETS.db.dao;


import Models.CurrentUser;
import Models.LoginEntity;
import Services.DAOInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends UnicastRemoteObject implements DAOInterface<CurrentUser> {

    protected final Connection connection;
    private static final String DELETE = "DELETE FROM user WHERE phone_number = ?";
    private static final String INSERT = "INSERT INTO user (phone_number,password,Display_name, email,gender,country,age,bio,image) VALUES (?,?, ?, ?, ?,?,?,?,?)";
    private static final String GET_ONE = "SELECT * FROM user WHERE phone_number=?";
    private static final String GET_Friends = "SELECT * FROM user where phone_number = (select friend_number from user_friend where user_phone_number = ?)";
    private static final String UPDATE = "UPDATE person SET  password =?,Display_name=?, email = ?, gender = ?,country =?, age=?,bio =?,image =?,status=?  WHERE phone_number = ?";
    private static final String GET_ONE_WITH_Pass = "SELECT * FROM user WHERE phone_number=? and password =?";
    private static final String UPDATE_USER_STATUS="UPDATE user SET STATUS=? where phone_number=?";
    public UserDao(Connection connection) throws RemoteException {
        this.connection = connection;
    }



    @Override
    public CurrentUser findById(String phoneNumber) throws SQLException, RemoteException {
        CurrentUser user = new CurrentUser();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setString(1, phoneNumber);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = createUser(rs, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public CurrentUser update(CurrentUser dto) throws SQLException, RemoteException {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE);) {
            statement.setString(1, dto.getPhoneNumber());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getDisplayName());
            statement.setString(4, dto.getEmail());
            statement.setString(5, dto.getGender());
            statement.setString(6, dto.getCountry());
            statement.setInt(7, dto.getAge());
            statement.setString(8, dto.getBio());
            statement.setBytes(9, dto.getUserPhoto());
            statement.setString(10, dto.getStatus());
            statement.executeUpdate();
            return dto;
        }
    }



    @Override
    public CurrentUser create(CurrentUser dto) throws RemoteException, SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT);) {
            statement.setString(1, dto.getPhoneNumber());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getDisplayName());
            statement.setString(4, dto.getEmail());
            statement.setString(5, dto.getGender());
            statement.setString(6, dto.getCountry());
            statement.setInt(7, dto.getAge());
            statement.setString(8, dto.getBio());
            statement.setBytes(9, dto.getUserPhoto());
//            statement.setString(10, dto.getStatus());
            statement.executeUpdate();
            return dto;
        }
    }



    public int delete(String phoneNumber) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE)) {
            statement.setString(1, phoneNumber);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public List<CurrentUser> getFriends(String phoneNumber) throws RemoteException, SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_Friends);) {
            preparedStatement.setString(1, phoneNumber);
            List<CurrentUser> friends = new ArrayList<>();
            ResultSet rs = preparedStatement.executeQuery();
            CurrentUser user = new CurrentUser();
            while (rs.next()) {
                friends.add(createUser(rs, user));
            }
            return friends;
        }
    }

    @Override
    public CurrentUser findByPhoneAndPassword(LoginEntity l) throws RemoteException {
        CurrentUser user = new CurrentUser();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_WITH_Pass);) {
           statement.setString(1,l.getPhoneNumber());
           statement.setString(2,l.getPassword());
            ResultSet rs = statement.executeQuery();

           if (rs.next()) {
                user = createUser(rs, user);
               return user;
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserStatus(String phoneNumber,String status){
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_USER_STATUS);) {
            statement.setString(1, status);
            statement.setString(2, phoneNumber);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private CurrentUser createUser(ResultSet rs, CurrentUser user) throws SQLException {
        user.setDisplayName(rs.getString("display_name"));
        user.setEmail(rs.getString("email"));
        user.setGender(rs.getString("gender"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAge(rs.getInt("age"));
        user.setBio(rs.getString("bio"));
        user.setStatus(rs.getString("status"));
        user.setUserPhoto(rs.getBytes("image"));
        return user;
    }
}