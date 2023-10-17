package org.example;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Question {
    @Getter
    private String questionText;
    @Getter
    private List<String> consequences = null;
    @Getter
    private List<Answer> answers;
    // нужен чтоб понимать, зависит ли вопрос от под вопроса
    @Getter
    private Integer dependsOnAnswer = null;
    // подвопросы представлены в виде списка списков, так как некоторые подвопросы могут зависеть от ответов. Поэтому
    // каждый подписок - это набор из трёх значений: 1) Сам вопрос, 2) индекс ответа, от которого зависит задавать
    // вопрос или нет. 3) Добавочный коэффициент, который уувеличивает коэф подвопроса (пока что не реализовано)
    @Getter
    private List<Question> subQuestions = null;
    public void answer(List<Integer> answer)
    {
        chosenAnswers = answer;
    }
    @Expose(serialize = false, deserialize = false)
    @Getter
    private List<Integer> chosenAnswers = new ArrayList<>();
}
