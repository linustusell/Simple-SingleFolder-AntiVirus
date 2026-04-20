package com.tusell;

import com.tusell.Scanner.FileScanner;
import com.tusell.hash.SignaturesDataBases;
import com.tusell.quarantine.QuarantineManager;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {

        String home = System.getProperty("user.home");

        SignaturesDataBases db = new SignaturesDataBases();
        db.carregar(Path.of("src/main/resources/signatures.json"));

        QuarantineManager quarantine = new QuarantineManager(
                Path.of(home, "Quarantine")
        );

        FileScanner scanner = new FileScanner(db, quarantine);
        scanner.scan(Path.of(home,  "ProvaAntiVirus"));
    }
}