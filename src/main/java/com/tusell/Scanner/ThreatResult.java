package com.tusell.Scanner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ThreatResult {
    private Path fitxer;
    private List<String> motius;

    public ThreatResult(Path fitxer) {
        this.fitxer = fitxer;
        this.motius = new ArrayList<>();
    }

    public void afegirMotiu(String motiu) {
        motius.add(motiu);
    }

    public boolean esAmenaca() {
        return !motius.isEmpty();
    }

    public Path getFitxer() {
        return fitxer;
    }

    public List<String> getMotius() {
        return motius;
    }

    public void setMotius(List<String> motius) {
        this.motius = motius;
    }
}