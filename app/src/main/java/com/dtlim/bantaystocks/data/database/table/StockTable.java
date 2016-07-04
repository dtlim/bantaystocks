package com.dtlim.bantaystocks.data.database.table;

import android.provider.BaseColumns;

/**
 * Created by dale on 6/29/16.
 */
public class StockTable implements BaseColumns {
    public static final String TABLE_NAME = "stocks";

    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String CURRENCY = "currency";
    public static final String PERCENT_CHANGE = "percent_change";
    public static final String VOLUME = "volume";
    public static final String SYMBOL = "symbol";

    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + _ID                   +       " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME                  +       " TEXT, "
            + PRICE                 +       " TEXT, "
            + CURRENCY              +       " TEXT, "
            + PERCENT_CHANGE        +       " TEXT, "
            + VOLUME                +       " TEXT, "
            + SYMBOL                +       " TEXT, "
            + "UNIQUE (" + NAME + ") ON CONFLICT REPLACE);";

    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
