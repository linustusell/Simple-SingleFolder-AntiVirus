package com.tusell.hash;

import java.security.MessageDigest;
import java.nio.file.Files;
import java.nio.file.Path;

public class HashCalculator {

    // Calcula el hash d'un fitxer
    // algorisme pot ser "MD5" o "SHA-256"
    public String calcularHash(Path fitxer, String algorisme) throws Exception {

        MessageDigest hash = MessageDigest.getInstance(algorisme);
        byte[] bytes = Files.readAllBytes(fitxer);
        byte[] digest = hash.digest(bytes);
        return bytesAHex(digest);

    }

    private String bytesAHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            // cada byte el convertim a 2 caràcters hexadecimals
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}