package com.cbk.sqlgenerate.core.generator;

import com.cbk.sqlgenerate.common.ErrorCode;
import com.cbk.sqlgenerate.core.schema.TableSchema.Field;
import com.cbk.sqlgenerate.exception.BusinessException;
import com.cbk.sqlgenerate.model.entity.Dict;
import com.cbk.sqlgenerate.service.DictService;
import com.cbk.sqlgenerate.utils.SpringContextUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class DictDataGenerator implements DataGenerator {

    private static final DictService dictService = SpringContextUtils.getBean(DictService.class);

    private final static Gson GSON = new Gson();


    @Override
    public List<String> doGenerate(Field field, int rowNum) {
        String mockParams = field.getMockParams();
        long id = Long.parseLong(mockParams);
        Dict dict = dictService.getById(id);
        if (dict == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "词库不存在");
        }
        List<String> wordList = GSON.fromJson(dict.getContent(),
                new TypeToken<List<String>>() {
                }.getType());
        List<String> list = new ArrayList<>(rowNum);
        for (int i = 0; i < rowNum; i++) {
            String randomStr = wordList.get(RandomUtils.nextInt(0, wordList.size()));
            list.add(randomStr);
        }
        return list;
    }
}
