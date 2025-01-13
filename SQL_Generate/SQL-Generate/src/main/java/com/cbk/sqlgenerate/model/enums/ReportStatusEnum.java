package com.cbk.sqlgenerate.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ReportStatusEnum {

    DEFAULT("未处理", 0),
    HANDLED("已处理", 1);

    private final String text;

    private final int value;

    ReportStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
