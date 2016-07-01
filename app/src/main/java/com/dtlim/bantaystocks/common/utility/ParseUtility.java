package com.dtlim.bantaystocks.common.utility;

/**
 * Created by dale on 7/1/16.
 */
public class ParseUtility {

    private ParseUtility() {}

    public static String[] parseStockList(String stockList) {
        return stockList.split(",");
    }
}
