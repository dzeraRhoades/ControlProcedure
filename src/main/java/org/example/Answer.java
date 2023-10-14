package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

class Answer {
    @Getter @Setter
    protected int id;
    @Getter @Setter
    protected String text;
    @Getter @Setter
    private List<String> consequences = null;
    // coefficients for risk and reliability
    @Getter
    private List<Integer> coefs = null;
    @Getter
    private Integer templateIndex = null;
}

class ConsequenceAnswer extends Answer
{

}
class CoefAnswer extends Answer
{
    private Integer coef = null;
}
