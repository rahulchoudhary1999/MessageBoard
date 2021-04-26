package com.thinking.machines.msgboard.utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import com.thinking.machines.msgboard.dao.*;
/*
Don't waste your time on thinking or writing about Encryption/Decryption
Look for java AES encryption/decryption Example
*/
public class EncryptionUtility
{
private static SecretKeySpec secretKey;
private static byte[] key;
private EncryptionUtility()
{
}

public static void setKey(String myKey) throws DAOException
{
MessageDigest sha = null;
try 
{
key = myKey.getBytes("UTF-8");
sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 16); 
secretKey = new SecretKeySpec(key, "AES");
} catch (NoSuchAlgorithmException e) 
{
e.printStackTrace();
} 
catch (UnsupportedEncodingException e) 
{
e.printStackTrace();
}
}

public static String getKey() throws DAOException
{
String key=UUID.randomUUID().toString(); //write code to generate secret key/salt key
return key;
}
public static String encrypt(String strToEncrypt, String secret) throws DAOException
{
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
            throw new DAOException("Unable to encrypt");
        }
        
}

public static String decrypt(String strToDecrypt, String secret) throws DAOException
{
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
            throw new DAOException("Unable to decrypt");
        }
        
    
}
}