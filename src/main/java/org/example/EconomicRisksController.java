package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EconomicRisksController {
    private EconomicRisksCalculator calculator;
    private Path file = Paths.get("C:\\Users\\Влад\\IdeaProjects\\EconomicRisks\\src\\main\\resources\\risks.json");
    private int curQuestion = 0;
    public EconomicRisksController()
    {
        calculator = new EconomicRisksCalculator(file);
    }
    public void start()
    {
        // asking user questions
        while()
    }
}
