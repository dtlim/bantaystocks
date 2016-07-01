package com.dtlim.bantaystocks.data.database;

import android.content.ContentValues;

import rx.Observable;

/**
 * Created by dale on 6/29/16.
 */
public interface Database {
    Observable query(String table, String query);

    Observable query(String table, String query, String... args);

    long insert(String table, ContentValues contentValues);

    int insert(String table, ContentValues[] contentValues);

    int update(String table, ContentValues contentValues, String where, String[] whereArgs);

    int delete(String table, String where, String[] whereArgs);

    void closeDatabase();
}
