package com.aegeanflow.core.node.data;

import com.aegeanflow.core.flow.Flow;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Optional;

/**
 * Created by gorkem on 29.01.2018.
 */
public enum SqlType {
    VARCHAR(Types.VARCHAR, String.class),
    NVARCHAR(Types.NVARCHAR, String.class),
    DOUBLE(Types.DOUBLE, Double.class),
    DECIMAL(Types.DECIMAL, Double.class),
    INTEGER(Types.INTEGER, Integer.class),
    FLOAT(Types.FLOAT, Float.class),
    NUMERIC(Types.NUMERIC, BigDecimal.class),
    BIGINT(Types.BIGINT, Long.class),
    DATE(Types.DATE, Date.class),
    TIME(Types.TIME, Time.class),
    TIMESTAMP(Types.TIMESTAMP, Timestamp.class),
    UNKOWN(Types.NULL, Object.class)
    ;

    private final int sqlType;

    private final Class<?> javaType;

    SqlType(int sqlType, Class<?> javaType) {
        this.sqlType = sqlType;
        this.javaType = javaType;
    }

    public int getSqlType() {
        return sqlType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public static SqlType valueOf(int type) {
        for (SqlType sqlType : SqlType.values()){
            if (sqlType.getSqlType() == type) {
                return sqlType;
            }
        }
        return UNKOWN;
    }
}
