package com.dtlim.bantaystocks.data.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dtlim.bantaystocks.common.utility.DateUtility;
import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.model.StockList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
    //public static final String MQTT_SERVER_URI = "tcp://broker.mqttdashboard.com:1883";
    public static final String MQTT_USERNAME = "dtlim";
    public static final String MQTT_PASSWORD = "password";

    private MqttClient mClient;
    PublishSubject<List<Stock>> mStocksSubject;
    Handler handler = new Handler(Looper.getMainLooper());

    public MqttStocksNotificationRepository() {
        mStocksSubject = PublishSubject.create();
    }

    @Override
    public void connect() throws Throwable {
        mClient = new MqttClient(MQTT_SERVER_URI,
                MqttClient.generateClientId(),
                new MemoryPersistence());

        mClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                emitError(throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                String message = new String(mqttMessage.getPayload());
                Log.d("MQTT", topic + ": " + message);
                emitStocks(message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(MQTT_USERNAME);
        options.setPassword(MQTT_PASSWORD.toCharArray());
        options.setConnectionTimeout(MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT);
        options.setKeepAliveInterval(MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT);
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        mClient.connect(options);
    }

    @Override
    public void subscribe(String... topics) throws Exception{
        if (mClient != null) {
            int[] qos = new int[topics.length];
            for (int i = 0; i < topics.length; i++) {
                qos[i] = 1;
            }
            mClient.subscribe(topics, qos);
        }
    }

    @Override
    public void unsubscribe(String... topics) throws Exception{
        if (mClient != null) {
            mClient.unsubscribe(topics);
        }
    }

    @Override
    public void unsubscribeAll() throws Exception{
        if (mClient != null) {
            mClient.unsubscribe("dale/stocks/*");
        }
    }

    @Override
    public Observable<List<Stock>> getStocks() {
        return mStocksSubject.asObservable();
    }

    private void emitStocks(String message) {
        Stock stock;
        StockList stockList;
        if((stock = ParseUtility.parseSingleStockFromJson(message)) != null && stock.isValid()) {
            emitSingleStock(stock);
        }
        else if((stockList = ParseUtility.parseStockListFromJson(message)) != null) {
            emitStockList(stockList);
        }
    }

    private void emitSingleStock(final Stock stock) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Stock> stocks = new ArrayList<Stock>();
                stocks.add(stock);
                stock.setLastUpdate(System.currentTimeMillis() + "");
                mStocksSubject.onNext(stocks);
            }
        });
    }

    private void emitStockList(final StockList stockList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Stock> stocks = stockList.getStockList();
                for(int i=0; i<stocks.size(); i++) {
                    stocks.get(i).setLastUpdate(
                            DateUtility.parseApiToUnixTimestamp(stockList.getAsOf()));
                }
                mStocksSubject.onNext(stocks);
            }
        });
    }

    private void emitError(final Throwable throwable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mStocksSubject.onError(throwable);
            }
        });
    }
}
