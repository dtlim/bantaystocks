package com.dtlim.bantaystocks.data.database.repository;

import com.dtlim.bantaystocks.data.database.Database;
import com.dtlim.bantaystocks.data.database.dao.StockDao;
import com.dtlim.bantaystocks.data.database.table.StockTable;
import com.dtlim.bantaystocks.data.model.Stock;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;

/**
 * Created by dale on 6/29/16.
 */
public class SqliteDatabaseRepository implements DatabaseRepository{

    private StockDao mStockDao;
    private Database mDatabase;

    public SqliteDatabaseRepository(Database database) {
        mDatabase = database;
        mStockDao = new StockDao(mDatabase);
    }


    @Override
    public long insert(Stock stock) {
        return mStockDao.insert(stock);
    }

    @Override
    public long insert(List<Stock> stocks) {
        return mStockDao.insert(stocks);
    }

    @Override
    public long update(Stock stock) {
        return mStockDao.update(stock, StockTable.NAME + "=?", new String[]{stock.getName()});
    }

    @Override
    public long delete(Stock stock) {
        return mStockDao.delete(StockTable.NAME + "=?", new String[]{stock.getName()});
    }

    public Observable<SqlBrite.Query> queryStocks() {
        return mStockDao.query("SELECT * FROM " + StockTable.TABLE_NAME);
    }

    public Observable<SqlBrite.Query> queryStocks(String... stocks) {
        String query = "SELECT * FROM " + StockTable.TABLE_NAME + " WHERE ";
        for(int i=0; i<stocks.length; i++) {
            query += StockTable.SYMBOL + "=?";
            query += i==stocks.length-1 ? "" : " OR ";
        }
        return mStockDao.query(query, stocks);
    }
}
