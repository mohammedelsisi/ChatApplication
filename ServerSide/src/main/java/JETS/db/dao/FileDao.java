package JETS.db.dao;

import Models.FileEntity;

public interface FileDao {
    void insertFile(FileEntity file, int messageId);
    FileEntity getFileData(FileEntity file, int messageId);
}
