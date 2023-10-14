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
    @Getter
    private List<List<Object>> subQuestions = null;
    public void answer(List<Integer> answer)
    {
        chosenAnswers = answer;
    }
    @Expose(serialize = false, deserialize = false)
    @Getter
    private List<Integer> chosenAnswers = null;
}
