package com.tusell.quarantine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuarantineManager {

    private Path quarantineFolder;

    public QuarantineManager(Path quarantineFolder) throws IOException {
        this.quarantineFolder = quarantineFolder;
        Files.createDirectories(quarantineFolder);
        // Crea la carpeta si no existeix
    }

    // Mou el fitxer a la carpeta de quarantena
    public void posar(Path fitxer) throws IOException {
        String dataHora = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );
        String nomAmbData = dataHora + "_" + fitxer.getFileName().toString();
        Path destination = quarantineFolder.resolve(nomAmbData);

        Files.move(fitxer, destination);
    }

    // Registra l'event en un fitxer de log
    public void registrar(Path fitxer, List<String> motius) throws IOException {
        Path logPath = quarantineFolder.resolve("quarantine.log");

        String dataHora = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        String linia = dataHora + " | " + fitxer.getFileName() + " | " + motius + "\n";

        Files.writeString(logPath, linia, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}