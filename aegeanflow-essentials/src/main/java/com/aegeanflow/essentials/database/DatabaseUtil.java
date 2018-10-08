package com.aegeanflow.essentials.database;

import com.aegeanflow.essentials.table.Field;
import com.aegeanflow.essentials.table.Schema;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseUtil {

    private DatabaseUtil() {
    }

    public static Schema createSchema(ResultSet rs) throws SQLException, ClassNotFoundException {
        ResultSetMetaData metaData = rs.getMetaData();
        List<Field> fieldList = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String fieldName = metaData.getColumnName(i);
            String fieldClassName = metaData.getColumnClassName(i);
            Field field = new Field(fieldName, Class.forName(fieldClassName));
            fieldList.add(field);
        }
        Schema schema = new Schema(fieldList);
        return schema;
    }
}
