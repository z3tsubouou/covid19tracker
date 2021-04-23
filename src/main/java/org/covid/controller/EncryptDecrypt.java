package org.covid.controller;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 *Нууц үгийн нууцалж тайлж унших
 */
public class EncryptDecrypt {
    private static SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES";

    /**
     *Нууц үгийн нууцлах төрлийг бэлтгэх
     * @param myKey Key parameter-ээр авна
     */
    public void prepareSecreteKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     *Нууц үгийг нууцлах
     * @param strToEncrypt нууцлаг string
     * @param secret нууцлах түлхүүр үг
     * @return нууцалсан үгийг эсвэл хоосон утга буцаана
     */
    public String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    /**
     *Нууц үгийг тайлж унших
     * @param strToDecrypt тайлах string
     * @param secret тайлах түлхүүр үг
     * @return тайлсан үгийг эсвэл хоосон утга буцаана
     */
    public String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }

}