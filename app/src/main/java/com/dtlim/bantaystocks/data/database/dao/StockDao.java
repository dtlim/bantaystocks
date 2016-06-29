package com.dtlim.bantaystocks.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.dtlim.bantaystocks.data.database.Database;
import com.dtlim.bantaystocks.data.database.table.StockTable;
import com.dtlim.bantaystocks.data.model.Price;
import com.dtlim.bantaystocks.data.model.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dale on 6/29/16.
 */
public class StockDao extends BaseDao<Stock>{

    public StockDao(Database database) {
        super(database);
    }

    @Override
    protected String getTableName() {
        return StockTable.TABLE_NAME;
    }

    @Override
    protected ContentValues getContentValues(Stock stock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(StockTable.NAME, stock.getName());
        contentValues.put(StockTable.PRICE, stock.getPrice().getAmount());
        contentValues.put(StockTable.CURRENCY, stock.getPrice().getCurrency());
        contentValues.put(StockTable.PERCENT_CHANGE, stock.getPercentChange());
        contentValues.put(StockTable.VOLUME, stock.getVolume());
        contentValues.put(StockTable.SYMBOL, stock.getSymbol());

        return contentValues;
    }

    @Override
    protected List<Stock> parseCursor(Cursor cursor) {
        List<Stock> stockList = new ArrayList<>();

        try{
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    Stock stock = new Stock();
                    stock.setName(cursor.getString(cursor.getColumnIndex(StockTable.NAME)));
                    stock.setPercentChange(cursor.getString(cursor.getColumnIndex(StockTable.PERCENT_CHANGE)));
                    stock.setVolume(cursor.getString(cursor.getColumnIndex(StockTable.VOLUME)));
                    stock.setSymbol(cursor.getString(cursor.getColumnIndex(StockTable.SYMBOL)));
                    Price price = new Price(cursor.getString(cursor.getColumnIndex(StockTable.PRICE)),
                            cursor.getString(cursor.getColumnIndex(StockTable.CURRENCY)));
                    stock.setPrice(price);
                    stockList.add(stock);
                }
                while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return stockList;
    }
}
