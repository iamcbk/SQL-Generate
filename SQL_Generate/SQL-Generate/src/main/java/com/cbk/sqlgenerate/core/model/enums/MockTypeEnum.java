package com.cbk.sqlgenerate.core.model.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MockTypeEnum {

    NONE("不模拟"),
    INCREASE("递增"),
    FIXED("固定"),
    RANDOM("随机"),
    RULE("规则"),
    DICT("词库");

    private final String value;

    MockTypeEnum(String value) {
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(MockTypeEnum::getValue).collect(Collectors.toList());
    }

    public static MockTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (MockTypeEnum mockTypeEnum : MockTypeEnum.values()) {
            if (mockTypeEnum.value.equals(value)) {
                return mockTypeEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
