package com.aegeanflow.core.node.data;

import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeOutputBean
public class TabularDataInfo {

    private final int columnCount;

    private final int rowCount;

    public TabularDataInfo(int columnCount, int rowCount) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    @NodeOutput
    public int getColumnCount() {
        return columnCount;
    }

    @NodeOutput
    public int getRowCount() {
        return rowCount;
    }
}
