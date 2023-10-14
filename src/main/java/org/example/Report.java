package org.example;

import lombok.Getter;

import java.util.List;

public class Report {
    @Getter
    private String templateText;
    @Getter
    private Integer minPointsForTable;
    @Getter
    private List<PKP> pkpList;
}
