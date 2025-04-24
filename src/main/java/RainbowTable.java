import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RainbowTable {


    private static String hashFunctionMD5(String input) {
        try {
            // Eine Instanz des MD5 Algorithmus erhalten
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Input mit MD5 hashen
            byte[] hashedValueInBytes = md.digest(input.getBytes());

            // Gehashter Wert in Hexidezimal Darstellung umwandeln
            String hashedValue = String.format("%032x", new BigInteger(1, hashedValueInBytes));
            return hashedValue;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
