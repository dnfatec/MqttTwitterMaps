package com.example.consultor.exnewmqtt;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;





public class ListaTempActivity extends ListActivity {
    static final String[] dado = new String[] { " ", " ", " ",
            "  ", "  ", "  ", "  ", "  ",
            "  ", "  ", "  ", "  ", "  ",
            "  ", "  ", "  ", "  ", "  " ,
            "  ", "  ", "  ", "  ", "  " ,
            "  ", "  ", "  ", "  ", "  " ,
            "  ", "  ", "  ", "  ", "  " ,
            "  ", "  ", "  ", "  ", "  " ,
            "  ", "  ", "  ", "  ", "  " };

    public SQLiteDatabase db=null;

    public ManipulaBanco manipulaBanco = new ManipulaBanco(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_lista_temp);
        seta20UltimosDados();


        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_itens,
                dado));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void seta20UltimosDados()
    {
        Cursor cursor;
        try{
        cursor =  manipulaBanco.consulta(this,db, "temperatura");
        cursor.moveToLast();
        dado[0]=cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[1]=cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[2]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[3]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[4]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[5]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[6]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[7]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[8]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[9]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[10]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[11]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[12]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[13]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[14]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[15]="Temperatura: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        }
        catch(Exception e)
        {

        }

    }
}
