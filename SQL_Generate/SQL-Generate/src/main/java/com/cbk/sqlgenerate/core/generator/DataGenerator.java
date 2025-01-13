package com.cbk.sqlgenerate.core.generator;

import com.cbk.sqlgenerate.core.schema.TableSchema.Field;

import java.util.List;

public interface DataGenerator {

    List<String> doGenerate(Field field, int rowNum);

}
