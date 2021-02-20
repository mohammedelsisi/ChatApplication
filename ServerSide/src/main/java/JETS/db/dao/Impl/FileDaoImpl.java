package JETS.db.dao.Impl;

import JETS.db.dao.FileDao;
import Models.FileEntity;
import com.mysql.cj.jdbc.Blob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileDaoImpl implements FileDao {

    private static final String INSERT = "INSERT INTO FILE(data, name, msg_id) VALUES (?,?,?);";
    private static final String RETRIEVE_DATA = "SELECT data FROM FILE WHERE name = ? and msg_id = ?;";
    private final Connection CONNECTION;

    public FileDaoImpl(Connection connection) {
        this.CONNECTION = connection;
    }

    @Override
    public void insertFile(FileEntity file, int messageId) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(INSERT)) {
            statement.setBytes(1, file.getData());
            statement.setString(2, file.getName());
            statement.setInt(3, messageId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public FileEntity getFileData(FileEntity file, int messageId) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(RETRIEVE_DATA)) {
            statement.setString(1, file.getName());
            statement.setInt(2, messageId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                file.setData(result.getBytes(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return file;
    }
}
