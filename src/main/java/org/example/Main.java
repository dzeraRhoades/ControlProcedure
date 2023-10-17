package org.example;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
//        EconomicRisksCalculator calculator = new EconomicRisksCalculator(Paths.get("C:\\Users\\Влад\\IdeaProjects\\EconomicRisks\\src\\main\\resources\\risks.json"));
        EconomicRisksController controller = new EconomicRisksController();
        controller.start();
    }
}