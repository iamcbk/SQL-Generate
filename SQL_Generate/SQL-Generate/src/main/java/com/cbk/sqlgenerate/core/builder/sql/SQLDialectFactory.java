package com.cbk.sqlgenerate.core.builder.sql;

import com.cbk.sqlgenerate.common.ErrorCode;
import com.cbk.sqlgenerate.exception.BusinessException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SQLDialectFactory {

    private static final Map<String, SQLDialect> DIALECT_POOL = new ConcurrentHashMap<>();

    private SQLDialectFactory() {
    }

    public static SQLDialect getDialect(String className) {
        SQLDialect dialect = DIALECT_POOL.get(className);
        if (null == dialect) {
            synchronized (className.intern()) {
                dialect = DIALECT_POOL.computeIfAbsent(className,
                        key -> {
                            try {
                                return (SQLDialect) Class.forName(className).newInstance();
                            } catch (Exception e) {
                                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                            }
                        });
            }
        }
        return dialect;
    }
}
