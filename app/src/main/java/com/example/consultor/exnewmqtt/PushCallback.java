package com.example.consultor.exnewmqtt;

/**
 * Created by Consultor on 06/07/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.util.Log;
import android.widget.EditText;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PushCallback implements MqttCallback {

    private ContextWrapper context;

    NotificationManager manager;
    Notification myNotication;
    EditText edttmsg;

    private Context telavindo;

    public SQLiteDatabase banco=null;



    public PushCallback(ContextWrapper context, Context tela) {

        this.context = context;
        this.telavindo = tela;
    }


    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String vrPay="", sql="";
        Integer numero;
        String c1="", c2="";
        Intent intent = new Intent("com.example.consultor.exnewmqtt.MensagemActivity");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String data = dateFormat.format(date);


        Log.d("payload",(new String(message.getPayload()) + "°"));
        String tipo= new String(message.getPayload());
        if (tipo.length() > 0 )
        {
           // numero =  Integer.valueOf(new String(message.getPayload()).substring(1));
            //if (numero<10 || numero >30)
           // {
            //    Vibrator vib = (Vibrator)telavindo.getSystemService(telavindo.VIBRATOR_SERVICE);
             //   vib.vibrate(500);

            //}
            char sendor=tipo.charAt(0);
            Log.d("payload",(Character.toString(sendor)));
            if (sendor =='1')
            {
                sendor=tipo.charAt(1);
                if (sendor=='M') {
                    vrPay=new String(message.getPayload()).substring(2);
                    Log.d("payload", "Entrou if temp");
                    Log.d("payload","Entrou if terremot");
                    sql = "Insert into terremoto (Sensor, Valor, DataHora) values(" +
                            "'2', '" + vrPay + "', " +
                            "'"+ data + "')";


                }
                if (sendor=='C')
                {
                    sendor=tipo.charAt(2);
                    if (sendor=='1')
                    {
                        c1=(new String(message.getPayload()).substring(2));
                    }
                    else
                    {
                        c2=(new String(message.getPayload()).substring(2));
                    }

                    sql = "Insert into geolocalizacao (Sensor, Ca, Cb, DataHora) values(" +
                            "'1', '" + c1.toString() + "',' " +  c1 + "'," + c2 + ",' "+ data + "')";

                }


            }
            else if (sendor == '2')
            {
                sendor=tipo.charAt(1);
                if (sendor=='M') {
                    vrPay=new String(message.getPayload()).substring(2);
                    Log.d("payload", "Entrou if temp");
                    Log.d("payload","Entrou if terremot");
                    sql = "Insert into temperatura (Sensor, Valor, DataHora) values(" +
                            "'2', '" + vrPay + "', " +
                            "'"+ data + "')";


                }
                if (sendor=='C')
                {
                    sendor=tipo.charAt(2);
                    if (sendor=='1')
                    {
                        c1=(new String(message.getPayload()).substring(2));
                    }
                    else
                    {
                        c2=(new String(message.getPayload()).substring(2));
                    }

                    sql = "Insert into geolocalizacao (Sensor, Ca, Cb, DataHora) values(" +
                            "'1', '" + c1.toString() + "',' " +  c1 + "'," + c2 + ",' "+ data + "')";

                }

            }
            else if (sendor == '3')
            {
                Log.d("payload","Entrou if luminos");
                sendor=tipo.charAt(1);
                if (sendor=='M') {
                    vrPay=new String(message.getPayload()).substring(2);
                    Log.d("payload", "Entrou if temp");
                    Log.d("payload","Entrou if terremot");
                    sql = "Insert into luminosidade (Sensor, Valor, DataHora) values(" +
                            "'2', '" + vrPay + "', " +
                            "'"+ data + "')";


                }
                if (sendor=='C')
                {
                    sendor=tipo.charAt(2);
                    if (sendor=='1')
                    {
                        c1=(new String(message.getPayload()).substring(2));
                    }
                    else
                    {
                        c2=(new String(message.getPayload()).substring(2));
                    }

                    sql = "Insert into geolocalizacao (Sensor, Ca, Cb, DataHora) values(" +
                            "'1', '" + c1.toString() + "',' " +  c1 + "'," + c2 + ",' "+ data + "')";

                }

            }
            else
            {
                Log.d("payload","Entrou else nada");
            }

        }


        if (sql.length() >0) {
            try {
                ManipulaBanco bancoa = new ManipulaBanco(telavindo);

                bancoa.abreouCriaBanco(telavindo, banco);
                Log.d("VarSql", sql);
                bancoa.executaSql(telavindo, banco, sql);

            } catch (Exception erro) {
                Log.d("Erro ao montar sql", "banco");
            }
        }
        else
        {
            Log.d("payload","sem informacao");
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


}
