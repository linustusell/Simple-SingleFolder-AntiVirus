package com.tusell;

import com.tusell.Scanner.FileScanner;
import com.tusell.Scanner.ThreatResult;
import com.tusell.hash.SignaturesDataBases;
import com.tusell.quarantine.QuarantineManager;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static final Path DEFAULT_SCAN_PATH = Path.of(System.getProperty("user.home"), "ProvaAntiVirus");

    public static void main(String[] args) {
        Path scanPath = args.length >= 1 ? Path.of(args[0]) : DEFAULT_SCAN_PATH;
        if (!scanPath.toFile().exists()) {
            System.err.println("La ruta a escanejar no existeix: " + scanPath.toAbsolutePath());
            return;
        }

        Path signaturesPath = args.length >= 2
                ? Path.of(args[1])
                : Path.of("src", "main", "resources", "signatures.json");
        Path quarantinePath = args.length >= 3
                ? Path.of(args[2])
                : scanPath.resolve("quarantine");

        try {
            SignaturesDataBases db = new SignaturesDataBases();
            db.carregar(signaturesPath);

            QuarantineManager quarantine = new QuarantineManager(quarantinePath);
            FileScanner scanner = new FileScanner(db, quarantine);

            System.out.println("Iniciant escaneig de: " + scanPath.toAbsolutePath());
            System.out.println("Signatures: " + signaturesPath.toAbsolutePath());
            System.out.println("Quarantena: " + quarantinePath.toAbsolutePath());

            List<ThreatResult> resultats = scanner.scan(scanPath);
            printResults(resultats);
        } catch (IOException e) {
            System.err.println("Error d'entrada/sortida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperat durant l'escaneig: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Ús:");
        System.out.println("  java com.tusell.Main <ruta-a-escanejar> [ruta-signatures] [ruta-quarantena]");
        System.out.println();
        System.out.println("Exemple:");
        System.out.println("  java com.tusell.Main C:\\ruta\\a\\escanejar");
        System.out.println("Si no passes ruta, s'escaneja per defecte: " + DEFAULT_SCAN_PATH);
    }

    private static void printResults(List<ThreatResult> resultats) {
        System.out.println();
        System.out.println("Escaneig completat.");
        System.out.println("Amenaces detectades: " + resultats.size());

        if (!resultats.isEmpty()) {
            System.out.println("Els fitxers sospitosos s'han mogut a quarantena.");
        } else {
            System.out.println("No s'ha detectat cap amenaça.");
        }
    }
}