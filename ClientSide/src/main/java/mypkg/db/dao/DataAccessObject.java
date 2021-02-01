package mypkg.db.dao;

import mypkg.db.dto.DataTransferObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public abstract class DataAccessObject<T extends DataTransferObject> {

    protected final Connection connection;
    protected final static String selection = "SELECT * FROM ";



    public DataAccessObject(Connection connection) {
        super();
        this.connection = connection;
    }

    public abstract T findById(long id);



    public abstract T update(T dto);

    public abstract T create(T dto) throws SQLException;

    public abstract void delete(long id);

    public int getLastVal(String tableName) {
        int key = 0;
        String sql = selection + tableName;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(sql);
            rs.last();
            key = rs.getInt(1);
            return key;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty (String tableName){
        boolean flag = true;
        String sql = selection + tableName;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(sql);
           rs.beforeFirst();
//            System.out.println(rs.next());
           return (!rs.next());

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return flag;
        }
    }
        public int getFirstVal(String tableName) {
            int key = -1;
            String sql = selection + tableName;
            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY)) {
                ResultSet rs = statement.executeQuery(sql);
                rs.first();
                key = rs.getInt(1);
                return key;
            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
                System.err.println(e.getMessage());
            }
            return key;

    }
}


