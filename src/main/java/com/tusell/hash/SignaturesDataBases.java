package com.tusell.hash;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SignaturesDataBases {

    private Map<String, String> signatures = new HashMap<>();

    // Carrega el JSON des del fitxer
    public void carregar(Path fitxerJson) throws IOException {

        String contingut = Files.readString(fitxerJson);
        signatures = new HashMap<>();

        // Elimina { } i processa línia a línia
        contingut = contingut.replace("{", "").replace("}", "").trim();

        for (String linia : contingut.split(",")) {
            linia = linia.trim();
            if (linia.isEmpty()) continue;

            String[] parts = linia.split(":");
            String hash = parts[0].trim().replace("\"", "");
            String malware = parts[1].trim().replace("\"", "");

            signatures.put(hash, malware);
        }
    }

    // Comprova si un hash és malware conegut
    // Retorna el nom del malware o null si és net
    public String consultar(String hash) {

        return signatures.get(hash);
    }
}