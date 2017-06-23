package com.alvaro.tfg.gymkanaturistica.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alvaro.tfg.gymkanaturistica.MainActivity;

/**
 * Created by Alvaro on 27/04/2017.
 */

public class GymkanaDB extends SQLiteOpenHelper {





    String creaTablaTiposRuta= "CREATE TABLE  TIPOS_RUTAS (" +
            "TIPO_RUTA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DESCRIPCION TEXT)";

    String creaTablaRutas="CREATE TABLE RUTAS("
            +  " RUTA_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +  " ACTIVA NUMERIC, "
            +  " COMPLETADA NUMERIC, "
            +  " DESCRIPCION_RUTA TEXT, "
            +  " DURACION TEXT, "
            +  " MODO TEXT, "
            +  " DISTANCIA TEXT, "
            +  " IDENTIFICACION TEXT, "
            +  " PUNTUACION INTEGER, "
            +  " ORDEN_SIGUIENTE_POI INTEGER, "
            +  " TIPOS_RUTAS_TIPO_RUTA_ID INTEGER, "
            +  " FOREIGN KEY( TIPOS_RUTAS_TIPO_RUTA_ID ) "
            +  " REFERENCES TIPOSRUTAS ( TIPO_RUTA_ID) )";


    String creaTablaPOIS =" CREATE TABLE POIS ("
            +  " POI_ID INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +  " RUTAS_RUTA_ID INTEGER   NOT NULL ,"
            +  " ORDEN INTEGER    ,"
            +  " NOMBRE TEXT    ,"
            +  " DESCRIPCION TEXT    ,"
            +  " LONGITUD REAL    ,"
            +  " LATITUD REAL    ,"
            +  " IMAGEN TEXT    ,"
            +  " MASINFO TEXT    ,"
            +  " CATEGORIA TEXT    ,"
            +  " PUNTOS INTEGER    ,"
            +  " ORDENSIGUIENTE INTEGER      ,"
            +  " PREGUNTA TEXT      ,"
            +  " FOREIGN KEY(RUTAS_RUTA_ID)"
            +  " REFERENCES RUTAS(RUTA_ID))";




    String creaTablaRespuestas = "CREATE TABLE RESPUESTAS ("
            +  " RESPUESTA_ID INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +  " POIS_POI_ID INTEGER   NOT NULL ,"
            +  " DESCRIPCION TEXT    ,"
            +  " ESCORRECTO NUMERIC      ,"
            +  " FOREIGN KEY(POIS_POI_ID)"
            +  " REFERENCES POIS(POI_ID))";





    public GymkanaDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(creaTablaTiposRuta);
        db.execSQL(creaTablaRutas);
        db.execSQL(creaTablaPOIS);
        db.execSQL(creaTablaRespuestas);
        Log.i(MainActivity.TAG,"Tablas Creadas");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
