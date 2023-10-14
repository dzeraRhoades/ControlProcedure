package org.example;

import lombok.Getter;

import java.util.List;

public class PKP {
    @Getter
    private String text = null;
    @Getter
    private List<Integer> coefInteval = null;
    @Getter
    private List<String> consequences = null;
    @Getter
    private List<String> minimization = null;
}
