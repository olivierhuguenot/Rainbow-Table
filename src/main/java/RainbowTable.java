import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTable {
    private static final char[] Z = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int PASSWORD_LENGTH = 7;
    private static final int CHAIN_AMOUNT = 2000;
    private static final int CHAIN_ELEMENTS = 2000;

    public static void main(String[] args) {
        Map<String, String> rainbowTable = generateRainbowTable();
    }

    public static Map<String, String> generateRainbowTable() {
        // HashMap in der wir jeweils den Start und Endwert einer Kette speichern
        HashMap<String, String> rainbowTable = new HashMap<>();

        // Erstes mögliches Passwort
        String password = "0000000";

        // 2000 Ketten generieren
        for(int i = 0; i < CHAIN_AMOUNT; i++) {
            String firstPassword = password;
            String lastPassword = firstPassword;

            // 2000 Kettenelemente generieren
            for(int j = 0; j < CHAIN_ELEMENTS; i++) {
                String hashedValue = hashFunctionMD5(lastPassword);
                lastPassword = reduceFunction(hashedValue);
            }

            // Das Erste und Letzte Passwort einer Kette wird in den Rainbowtable gespeichert
            // Da wir das Letzte Element benötigen zum Nachschlagen, speichern wir es als Key
            rainbowTable.put(lastPassword, firstPassword);

            // Für die nächste Kette benötigen wir das nächste Passwort (+1)
            password = nextPassword(password);
        }

        return rainbowTable;
    }


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


    private static String reduceFunction(String hashedInput) {
        // TODO
        return null;
    }


    private static String nextPassword(String password) {
        // TODO
        return null;
    }
}
