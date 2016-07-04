package com.dtlim.bantaystocks.data.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.model.StockList;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by dale on 6/28/16.
 */
public class MqttStocksNotificationRepository implements StocksNotificationRepository {

    public static final String MQTT_SERVER_URI = "tcp://api.brandx.dev.voyager.ph:1883";
    public static final String TOPIC_PREFIX = "dale/stocks/";
    public static final Gson gson = new Gson();

    private MqttClient mClient;
    PublishSubject mStocksSubject;
    Handler handler = new Handler(Looper.getMainLooper());

    public MqttStocksNotificationRepository() {
        mStocksSubject = PublishSubject.create();

        try {
            mClient = new MqttClient(MQTT_SERVER_URI,
                    MqttClient.generateClientId(),
                    new MemoryPersistence());

            mClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    String message = new String(mqttMessage.getPayload());
                    Log.d("MQTT", topic + ": " + message);
                    publishStocks(message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            mClient.connect();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(String... topics) {
        try {
            if (mClient != null) {
                int[] qos = new int[topics.length];
                for (int i = 0; i < topics.length; i++) {
                    qos[i] = 1;
                }
                mClient.subscribe(topics, qos);
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(String... topics) {
        try {
            if (mClient != null) {
                mClient.unsubscribe(topics);
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribeAll() {
        try {
            if (mClient != null) {
                mClient.unsubscribe("dale/stocks/*");
            }
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<List<Stock>> getStocks() {
        return mStocksSubject.asObservable();
    }

    private void publishStocks(String message) {
        Stock stock;
        StockList stockList;
        Log.d("MQTT", "MQTT start parsing " + message);
        if((stock = ParseUtility.parseSingleStockFromJson(message)) != null) {
            publishSingleStock(stock);
        }
        else if((stockList = ParseUtility.parseStockListFromJson(message)) != null) {
            publishStockList(stockList);
        }
        Log.d("MQTT", "MQTT finish parsing ");
    }

    private void publishSingleStock(final Stock stock) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("MQTT", "MQTT start post " + Looper.myLooper().getThread().getName());
                List<Stock> stocks = new ArrayList<Stock>();
                stocks.add(stock);
                Log.d("MQTT", "MQTT post" + stocks.size());
                mStocksSubject.onNext(stocks);
            }
        });
    }

    private void publishStockList(final StockList stockList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("MQTT", "MQTT start post " + Looper.myLooper().getThread().getName());
                List<Stock> stocks = stockList.getStockList();
                Log.d("MQTT", "MQTT post" + stocks.size());
                mStocksSubject.onNext(stocks);
            }
        });
    }
}
