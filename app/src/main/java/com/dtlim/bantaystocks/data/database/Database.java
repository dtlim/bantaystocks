package com.dtlim.bantaystocks.data.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by dale on 6/29/16.
 */
public interface Database {
    Cursor query(String table, String selection, String[] selectionArgs);

    Cursor query(String table, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    long insert(String table, ContentValues contentValues);

    int insert(String table, ContentValues[] contentValues);

    int update(String table, ContentValues contentValues, String where, String[] whereArgs);

    int delete(String table, String where, String[] whereArgs);

    void closeDatabase();
}
