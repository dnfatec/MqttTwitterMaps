package com.example.consultor.exnewmqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Consultor on 06/07/2017.
 */
public class MQTTService extends Service  {
   // public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
//    public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
   public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */

    //public static final String BROKER_URL = "iot.eclipse.org:1883";
    //public static final String TOPIC = "/danilokaran/topico";
    public static final String TOPIC = "/darkpenguin/disastercontrol";

    public static final String clientId = "android-clienta";
    //public static final String clientId = "/danilokaran/topico";

    //public static final String TOPIC = "de/eclipsemagazin/blackice/warnings";
    private MqttClient mqttClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //Toast.makeText(getApplicationContext(), "antes do try iniciou conexao de servico", Toast.LENGTH_SHORT).show();
        try {

            mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
            //Toast.makeText(getApplicationContext(), "depois do try iniciou conexao de servico", Toast.LENGTH_SHORT).show();
            mqttClient.setCallback(new PushCallback(this, getApplicationContext()));
            mqttClient.connect();

            //Subscribe to all subtopics of homeautomation
            mqttClient.subscribe(TOPIC);


        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}
