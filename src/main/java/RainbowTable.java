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
        String wantedHash = "1d56a37fb6b08aa709fe90e12ca59e12";
        String password = findHash(wantedHash, rainbowTable);
        System.out.println("Wanted password: " + password);
    }

    public static Map<String, String> generateRainbowTable() {
        // HashMap in der wir jeweils den Start und Endwert einer Kette speichern. Achtung bei uns sind die keys die Enwerte und die values die Startwerte
        HashMap<String, String> rainbowTable = new HashMap<>();

        // Erstes mögliches Passwort
        String password = "0000000";

        // 2000 Ketten generieren
        for (int i = 0; i < CHAIN_AMOUNT; i++) {
            String firstPassword = password;
            String lastPassword = firstPassword;
            /* Gibt die Kettennummern auf der Konsole aus
            if (i == 0) {
                System.out.println("-------- CHAIN: " + i+1 + " --------");
            }
             */

            // 2000 Kettenelemente pro Kette generieren
            for (int j = 0; j < CHAIN_ELEMENTS; j++) {
                String hashedValue = hashFunctionMD5(lastPassword);
                lastPassword = reduceFunction(hashedValue, j);
                /* Gibt Kettenelemennummer, Hashwert und Reduktionswert auf Konsole aus
                if (i == 0) {
                    System.out.println("Chain-Element: " + j+1);
                    System.out.println("Hashed Value: " + hashedValue);
                    System.out.println("Reduced Password: " + lastPassword);
                }
                 */
                /*
                if(j % 100 == 0) {
                    System.out.println("Element: " + j);
                    System.out.println("Hashed Value: " + hashedValue);
                    System.out.println("Reduced Password: " + lastPassword);
                }
                 */
            }

            // Das Erste und Letzte Passwort einer Kette wird in den Rainbowtable gespeichert
            // Da wir das Letzte Element benötigen zum Nachschlagen, speichern wir es als Key
            rainbowTable.put(lastPassword, firstPassword);

            // Für die nächste Kette benötigen wir das nächste Passwort (+1)
            password = nextPassword(password);
            /*
            System.out.println("Next Password: " + password);
             */
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

    private static String reduceFunction(String hashedInput, int round) {
        // Z ist die Menge der Ziffern 1-9 und alle Kleinbuchstaben -> |Z| = 36
        // L ist die Länge der Passwörter also 7
        // H entspricht hashedInput
        // Stufe entspricht der Variable round

        // Stufe wird zum Hashwert addiert
        BigInteger h = new BigInteger(hashedInput, 16);
        h = h.add(BigInteger.valueOf(round));

        // Jedes Zeichen des Outputs wird durch mod und div mit PASSWORD_LENGTH ermittelt und dann in umgekehrter Reihenfolge zurückgegeben
        char[] resultString = new char[PASSWORD_LENGTH];

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int ri = h.mod(BigInteger.valueOf(Z.length)).intValue();
            resultString[i] = Z[ri];
            h = h.divide(BigInteger.valueOf(Z.length));
        }
        String temp = new String(resultString);
        String reversed = new StringBuilder(temp).reverse().toString();
        return reversed;
    }

    private static String nextPassword(String password) {
        char[] pwCharArray = password.toCharArray();

        for (int i = pwCharArray.length - 1; i >= 0; i--) {
            // Index des aktuellen Chars im Z Array finden
            int index = indexOfCharinArrayZ(pwCharArray[i]);
            // Fall es nicht der letzte Char im Alphabet ist, wird er inkrementiert und ersetzt
            // 0000000, 0000001, ..., 0000009, 000000a, 000000b, ..., 000000z, 0000010
            if (index < Z.length - 1) {
                pwCharArray[i] = Z[index + 1];
                return new String(pwCharArray);
            } else {
                pwCharArray[i] = Z[0];
            }
        }
        return new String(pwCharArray);
    }

    private static int indexOfCharinArrayZ(char c) {
        for (int i = 0; i < Z.length; i++) {
            if (Z[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static String findHash(String hashValue, Map<String, String> rainbowTable) {
        // Beginn bei letzter Stufe (1999) dann rückwärts jede Stufe durchgehen
        int lastColumn = CHAIN_ELEMENTS - 1;
        for (int i = lastColumn; i >= 0; i--) {
            String h = hashValue;
            // Iteriere von aktueller Stufe bis zum Ende der Kette
            for (int j = i; j < CHAIN_ELEMENTS; j++) {
                String r = reduceFunction(h, j);
                // Prüfe ob der reduzierte Hash in der Tabelle als Endwert vorkommt
                if (rainbowTable.containsKey(r)) {
                    // Holen des dazugehörigen Startwerts
                    String startValue = rainbowTable.get(r);
                    String reduceValue = startValue;
                    // Kette von Startwert aus rekonstruieren und mit hashValue prüfen und wenn korrekt gesuchtes Passwort zurückgeben
                    for (int k = 0; k < CHAIN_ELEMENTS; k++) {
                        String tempHash = hashFunctionMD5(reduceValue);
                        if (tempHash.equals(hashValue)) {
                            return reduceValue;
                        }
                        reduceValue = reduceFunction(tempHash, k);
                    }
                }
                // Wenn r kein Endwert ist wird gehasht für die nächste Stufe
                h = hashFunctionMD5(r);
            }
        }
        return "Kein Passwort zum Hashwert: " + hashValue + " gefunden.";
    }
}