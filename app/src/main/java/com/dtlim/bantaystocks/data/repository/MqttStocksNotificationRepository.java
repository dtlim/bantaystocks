package com.dtlim.bantaystocks.data.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dtlim.bantaystocks.data.model.Stock;
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
    private MqttClient mClient;
    PublishSubject mStocksSubject;

    public MqttStocksNotificationRepository() {
        mStocksSubject = PublishSubject.create();

        try {
            mClient = new MqttClient("tcp://broker.mqttdashboard.com:1883",
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

            String[] topics = new String[]{"dale/stocks/2GO", "dale/stocks/TEL"};
            subscribe(topics);
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
    public Observable<List<Stock>> getStocks() {
        return mStocksSubject.asObservable();
    }

    private void publishStocks(String message) {
        final Stock stock = new Gson().fromJson(message, Stock.class);
        Log.d("MQTT", "MQTT finish parsing ");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
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
}
