package JETS.db.dao;

import Models.CurrentUser;
import Services.DAOInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends UnicastRemoteObject implements DAOInterface<CurrentUser> {

    protected final Connection connection;
    private static final String GET_ID = "select id  from user where phone_number = ?";
    private static final String DELETE = "DELETE FROM user WHERE id = ?";
    protected final static String SELECT_ALL = "SELECT * FROM user ";
    private static final String INSERT = "INSERT INTO user (phone_number,password,Display_name, email,gender,country,age,bio) VALUES (?,?, ?, ?, ?,?,?,?)";
    private static final String GET_ONE = "SELECT * FROM user WHERE id=?";
    private static final String GET_Friends = "SELECT * FROM user where id = (select friend_id from user_friend where user_id = ?)";
    private static final String UPDATE = "UPDATE person SET phone_number = ?, password =?,Display_name=?, email = ?, gender = ?,country =?, age=?,bio =?,image =?,status=?  WHERE id = ?";

    public UserDao(Connection connection) throws RemoteException {
        this.connection = connection;
    }

    public long getIdByPhone(String phoneNumber) {
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ID)) {
            statement.setString(1, phoneNumber);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public CurrentUser findById(long id) throws SQLException, RemoteException {
        CurrentUser user = new CurrentUser();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1, id);
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
            statement.setString(9, dto.getUserPhoto());
            statement.setString(10, dto.getStatus());
            statement.setLong(11,dto.getId());
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
//            statement.setString(9, dto.getUserPhoto());
//            statement.setString(10, dto.getStatus());
            statement.executeUpdate();
            dto.setId(this.getIdByPhone(dto.getPhoneNumber()));
            return dto;
        }
    }



    public int delete(long id) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public List<CurrentUser> getFriends(long id) throws RemoteException, SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_Friends);) {
            preparedStatement.setLong(1, id);
            List<CurrentUser> friends = new ArrayList<>();
            ResultSet rs = preparedStatement.executeQuery();
            CurrentUser user = new CurrentUser();
            while (rs.next()) {
                friends.add(createUser(rs, user));
            }
            return friends;
        }
    }


    private CurrentUser createUser(ResultSet rs, CurrentUser user) throws SQLException {
        user.setId(rs.getLong("id"));
        user.setDisplayName(rs.getString("display_name"));
        user.setEmail(rs.getString("email"));
        user.setGender(rs.getString("gender"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAge(rs.getInt("age"));
        user.setBio(rs.getString("bio"));
        user.setStatus(rs.getString("status"));
        user.setUserPhoto(rs.getString("image"));  /*  not confirmed yet */
        return user;
    }
}