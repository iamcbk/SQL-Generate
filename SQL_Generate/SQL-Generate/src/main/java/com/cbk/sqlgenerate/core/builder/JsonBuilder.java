package com.cbk.sqlgenerate.core.builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class JsonBuilder {

    public static String buildJson(List<Map<String, Object>> dataList) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(dataList);
    }
}
