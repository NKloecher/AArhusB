package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class User extends Person {
    private String username;
    private byte[] passwordHash;
    private byte[] salt = new byte[16];
    private Permission permission;

    public User(String name, String username, String password) {
    	super(name);
    	
        this.username = username;

        setPassword(password);
    }
    
    

    public byte[] getHash(String password){
        try{
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return f.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException ex){
            throw new RuntimeException("Error getting hashing algorithm");
        } catch (InvalidKeySpecException ex){
            throw new RuntimeException("Error generating hash");
        }

    }

    public boolean checkPassword(String password){
        return Arrays.equals(passwordHash, this.getHash(password));
    }
    
    public String getUsername() {
    	return username;
    }
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setPassword(String password) {
        new Random().nextBytes(salt);
        passwordHash = this.getHash(password);
    }
    
    @Override
    public String toString() {
    	return name + " - " + username;
    }
}
