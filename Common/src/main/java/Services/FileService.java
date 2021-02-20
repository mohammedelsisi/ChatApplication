package Services;

import Models.FileEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileService extends Remote {
    FileEntity getFileData(FileEntity file, int messageId) throws RemoteException;
}
