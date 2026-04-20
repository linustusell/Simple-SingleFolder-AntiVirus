package com.tusell.service;

import com.tusell.Scanner.ThreatResult;
import java.nio.file.Path;
import java.util.List;

public class ScanSummary {
    private final Path scannedPath;
    private final Path signaturesPath;
    private final Path quarantinePath;
    private final List<ThreatResult> threats;

    public ScanSummary(Path scannedPath, Path signaturesPath, Path quarantinePath, List<ThreatResult> threats) {
        this.scannedPath = scannedPath;
        this.signaturesPath = signaturesPath;
        this.quarantinePath = quarantinePath;
        this.threats = threats;
    }

    public Path getScannedPath() {
        return scannedPath;
    }

    public Path getSignaturesPath() {
        return signaturesPath;
    }

    public Path getQuarantinePath() {
        return quarantinePath;
    }

    public List<ThreatResult> getThreats() {
        return threats;
    }
}
