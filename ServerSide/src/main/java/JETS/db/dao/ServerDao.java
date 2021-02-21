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

    public JdbcRowSet getUsersInfo() {
        JdbcRowSet jdbcRowSet = null;
        try {
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            jdbcRowSet = rowSetFactory.createJdbcRowSet();
            jdbcRowSet.setUrl("jdbc:mysql://localhost:3306/chatschema");
            jdbcRowSet.setUsername("admin");
            jdbcRowSet.setPassword("iti41");
            jdbcRowSet.setCommand(USERS_GENDERS);
            jdbcRowSet.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return jdbcRowSet;
    }

}
