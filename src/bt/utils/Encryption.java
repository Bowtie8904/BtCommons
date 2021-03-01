package bt.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Encryption
{
    private static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }

    private static IvParameterSpec generateIv()
    {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Encrypts the given input with the password and salt.
     *
     * @param input    The text that should be encrypted.
     * @param password The password used for the key.
     * @param salt     The salt used for the key.
     * @return An array with 2 elements. THe first element will be the randomly generated IV, the second element will be the encrypted text.
     */
    public static String[] encrypt(String input, String password, String salt) throws NoSuchPaddingException, NoSuchAlgorithmException,
                                                                                      InvalidAlgorithmParameterException, InvalidKeyException,
                                                                                      BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException
    {
        IvParameterSpec iv = Encryption.generateIv();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,
                    Encryption.getKeyFromPassword(password, salt),
                    iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return new String[] { Base64.getEncoder().encodeToString(iv.getIV()),
                              Base64.getEncoder().encodeToString(cipherText)
        };
    }

    /**
     * Decrypts the given cypherText with the IV, password and salt.
     *
     * @param cipherText The text that should be decrypted.
     * @param iv         The IV that was used to encrypt the text.
     * @param password   The password for the key.
     * @param salt       The salt for the key.
     * @return The decrpyted text.
     */
    public static String decrypt(String cipherText, String iv, String password, String salt) throws NoSuchPaddingException, NoSuchAlgorithmException,
                                                                                                    InvalidAlgorithmParameterException, InvalidKeyException,
                                                                                                    BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                    Encryption.getKeyFromPassword(password, salt),
                    new IvParameterSpec(Base64.getDecoder().decode(iv)));
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }
}