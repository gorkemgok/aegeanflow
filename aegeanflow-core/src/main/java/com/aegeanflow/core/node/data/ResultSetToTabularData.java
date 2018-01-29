package com.aegeanflow.core.node.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by gorkem on 18.01.2018.
 */
public class ResultSetToTabularData implements Convertor<ResultSet, TabularData> {
    @Override
    public TabularData convert(ResultSet rs) throws SQLException{
        ResultSetMetaData metaData = rs.getMetaData();
        List<TabularData.Field> fieldList = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String name = metaData.getColumnName(i);
            SqlType sqlType = SqlType.valueOf(metaData.getColumnType(i));
            TabularData.Field field = new TabularData.Field(name, sqlType.getJavaType());
            fieldList.add(field);
        }
        TabularData.Schema schema = new TabularData.Schema(fieldList);
        List<List<Object>> data = new ArrayList<>();
        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }
        return new TabularData(schema, data);
    }
}
