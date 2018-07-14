package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;
import java.util.StringJoiner;

import static java.lang.String.format;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(name = "Database Writer")
public class DatabaseWriterBox extends AbstractAnnotatedBox<Void> {

    private Connection connection;

    private TabularData data;

    private Map<String, String> fieldMap;

    private Integer batchCount = 1000;

    private String tableName;

    @Override
    public Exchange<Void> call() throws Exception {
        StringJoiner fieldsJoiner = new StringJoiner(",", "(", ")");
        data.getSchema().getFieldList().forEach(field -> {
            fieldsJoiner.add(field.getName());
        });
        StringJoiner valuesJoiner = new StringJoiner(",");
        data.getData().forEach(row -> {
            StringJoiner rowJoiner = new StringJoiner(",", "(", ")");
            for (int i = 0; i < row.size(); i++) {
                Object value = row.get(i);
                TabularData.Field field = data.getSchema().getFieldList().get(i);
                if (value == null){
                    rowJoiner.add("null");
                } else if (field.getType().equals(String.class)) {
                    rowJoiner.add(format("'%s'", value.toString()));
                }else if (field.getType().equals(Double.class) ||
                        field.getType().equals(Integer.class) ||
                        field.getType().equals(Long.class) ||
                        field.getType().equals(BigDecimal.class) ||
                        field.getType().equals(Float.class)) {
                    rowJoiner.add(value.toString());
                } else if (field.getType().equals(Timestamp.class)) {
                    rowJoiner.add(format("timestamp '%s'", value));
                } else {
                    rowJoiner.add(format("'%s'", value.toString()));
                }
            }
            valuesJoiner.add(rowJoiner.toString());
        });
        String sql = format("INSERT INTO %s %s VALUES %s", tableName, fieldsJoiner.toString(), valuesJoiner.toString());
        System.out.println(sql);
        return null;
    }

    @NodeInput
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @NodeInput
    public void setData(TabularData data) {
        this.data = data;
    }

    @NodeInput
    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    @NodeInput(label = "Insert Table Name", order = 1)
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @NodeInput(label = "Batch Count", order = 2)
    public void setBatchCount(Integer batchCount) {
        this.batchCount = batchCount;
    }
}
