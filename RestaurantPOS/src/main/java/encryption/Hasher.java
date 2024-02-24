package encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    public static String hash(String message){
        try{
            // Hashes the message and return it
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(message.getBytes());
            BigInteger bigInteger = new BigInteger(1, messageDigest);
            return bigInteger.toString(16);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

}
