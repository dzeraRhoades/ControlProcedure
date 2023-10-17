package org.example;

import lombok.Getter;
import org.apache.commons.text.StringSubstitutor;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class EconomicRisksCalculator {
    private EconomicRisks risksData;
    private Map<String, String> stringsForTemplate;
    private List<String> consequences;
    private float risk;
    private float reliability;

    //    private Monitor monitor = null;
    EconomicRisksParser parser = new EconomicRisksJsonParser();
    public EconomicRisksCalculator(Path file)
    {
        risksData = parser.parse(file);
    }
    public Question getQuestion(int index)
    {
        if(index >= risksData.getQuestions().size())
            return null;
        return risksData.getQuestions().get(index);
    }

    public Result calculate(Consumer<Object> progress)
    {
        stringsForTemplate = new LinkedHashMap<>();
        consequences = new ArrayList<>();
        ResultBuilder resultBuilder = new ResultBuilder();
//        List<String> stringsForTemplate = new ArrayList<>();
        List<Question> questions = risksData.getQuestions();
        for(Question q : questions)
        {
            handleQuestion(q);
        }
        risk = Math.min(risk, 1);
        reliability = Math.min(reliability, 1);
        resultBuilder.setRisk(risk * 5);
        resultBuilder.setReliability(reliability * 5);
        resultBuilder.setConsequences(consequences);
        StringSubstitutor templTextSubsitutor = new StringSubstitutor(stringsForTemplate, "{", "}");
        // now we want to create report
        // first of all, we should create main text of report with the template we have
        String reportText = templTextSubsitutor.replace(risksData.getReport().getTemplateText());
        resultBuilder.setReportText(reportText);
        float leftBound;
        float rightBound;
        if(risksData.getReport().getMinPointsForTable() <= risk * reliability * 25)
        {
            for(PKP pkp : risksData.getReport().getPkpList())
            {
                leftBound = pkp.getCoefInterval().get(0);
                rightBound = pkp.getCoefInterval().get(1);
                if(risk > leftBound && risk <= rightBound)
                {
                    resultBuilder.setPkp(pkp);
                    break;
                }
            }
        }
        return resultBuilder.build();
    }
    private void handleQuestion(Question question)
    {
        for(int i = 0; i < question.getAnswers().size(); ++i)
        {
            // if user has chosen this question
            if(question.getChosenAnswers().contains(i))
            {
                Answer chosenAnswer = question.getAnswers().get(i);
                List<Float> coefs = chosenAnswer.getCoefs();
                if(chosenAnswer.getTemplateIndex() != null)
                {
                    stringsForTemplate.put(String.valueOf(chosenAnswer.getTemplateIndex()), chosenAnswer.getText());
                }
                if(coefs != null)
                {
                    risk+= coefs.get(0);
                    reliability += coefs.get(1);
                }
                if(chosenAnswer.getConsequences() != null)
                {
                    consequences.addAll(chosenAnswer.getConsequences());
                }
            }
        }
        if(question.getSubQuestions() != null)
        {
            for(Question q : question.getSubQuestions())
            {
                if(q.getChosenAnswers() != null)
                    handleQuestion(q);
            }
        }
    }

    public class Result
    {
        private Result() {}
        @Getter
        private float risk;
        @Getter
        private float reliability;
        @Getter
        private List<String> consequences = null;
        @Getter
        private String reportText = null;
        @Getter
        private PKP pkp;
    }
    private class ResultBuilder
    {
        Result result = null;
        public ResultBuilder()
        {
            result = new Result();
        }
        public ResultBuilder setReportText(String reportText)
        {
            result.reportText = reportText;
            return this;
        }
        public ResultBuilder setRisk(float risk)
        {
            result.risk = risk;
            return this;
        }
        public ResultBuilder setReliability(float reliability)
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
            return this;
        }

        public Result build()
        {
            return result;
        }

    }
}
