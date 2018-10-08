package com.aegeanflow.essentials.table;

import com.aegeanflow.core.spi.Streamable;
import com.aegeanflow.core.exchange.Exchange;

public interface Table extends Streamable<Row> {

    Schema getSchema();

    Exchange<Table> exchange();
}
