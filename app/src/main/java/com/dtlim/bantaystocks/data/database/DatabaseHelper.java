package com.dtlim.bantaystocks.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dtlim.bantaystocks.data.database.table.StockTable;

import java.io.File;

/**
 * Created by dale on 6/29/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements Database{

    private File mDatabaseFile;
    SQLiteDatabase mDatabase;
    private final Object mLock = new Object();

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
        mDatabaseFile = context.getDatabasePath("bantaystocks");
        try {
            mDatabase = getWritableDatabase();
        }
        catch(Exception e) {
            mDatabaseFile.delete();
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(StockTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        resetDatabase(sqLiteDatabase);
    }

    private void resetDatabase(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(StockTable.DROP);
        onCreate(sqLiteDatabase);
    }

    @Override
    public Cursor query(String table, String selection, String[] selectionArgs) {
        synchronized (mLock) {
            if (mDatabase.isOpen()) {
                return mDatabase.query(table, null, selection, selectionArgs, "", "", "");
            }
            return null;
        }
    }

    @Override
    public Cursor query(String table, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy) {
        synchronized (mLock) {
            if (mDatabase.isOpen()) {
                return mDatabase.query(table, null, selection, selectionArgs,
                        groupBy, having, orderBy);
            }
            return null;
        }
    }

    @Override
    public long insert(String table, ContentValues contentValues) {
        synchronized (mLock) {
            if (mDatabase.isOpen()) {
                return mDatabase.insert(table, null, contentValues);
            }
            return -1L;
        }
    }

    @Override
    public int insert(String table, ContentValues[] contentValues) {
        synchronized (mLock) {
            if (mDatabase.isOpen()) {
                int count = 0;
                mDatabase.beginTransaction();
                for (ContentValues value : contentValues) {
                    count += mDatabase.insert(table, null, value) != -1L ? 1 : 0;
                    mDatabase.yieldIfContendedSafely();
                }
                mDatabase.setTransactionSuccessful();
                mDatabase.endTransaction();
                return count;
            }
            return 0;
        }
    }

    @Override
    public int update(String table, ContentValues contentValues, String where, String[] whereArgs) {
        synchronized (mLock) {
            if (mDatabase.isOpen()) {
                return mDatabase.update(table, contentValues, where, whereArgs);
            }
            return 0;
        }
    }

    @Override
    public int delete(String table, String where, String[] whereArgs) {
        synchronized (mLock) {
            if (mDatabase.isOpen()) {
                return mDatabase.delete(table, where, whereArgs);
            }
            return 0;
        }
    }

    @Override
    public void closeDatabase() {
        synchronized (mLock) {
            mDatabase.close();
        }
    }
}
