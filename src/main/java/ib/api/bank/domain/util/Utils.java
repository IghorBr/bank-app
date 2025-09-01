package ib.api.bank.domain.util;


import java.security.SecureRandom;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {

    private Utils() {}

    private static final SecureRandom RNG = new SecureRandom();
    // cache simples de unicidade em memória (ex.: até persistir no DB)
    private static final Set<String> SEEN = ConcurrentHashMap.newKeySet();

    private static String next() {
        while (true) {
            String payload = String.format("%05d", RNG.nextInt(100_000)); // 00000..99999
            int dv = luhnCheckDigit(payload); // DV mod-10 (Luhn)
            String acc = payload + "-" + dv;
            // garante unicidade no processo; no banco, use UNIQUE
            if (SEEN.add(acc)) return acc;
        }
    }

    // Luhn (mod-10) no payload de 5 dígitos
    private static int luhnCheckDigit(String digits) {
        int sum = 0;
        boolean doubleIt = true; // começando da direita (penúltimo), dobra alternados
        for (int i = digits.length() - 1; i >= 0; i--) {
            int d = digits.charAt(i) - '0';
            if (doubleIt) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleIt = !doubleIt;
        }
        return (10 - (sum % 10)) % 10;
    }

    public static String getAccountNumber() {
        return next();
    }

    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            sb.append(RNG.nextInt(10));
        }
        String first15 = sb.toString();
        int checkDigit = luhnCheckDigit(first15);
        return first15 + checkDigit;
    }

    public static String generateCVV() {
        int cvv = RNG.nextInt(1000); // 0..999
        return String.format("%03d", cvv);
    }
}
