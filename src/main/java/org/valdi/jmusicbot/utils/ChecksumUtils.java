package org.valdi.jmusicbot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtils {

    public static boolean checkFileHash(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        return file.getName().equals(getFileChecksum(file, algorithm));
    }

    public static String getFileChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        // Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        return getChecksum(fis, algorithm);
    }

    public static String getChecksum(InputStream is, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        // Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        // Read file data and update in message digest
        while ((bytesCount = is.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        // close the stream; We don't need it now.
        is.close();

        // Get the hash's bytes
        byte[] bytes = digest.digest();

        // This bytes[] has bytes in decimal format;
        // Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        // return complete hash
        return sb.toString();
    }
}
