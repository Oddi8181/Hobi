package gio.hobist.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class MessageEncryption {

   private static KeyPair keys ;
   private static Cipher cipher;


    static{
        try {
            keys=load();
            cipher = Cipher.getInstance("RSA");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("failed to get instance for Message encription",e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    //M.G: method reads keys from file
    private static KeyPair load() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
       var privateKeyStr=new String(Files.readAllBytes(Paths.get("src/main/resources/NotDecryptionKeyDirectory!!/private.Key")));
       var publicKeyStr=new String(Files.readAllBytes(Paths.get("src/main/resources/NotDecryptionKeyDirectory!!/public.Key")));

        var keyFactory=KeyFactory.getInstance("RSA");

        byte[] publicKeyBytes= Base64.getDecoder().decode(publicKeyStr);
        var publicKey=keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        byte[] privateKeyBytes= Base64.getDecoder().decode(privateKeyStr);
        var privateKey=keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        return new KeyPair(publicKey,privateKey);
    }

    public static byte[] encrypt(String message) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE,keys.getPublic());
        var encryptedMessage=cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encode(encryptedMessage);//M.G: This format fits in db
    }

    public static String decrypt(byte[] message) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE,keys.getPrivate());
        var encryptedMessage=Base64.getDecoder().decode(message);
        return new String(cipher.doFinal(encryptedMessage));
    }
}
