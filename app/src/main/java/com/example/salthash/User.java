package com.example.salthash;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
    private String username;
    private byte[] password;
    private byte[] salt;

    public User(String user, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        username = user;
        salt = new byte[20];
        setPassword(pass);
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        password = hash(pass, true);
    }

    public byte[] getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

    private byte[] hash(String pass, boolean isNewPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(salt == null || isNewPassword) {
            SecureRandom random = new SecureRandom();
            byte[] newSalt = new byte[20];
            //random.nextBytes(newSalt); //Didn't work for this project
            String chars = "abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()><?\":{}+_=-][';/.,']ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random rng = new Random();
            for(byte b : newSalt)
                b = (byte) chars.charAt(rng.nextInt(chars.length()));
            salt = newSalt;
            System.out.println(new String(salt));
        }

        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();

    }

    public static byte[] hash(byte[] salt, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }

    public boolean authenticate(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        return Arrays.equals(hash, password);
    }

    public static int findUser(ArrayList<User> users, String user) {
        for(int i = 0; i < users.size(); i++)
            if(users.get(i).getUsername().equals(user))
                return i;
        return -1;
    }
}
