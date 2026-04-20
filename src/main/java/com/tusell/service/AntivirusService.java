package com.tusell.service;

import com.tusell.Scanner.FileScanner;
import com.tusell.Scanner.ThreatResult;
import com.tusell.hash.SignaturesDataBases;
import com.tusell.quarantine.QuarantineManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AntivirusService {
    public static final Path DEFAULT_SCAN_PATH = Path.of(System.getProperty("user.home"), "ProvaAntiVirus");
    public static final Path DEFAULT_SIGNATURES_PATH = Path.of("src", "main", "resources", "signatures.json");
    public static final Path DEFAULT_QUARANTINE_PATH = DEFAULT_SCAN_PATH.resolve("quarantine");

    public ScanSummary scan(Path scanPath, Path signaturesPath, Path quarantinePath) throws Exception {
        validateScanPath(scanPath);
        validateSignaturesPath(signaturesPath);

        SignaturesDataBases db = new SignaturesDataBases();
        db.carregar(signaturesPath);

        QuarantineManager quarantine = new QuarantineManager(quarantinePath);
        FileScanner scanner = new FileScanner(db, quarantine);
        List<ThreatResult> threats = scanner.scan(scanPath);

        return new ScanSummary(scanPath, signaturesPath, quarantinePath, threats);
    }

    private void validateScanPath(Path scanPath) throws IOException {
        if (scanPath == null || !Files.exists(scanPath) || !Files.isDirectory(scanPath)) {
            throw new IOException("La ruta a escanejar no existeix o no és una carpeta: " + scanPath);
        }
    }

    private void validateSignaturesPath(Path signaturesPath) throws IOException {
        if (signaturesPath == null || !Files.exists(signaturesPath) || Files.isDirectory(signaturesPath)) {
            throw new IOException("No s'ha trobat el fitxer de signatures: " + signaturesPath);
        }
    }
}
