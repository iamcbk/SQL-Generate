package com.cbk.sqlgenerate.core.generator;

import com.cbk.sqlgenerate.core.schema.TableSchema.Field;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IncreaseDataGenerator implements DataGenerator {

    @Override
    public List<String> doGenerate(Field field, int rowNum) {
        String mockParams = field.getMockParams();
        List<String> list = new ArrayList<>(rowNum);
        if (StringUtils.isBlank(mockParams)) {
            mockParams = "1";
        }
        int initValue = Integer.parseInt(mockParams);
        for (int i = 0; i < rowNum; i++) {
            list.add(String.valueOf(initValue + i));
        }
        return list;
    }
}
