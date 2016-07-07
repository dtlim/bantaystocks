package com.dtlim.bantaystocks.common.utility;

import android.util.Log;

import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.model.StockList;
import com.google.gson.Gson;

/**
 * Created by dale on 7/1/16.
 */
public class ParseUtility {
    private static final Gson gson = new Gson();
    private ParseUtility() {}

    public static String[] parseStockList(String stockList) {
        return stockList.trim().isEmpty() ? new String[]{} : stockList.split(",");
    }

    public static String encodeStockList(String[] stockList) {
        if(stockList==null || stockList.length <= 0) {
            return "";
        }
        StringBuilder encoded = new StringBuilder();
        for(int i=0; i<stockList.length; i++) {
            encoded.append(stockList[i]);
            encoded.append((i == stockList.length-1 ? "" : ","));
        }
        return encoded.toString();
    }

    public static Stock parseSingleStockFromJson(String json) {
        try {
            return gson.fromJson(json, Stock.class);
        }
        catch (Exception e) {
        }
        return null;
    }

    public static StockList parseStockListFromJson(String json) {
        try {
            return gson.fromJson(json, StockList.class);
        }
        catch (Exception e) {
        }
        return null;
    }
}
