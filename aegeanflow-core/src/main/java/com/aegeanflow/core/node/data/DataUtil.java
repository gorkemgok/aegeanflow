package com.aegeanflow.core.node.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by gorkem on 29.01.2018.
 */
public class DataUtil {
    public static final ResultSetToTabularData RS_TD_CONVERTER_INSTANCE = new ResultSetToTabularData();

    public static TabularData convert(ResultSet resultSet) throws SQLException {
        return RS_TD_CONVERTER_INSTANCE.convert(resultSet);
    }
}
