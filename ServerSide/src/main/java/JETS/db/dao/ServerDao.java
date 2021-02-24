package JETS.db.dao;

import javax.sql.RowSet;
import java.sql.Connection;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class ServerDao {
    protected final Connection connection;
        private final String USERS_GENDERS = "select phone_number,gender,country from user";

    public ServerDao(Connection connection) {
        this.connection = connection;
    }

    public ResultSet getUsersInfo() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(USERS_GENDERS,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            return preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

}
