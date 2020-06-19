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

public class ListaLumActivity extends ListActivity {

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
        //setContentView(R.layout.activity_lista_lum);
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
        try
        {
        cursor =  manipulaBanco.consulta(this,db, "luminosidade");
        cursor.moveToLast();
        dado[0]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[1]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[2]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[3]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[4]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[5]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[6]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[7]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[8]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[9]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[10]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[11]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[12]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[13]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[14]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        cursor.moveToPrevious();
        dado[15]="Luminosidade: "+ cursor.getString(2) +"°. Recebido em: " + cursor.getString(3);
        }
        catch(Exception e)
        {

        }
    }
}
