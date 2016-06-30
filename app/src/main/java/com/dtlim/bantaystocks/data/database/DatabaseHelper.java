package com.dtlim.bantaystocks.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dtlim.bantaystocks.data.database.table.StockTable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 6/29/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements Database{

    BriteDatabase mBriteDatabase;
    private SqlBrite mSqlBrite = SqlBrite.create();

    private final Object mLock = new Object();

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
        mBriteDatabase = mSqlBrite.wrapDatabaseHelper(this, Schedulers.io());
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
    public Observable query(String table, String query) {
        return mBriteDatabase.createQuery(table, query);
    }

    @Override
    public long insert(String table, ContentValues contentValues) {
        synchronized (mLock) {
            return mBriteDatabase.insert(table, contentValues);
        }
    }

    @Override
    public int insert(String table, ContentValues[] contentValues) {
        synchronized (mLock) {
            int count = 0;
            BriteDatabase.Transaction transaction = mBriteDatabase.newTransaction();
            try {
                for (ContentValues value : contentValues) {
                    count += mBriteDatabase.insert(table, value) != -1L ? 1 : 0;
                    transaction.yieldIfContendedSafely();
                }
                transaction.markSuccessful();
            }
            finally{
                transaction.end();
            }
            return count;
        }
    }

    @Override
    public int update(String table, ContentValues contentValues, String where, String[] whereArgs) {
        synchronized (mLock) {
            return mBriteDatabase.update(table, contentValues, where, whereArgs);
        }
    }

    @Override
    public int delete(String table, String where, String[] whereArgs) {
        synchronized (mLock) {
            return mBriteDatabase.delete(table, where, whereArgs);
        }
    }

    @Override
    public void closeDatabase() {
        synchronized (mLock) {
            mBriteDatabase.close();
        }
    }
}
