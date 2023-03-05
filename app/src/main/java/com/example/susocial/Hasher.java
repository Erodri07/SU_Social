package com.example.susocial;
import android.util.Base64;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Hasher {
    // Initializing the Salt and hashing algorithm
    private static final int SALT_LENGTH = 16;
    private static final String HASH_ALGORITHM = "SHA-256";

    // Method to hash a plaintext password
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Generate a random salt value
        byte[] salt = generateSalt();
        byte[] hash = generateHash(password, salt);
        // Concatenate the salt and hash values and encode them as a Base64 string
        return Base64.encodeToString(salt, Base64.DEFAULT) + ":" + Base64.encodeToString(hash, Base64.DEFAULT);
    }

    //
// Method to verify a plaintext password against a hashed password
    public static boolean verifyPassword(String password, String hashedPassword) throws NoSuchAlgorithmException {
        // Split the hashed password into the salt and hash values
        String[] parts = hashedPassword.split(":");
        byte[] salt = Base64.decode(parts[0], Base64.DEFAULT);
        byte[] expectedHash = Base64.decode(parts[1], Base64.DEFAULT);
        // Generate a hash value for the plaintext password using the salt
        byte[] actualHash = generateHash(password, salt);
        // Compare the expected and actual hash values using a MessageDigest method
        return MessageDigest.isEqual(expectedHash, actualHash);
    }
    // Method to generate a random salt value
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    // Method to generate a hash value for a plaintext password using a salt
    private static byte[] generateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return hash;
    }

}