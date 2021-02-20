package JETS.service;

import JETS.db.DataSourceFactory;
import JETS.db.dao.Impl.FileDaoImpl;
import Models.FileEntity;
import Services.FileService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class FileServiceImpl extends UnicastRemoteObject implements FileService {
    public FileServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public FileEntity getFileData(FileEntity file, int messageId) throws RemoteException {
        try {
            return new FileDaoImpl(DataSourceFactory.getConnection()).getFileData(file, messageId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
