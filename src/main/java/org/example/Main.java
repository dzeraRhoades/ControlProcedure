package org.example;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        EconomicRisksCalculator calculator = new EconomicRisksCalculator(Paths.get("C:\\Users\\Влад\\IdeaProjects\\EconomicRisks\\src\\main\\resources\\risks.json"));
        calculator.calculate();
    }
}