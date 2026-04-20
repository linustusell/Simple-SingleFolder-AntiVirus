package com.tusell.Scanner;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileScanner {

    public void scan(Path rootPath) throws IOException {
        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                ThreatResult resultat = new ThreatResult(file);

                if (extensioPerillosa(file))  resultat.afegirMotiu("Extensió perillosa");
                if (esOcult(file))            resultat.afegirMotiu("Fitxer ocult");
                if (nomEstrany(file))         resultat.afegirMotiu("Nom estrany");
                if (midaAnormal(attrs))       resultat.afegirMotiu("Mida anormal");


                if (resultat.esAmenaca()) {
                    System.out.println("️Atenció " + file + " → " + resultat.getMotius());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("No s'ha pogut llegir: " + file);
                return FileVisitResult.CONTINUE; // continua tot i l'error
            }
        });
    }

    // Retorna true si l'extensió és perillosa
    private boolean extensioPerillosa(Path fitxer) {
         String Tipus = fitxer.getFileName().toString();
        return Tipus.endsWith(".exe") || Tipus.endsWith(".bat") || Tipus.endsWith(".cmd") || Tipus.endsWith(".vbs")
                || Tipus.endsWith(".jar") || Tipus.endsWith(".ps1");

        //Extensions: .exe .bat .cmd .vbs .jar .ps1

        //fitxer.getFileName().toString() i endsWith()
    }

    // Retorna true si el fitxer és ocult
    private boolean esOcult(Path fitxer) throws IOException {
        return Files.isHidden(fitxer);
        //Files.isHidden(fitxer)
    }

    // Retorna true si el nom sembla estrany
    private boolean nomEstrany(Path fitxer) {
        String nom = fitxer.getFileName().toString();

        // Comprovació 1: té doble extensió perillosa?
        // mira si el nom conté ".pdf.exe", ".jpg.bat", etc.
        // String[] extensions = {".exe", ".bat", ".cmd", ".vbs"};
        // has de comprovar si ABANS d'aquestes extensions hi ha UN ALTRE punt

        String[] extensions = new String[]{                // Executables Windows
                ".exe", ".com", ".scr", ".msi",

                // Scripts Windows
                ".bat", ".cmd", ".ps1", ".vbs", ".wsf", ".wsh",

                // Scripts Linux/Mac
                ".sh", ".bash", ".zsh",

                // Java
                ".jar",

                // Office amb macros (phishing)
                ".docm", ".xlsm", ".pptm", ".txt",

                // Altres
                ".dll", ".sys", ".hta", ".lnk"};
        for (String extension : extensions) {
            if (nom.endsWith(extension)) {
                nom = nom.substring(0, nom.length() - extension.length());
                for (String extension2 : extensions) {
                    if (nom.endsWith(extension2)) {
                        return true;
                    }
                }
            }
        }


        // Comprovació 2: té massa caràcters especials?
        // recorre el nom lletra a lletra amb un comptador
        // caràcters especials: !@#$%^&*()[]{}
        // si el comptador > 3, sospitós

        String[] CaractersEspecials = new String[]{ "!","@","#","$","%","^","&","*","(",")","[","]","{","}"};
        int numChar = 0;
        nom = fitxer.getFileName().toString();
        for (String caracter : CaractersEspecials) {
            if (nom.contains(caracter)) {
                numChar++;
            }
        }
        if (numChar >= 4) {
            return true;
        }

        // Comprovació 3: nom massa llarg?
        //nom.length() > ...

        nom = fitxer.getFileName().toString();
        if (nom.length() > 100){
            return true;
        }


        // Comprovació 4: té espais al final (abans de l'extensió)
        //mira si nom te "  " (doble espai) o espai seguit de punt
        int indexUltimPunt = nom.lastIndexOf('.');
        if (indexUltimPunt > 0 && nom.charAt(indexUltimPunt - 1) == ' ') {
            return true;
        }

        return false;


    }


    // Retorna true si la mida és anormal (p.ex. > 50MB o 0 bytes)
    private boolean midaAnormal(BasicFileAttributes attrs) {
        return attrs.size() >= 50 * 1024 * 1024 || attrs.size() == 0;
        //attrs.size()
    }
}
