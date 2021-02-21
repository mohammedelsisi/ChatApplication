package JETS.security.hash;

import JETS.security.hash.Hash;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

class SHA3Hash implements Hash {

    @Override
    public String hash(char[] input) {
        if (input != null && input.length > 0) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA3-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] data = toBytes(input);
            byte[] hashed = digest.digest(data);
            //clearing data
            clearData(input, data);
            return bytesToHex(hashed);
        } else {
            throw new IllegalArgumentException("Argument must be not null or empty.");
        }
    }

    private byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        //clearing data
        clearData(charBuffer.array(), byteBuffer.array());
        return bytes;
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void clearData(char[] charData, byte[] byteData) {
        Arrays.fill(charData, '\u0000');
        Arrays.fill(byteData, (byte) 0);
    }
}
