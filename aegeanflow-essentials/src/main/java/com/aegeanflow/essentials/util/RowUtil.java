package com.aegeanflow.essentials.util;

import com.aegeanflow.essentials.table.Row;

public final class RowUtil {

    private RowUtil() {
    }

    public static boolean equals(Row r1, Row r2, double delta) {
        if (r1.size() == r2.size()) {
            for (int i = 0; i < r1.size(); i++) {
                if (!equals(r1, r2, i, i, delta)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(Row r1, Row r2, int i1, int i2, double delta) {
        Object v1 = r1.get(i1);
        Object v2 = r2.get(i2);

        if (!v1.equals(v2)) {
            if (v1 instanceof Number && v2 instanceof Number) {
                return ((Number)v1).doubleValue() - ((Number)v2).doubleValue() < delta;
            }
            return false;
        }
        return true;
    }
}
