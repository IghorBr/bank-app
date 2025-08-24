package ib.api.bank.domain.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtils {

    private static final String ALG = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BITS = 128;         // 16 bytes
    private static final int IV_LEN = 12;                // 12 bytes recomendado
    private static final SecureRandom RNG = new SecureRandom();
    private static volatile SecretKey CACHED_KEY;

    private EncryptUtils() {}

    // Lê a chave da env var ou system prop uma vez (Base64 de 16/24/32 bytes)
    public static SecretKey getKey() {
        if (CACHED_KEY != null) return CACHED_KEY;

        String b64 = System.getProperty("app.enc.key");
        if (b64 == null || b64.isBlank()) {
            b64 = System.getenv("APP_ENC_KEY");
        }
        if (b64 == null || b64.isBlank()) {
            throw new IllegalStateException(
                    "Chave não configurada. Defina app.enc.key (system property) ou APP_ENC_KEY (env) como Base64 de 16/24/32 bytes.");
        }
        byte[] keyBytes = Base64.getDecoder().decode(b64);
        if (!(keyBytes.length == 16 || keyBytes.length == 24 || keyBytes.length == 32)) {
            throw new IllegalStateException("Tamanho da chave inválido. Use 128/192/256 bits.");
        }
        CACHED_KEY = new SecretKeySpec(keyBytes, ALG);
        return CACHED_KEY;
    }

    public static String encryptToBase64(String plaintext) {
        try {
            if (plaintext == null) return null;
            if (plaintext.isEmpty()) return "";

            byte[] iv = new byte[IV_LEN];
            RNG.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] ct = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // pacote: IV || CT (inclui tag ao final)
            byte[] out = new byte[iv.length + ct.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(ct, 0, out, iv.length, ct.length);

            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao criptografar", e);
        }
    }

    public static String decryptFromBase64(String b64) {
        try {
            if (b64 == null) return null;
            if (b64.isEmpty()) return "";

            byte[] all = Base64.getDecoder().decode(b64);
            if (all.length < IV_LEN + 16) throw new IllegalArgumentException("Payload inválido");

            byte[] iv = new byte[IV_LEN];
            byte[] ct = new byte[all.length - IV_LEN];
            System.arraycopy(all, 0, iv, 0, IV_LEN);
            System.arraycopy(all, IV_LEN, ct, 0, ct.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] pt = cipher.doFinal(ct);
            return new String(pt, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao descriptografar", e);
        }
    }
}
