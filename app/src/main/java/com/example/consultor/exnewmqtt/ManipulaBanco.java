package com.example.consultor.exnewmqtt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Consultor on 09/07/2017.
 */
public class ManipulaBanco {
    SQLiteDatabase bancoDados = null;
    //Objeto para criar seu banco
    private String nome_banco = "base";
    //nome do seu banco
    private String SQL_CREATE;
    //string utilizada para criar a tabela
    Context context;



    public  ManipulaBanco(Context c)
    {
        context = c;
    }


    public void abreouCriaBanco(Context ctx,SQLiteDatabase db)
    {
        try //lembrar que é sempre bom colocar try quando for trabalhar com dados
        {
            //cria a base
            db = ctx.openOrCreateDatabase(nome_banco,Context.MODE_PRIVATE, null);
            SQL_CREATE = "Create table if not exists terremoto (id INTEGER PRIMARY KEY autoincrement, Sensor Text, Valor Text, DataHora Text)";
            db.execSQL(this.SQL_CREATE);
            SQL_CREATE = "Create table if not exists temperatura (id INTEGER PRIMARY KEY autoincrement, Sensor Text, Valor Text, DataHora Text)";
            db.execSQL(this.SQL_CREATE);
            SQL_CREATE = "Create table if not exists luminosidade (id INTEGER PRIMARY KEY autoincrement, Sensor Text, Valor Text, DataHora Text)";
            db.execSQL(this.SQL_CREATE);
            SQL_CREATE = "Create table if not exists geolocalizacao (id INTEGER PRIMARY KEY autoincrement, Sensor Text, Ca Text, Cb Text, DataHora Text)";
            db.execSQL(this.SQL_CREATE);

            db.close();
            Log.d("Funcionou","Criou base de dados");



        }
        catch(Exception erro)
        {
            Log.d("Erro","Ao criar base de dados");
            //mensagem("Erro ao abrir ou criar o banco", "Erro");
        }

    }


    public void executaSql(Context ctx, SQLiteDatabase db, String sql)
    {
        try //lembrar que é sempre bom colocar try quando for trabalhar com dados
        {
            //cria a base
            Log.d("Sql",sql);




            db = ctx.openOrCreateDatabase(nome_banco,Context.MODE_PRIVATE, null);
            db.execSQL(sql); //Criando tabela caso não exista!!
            db.close();
            Log.d("Inseriu",sql);
        }
        catch(Exception erro)
        {
            Log.d("Erro ao executar ",sql);
            //mensagem("Erro ao executar sql", "Erro");
        }

    }

    public Cursor consulta(Context ctx, SQLiteDatabase db, String tabela)
    {
        try //lembrar que é sempre bom colocar try quando for trabalhar com dados
        {
            db = ctx.openOrCreateDatabase(nome_banco,Context.MODE_PRIVATE,null);
            //db = ctx.get
            //aqui ele faz a consulta e retorna o resultado no cursor (dataset)
            //Pessoas (id INTEGER PRIMARY KEY, NOME
            Cursor cursor = db.query(tabela, null, null, null, null, null, null, null);
            return cursor;
            //esse cursor possui varios metodos que podem ser acessados
            //como o abaixo que conta linhas e se diferente de zero
        }
        catch(Exception erro)
        {
            Log.d("Erro ao consultar sql", "Erro");
            return null;
        }
    }



}
