package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

public class EconomicRisksController {
    private EconomicRisksCalculator calculator;
    private Path file = Paths.get("risks.json");
    private int curQuestion = 0;
    public EconomicRisksController()
    {
        calculator = new EconomicRisksCalculator(file);
    }
    public void start() throws IOException {
        int qIndex = 0;
        Question curQuestion;
        // asking user questions
        while ((curQuestion = calculator.getQuestion(qIndex)) != null)
        {
            handleQuestion(curQuestion);
            qIndex++;
        }
        // после того как спросили все вопросы, считаем результат и отчет
        EconomicRisksCalculator.Result result = calculator.calculate((obj) -> {});
        System.out.printf("risk: %f\n", result.getRisk());
        System.out.printf("reliability: %f\n", result.getReliability());
        for(String con : result.getConsequences())
        {
            System.out.printf("consequence: %s\n", con);
        }
        System.out.printf("Report main text: %s\n", result.getReportText());
        if(result.getPkp() != null)
        {
            System.out.printf("PKP main text: %s\n", result.getPkp().getText());
            for(String con : result.getPkp().getConsequences())
            {
                System.out.printf("PKP consequence: %s\n", con);
            }
            for(String min : result.getPkp().getMinimization())
            {
                System.out.printf("PKP minimization: %s\n", min);
            }
        }
    }

    void handleQuestion(Question question) throws IOException {
        System.out.println(question.getQuestionText());
        int answerIndex = 0;
        for(Answer answer : question.getAnswers())
        {
            System.out.printf("%d)", answerIndex + 1);
            System.out.println(answer.getText());
            answerIndex++;
        }
        System.out.printf("Choose answer: ");
        int choice = System.in.read() - '0';
        while(choice > question.getAnswers().size() || choice < 0)
        {
            System.out.printf("Choose answer again: ");
            choice = System.in.read() - '0';
            // пока пользователь не ввел корректный ответ
        }
        question.getChosenAnswers().add(choice - 1);
        if(question.getSubQuestions() != null)
        {
            for(Question subQuestion : question.getSubQuestions())
            {
                // елси был выбран не тот вариант, от которого зависел вопрос
                if(subQuestion.getDependsOnAnswer() != null && subQuestion.getDependsOnAnswer() != choice - 1)
                    continue;
                handleQuestion(subQuestion);
            }
        }
    }
}
