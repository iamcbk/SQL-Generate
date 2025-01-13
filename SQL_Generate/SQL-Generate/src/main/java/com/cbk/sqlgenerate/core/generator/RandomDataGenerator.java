package com.cbk.sqlgenerate.core.generator;

import com.cbk.sqlgenerate.core.model.enums.MockParamsRandomTypeEnum;
import com.cbk.sqlgenerate.core.schema.TableSchema.Field;
import com.cbk.sqlgenerate.core.utils.FakerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomDataGenerator implements DataGenerator {

    @Override
    public List<String> doGenerate(Field field, int rowNum) {
        String mockParams = field.getMockParams();
        List<String> list = new ArrayList<>(rowNum);
        for (int i = 0; i < rowNum; i++) {
            MockParamsRandomTypeEnum randomTypeEnum = Optional.ofNullable(
                            MockParamsRandomTypeEnum.getEnumByValue(mockParams))
                    .orElse(MockParamsRandomTypeEnum.STRING);
            String randomString = FakerUtils.getRandomValue(randomTypeEnum);
            list.add(randomString);
        }
        return list;
    }
}
