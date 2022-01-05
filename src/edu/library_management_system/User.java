package edu.library_management_system;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class User {
    String name, type, passwordHash, username;
    int id;
    static int currentId = 0;

    public User(String name, String type, String password) {
        this.name = name;
        this.type = type;
        this.id = ++currentId;
        this.username = name + "@" + id;
        try {
            this.passwordHash = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String getPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("Sha-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger num = new BigInteger(1, hash);
        StringBuilder sb = new StringBuilder(num.toString(16));
        while (sb.length() < 32) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}
