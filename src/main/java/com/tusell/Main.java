package com.tusell;

import com.tusell.Scanner.FileScanner;
import com.tusell.hash.HashCalculator;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        FileScanner scanner = new FileScanner();
        scanner.scan(Path.of("C:\\Users\\Linus\\ProvaAntiVirus"));
        HashCalculator calc = new HashCalculator();
        Path fitxer = Path.of("C:\\Users\\Linus\\ProvaAntiVirus\\virus.exe");
        System.out.println(calc.calcularHash(fitxer, "SHA-256"));
    }
}
