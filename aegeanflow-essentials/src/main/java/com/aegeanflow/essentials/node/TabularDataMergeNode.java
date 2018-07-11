package com.aegeanflow.essentials.node;

import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkem on 30.01.2018.
 */
@NodeEntry
public class TabularDataMergeNode extends AbstractRunnableNode<TabularData> {

    private TabularData data1;

    private TabularData data2;

    @Override
    public TabularData call() throws Exception {
        List<TabularData.Field> fieldList = new ArrayList<>(data1.getSchema().getFieldList());
        fieldList.addAll(data2.getSchema().getFieldList());

        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < data1.getData().size(); i++) {
            List<Object> row = new ArrayList<>();
            for (int j = 0; j < data1.getSchema().getFieldList().size(); j++) {
                row.add(data1.getData().get(i).get(j));
            }
            for (int j = 0; j < data2.getSchema().getFieldList().size(); j++) {
                if (i < data2.getData().size()) {
                    row.add(data2.getData().get(i).get(j));
                } else {
                    row.add(null);
                }
            }
            data.add(row);
        }
        return new TabularData(new TabularData.Schema(fieldList), data);
    }

    @NodeInput
    public void setData1(TabularData data1) {
        this.data1 = data1;
    }

    @NodeInput
    public void setData2(TabularData data2) {
        this.data2 = data2;
    }
}
