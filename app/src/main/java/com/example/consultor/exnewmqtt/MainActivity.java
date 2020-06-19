package com.example.consultor.exnewmqtt;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;


import org.eclipse.paho.client.mqttv3.MqttClient;

import android.Manifest;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    //public static final String SERVICE_CLASSNAME = "de.eclipsemagazin.mqtt.push.MQTTService";
    public static final String SERVICE_CLASSNAME = "com.example.consultor.exnewmqtt.MQTTService";
    //public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    //public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
    //public static final String BROKER_URL = "iot.eclipse.org:1883";
//    public static final String TOPIC = "/danilokaran/topico";
    public static final String TOPIC = "/darkpenguin/disastercontrol";
    //public static final String TOPIC = "de/eclipsemagazin/blackice/warnings";
    private MqttClient client;

    private Button btLer, btCarregar;

    public TextView edTemp, edLum, edTer, edMens;

    Context context=this;

    public SQLiteDatabase db=null;

    public ManipulaBanco manipulaBanco = new ManipulaBanco(this);

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private LatLng localizacao = new LatLng(-23.6026106, -48.0459634);

    private double ad=30;

    public TwitterLoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLer = (Button) findViewById(R.id.btnLer);
        btCarregar = (Button)findViewById(R.id.btnCarregar);
        edTemp = (TextView)findViewById(R.id.edtTemperatura);
        edTer = (TextView)findViewById(R.id.edtTerremoto);
        edLum = (TextView)findViewById(R.id.edtLuminosidade);
        edMens = (TextView)findViewById(R.id.edtSituacao);



        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.nossomapa); //ira receber o suporte para nosso mapa
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        updateButton();


        btCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregaDados();
            }
        });


        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("5WVPZYJokDALkjk8p30GElhTd", "btPnzUUcLd6ncDdBo4szohL4es7nGRfmFIt6mvWiOLwuZJqZOM"))
                .debug(true)
                .build();
        Twitter.initialize(config);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new MainActivity.LoginHandler());

    }

    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {

            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            final Intent intent = new ComposerActivity.Builder(MainActivity.this)
                    .session(session)
                    .text(edTemp.getText().toString() + " " + edTer.getText().toString() + " " + edLum.getText().toString())
                    .hashtags("#TesteTrer")
                    .createIntent();
            startActivity(intent);
        }
        @Override
        public void failure(TwitterException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateButton();
    }

    private void updateButton() {
        if (serviceIsRunning()) {
            btLer.setText("Parar");

            btLer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btLer.setText("Atualizar");
                    //Toast.makeText(getApplicationContext(), "if start", Toast.LENGTH_SHORT).show();
                    stopBlackIceService();
                    updateButton();
                    carregaDados();
                }
            });

        } else {
            btLer.setText("Captar");
            btLer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btLer.setText("Parar");
                    //Toast.makeText(getApplicationContext(), "else", Toast.LENGTH_SHORT).show();

                    startBlackIceService();

                    updateButton();
                    carregaDados();
                }
            });
        }

    }

    private void startBlackIceService() {

        final Intent intent = new Intent(context, MQTTService.class);
        //Toast.makeText(getApplicationContext(), "inicia servico", Toast.LENGTH_SHORT).show();
        startService(intent);
    }

    private void stopBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        stopService(intent);
    }

    private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //tipo de mapa

        //Latitude e longitude fatec
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localizacao, 18);
        //como dis o metodo latitude e longitude com o zoom
        map.animateCamera(update);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    //conecta a api do google
    }

    private void analisa()
    {

        Random gerador = new Random();
        double soma= (gerador.nextInt(2));


        Random gerador1 = new Random();
        int pos= (gerador.nextInt(4));


        double  c1=-23.512369, c2=-47.462682;
        ;
        String teste="";
        Cursor cursor;

        try {
        cursor =  manipulaBanco.consulta(context,db, "geolocalizacao");

            if (cursor.getCount() != 0) {
                cursor.moveToLast();
                teste=String.valueOf(cursor.getString(2));
                Log.d("LocC1", teste.toString());
                teste=String.valueOf(cursor.getString(3));
                Log.d("LocC2", teste.toString());

                c1 = Double.valueOf(cursor.getString(2));
                cursor.moveToPrevious();
                c2 = Double.valueOf(cursor.getString(3));
                Log.d("Geolo banc",String.valueOf(c1));
                c1 = c1 + soma;
                c2 = c2 + soma;
                ad= ad+30;
            }
        }
            catch(Exception erro)
            {
                if (pos  ==0)
                {
                    c1 = -23.478040;
                    c2 =-47.487112;
                }
                else if (pos==1)
                {
                    c1 = -23.478058;
                    c2 =-47.424352;
                }
                else if (pos==2)
                {
                    c1 = -23.512369;
                    c2 =-47.462682;

                }
                else if (pos==3)
                {
                    c1 = -23.582496;
                    c2 =-47.523368;

                }
                else
                {
                    c1=-23.512369;
                    c2=-47.462682;
                }
                //c1=-23.512369+soma;
                //c2=-47.462682+soma;
                Log.d("Geolo","error");
                //mensagem("Erro ao abrir ou criar o banco", "Erro");
            }





        LatLng latLng = new LatLng(c1, c2);

        Log.d("LocC1", teste.toString());
        teste=String.valueOf(c2);
        Log.d("LocC2", teste.toString());

        try {
            String msg="";
            msg = "";
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng).title("Acontecimento").snippet(msg);
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
            Marker marker = map.addMarker(markerOptions);
        }
        catch(Exception erro)
        {
        }

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.animateCamera(update);
        //map.addMarker(new MarkerOptions().position(new LatLng(c1,c2)).title("Marker").snippet("Snippet")); funcionando

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menuac, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id==R.id.menu01)
        {
         Intent tela = new Intent(MainActivity.this, ListaTempActivity.class);
            startActivity(tela);
        }
        if (id==R.id.menu02)
        {
            Intent tela = new Intent(MainActivity.this, ListaTerActivity.class);
            startActivity(tela);

        }
        if (id==R.id.menu03)
        {
            Intent tela = new Intent(MainActivity.this, ListaLumActivity.class);
            startActivity(tela);

        }

        return super.onOptionsItemSelected(item);
    }

    private void carregaDados()
    {

        Cursor cursor;
        try {
            cursor = manipulaBanco.consulta(context, db, "temperatura");
            cursor.moveToLast();
            edTemp.setText(cursor.getString(2));

            cursor = manipulaBanco.consulta(context, db, "terremoto");
            cursor.moveToLast();
            edTer.setText(cursor.getString(2));


            cursor = manipulaBanco.consulta(context, db, "luminosidade");
            cursor.moveToLast();
            edLum.setText(cursor.getString(2));
        }
        catch (Exception e)
        {

        }
        analisa();
    }


}
