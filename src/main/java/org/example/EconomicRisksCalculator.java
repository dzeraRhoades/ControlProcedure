package org.example;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public class EconomicRisksCalculator {
    private EconomicRisks risksData;
//    private Monitor monitor = null;
    EconomicRisksParser parser = new EconomicRisksJsonParser();
    public EconomicRisksCalculator(Path file)
    {
        risksData = parser.parse(file);
    }
    public Question getQuestion(int index)
    {
        return risksData.getQuestions().get(index);
    }

    public Result calculate(Consumer<Object> progress)
    {
        ResultBuilder resultBuilder = new ResultBuilder();
        int risk = 0;
        int reliability = 0;
        List<String> consequences = new ArrayList<>();
        List<String> stringsForTemplate = new ArrayList<>();
        List<Question> questions = risksData.getQuestions();
        for(Question q : questions)
        {
            for(int i = 0; i < q.getAnswers().size(); ++i)
            {
                // if user has chosen this question
                if(q.getChosenAnswers().contains(i))
                {
                    Answer chosenAnswer = q.getAnswers().get(i);
                    List<Integer> coefs = chosenAnswer.getCoefs();
                    if(chosenAnswer.getTemplateIndex() != null)
                    {
                        stringsForTemplate.add(chosenAnswer.getTemplateIndex(), chosenAnswer.getText());
                    }
                    if(coefs != null)
                    {
                        risk+= coefs.get(0);
                        reliability+= coefs.get(1);
                    }
                    if(chosenAnswer.getConsequences() != null)
                    {
                        consequences.addAll(chosenAnswer.getConsequences());
                    }
                }
            }
        }
        resultBuilder.setRisk(risk);
        resultBuilder.setReliability(reliability);
        resultBuilder.setConsequences(consequences);

        // now we want to create report
        // first of all, we should create main text of report with the template we have
        String reportText = String.format(risksData.getReport().getTemplateText(), stringsForTemplate);
        resultBuilder.setReportText(reportText);
        int leftBound;
        int rightBound;
        if(risksData.getReport().getMinPointsForTable() >= risk * reliability)
        {
            for(PKP pkp : risksData.getReport().getPkpList())
            {
                leftBound = pkp.getCoefInteval().get(0);
                rightBound = pkp.getCoefInteval().get(1);
                if(risk > leftBound && risk < rightBound)
                {
                    resultBuilder.setPkp(pkp);
                    break;
                }
            }
        }
        return resultBuilder.build();

    }

    public class Result
    {
        private Result() {}
        private int risk;
        private int reliability;
        private List<String> consequences = null;
        private String reportText = null;
        private PKP pkp;
    }
    private class ResultBuilder
    {
        Result result = null;
        public ResultBuilder()
        {
            Result result = new Result();
        }
        public ResultBuilder setReportText(String reportText)
        {
            result.reportText = reportText;
            return this;
        }
        public ResultBuilder setRisk(int risk)
        {
            result.risk = risk;
            return this;
        }
        public ResultBuilder setReliability(int reliability)
        {
            result.reliability = reliability;
            return this;
        }
        public ResultBuilder setPkp(PKP pkp)
        {
            result.pkp = pkp;
            return this;
        }
        public ResultBuilder setConsequences(List<String> consequences)
        {
            result.consequences = consequences;
        }

        public Result build()
        {
            return result;
        }

    }
}
