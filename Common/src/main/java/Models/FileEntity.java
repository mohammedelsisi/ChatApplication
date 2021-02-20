package Models;

import java.io.Serializable;

public class FileEntity implements Serializable {
    private String name;
    private byte[] data;

    public FileEntity(String name) {
        this.name = name;
    }

    public FileEntity(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
