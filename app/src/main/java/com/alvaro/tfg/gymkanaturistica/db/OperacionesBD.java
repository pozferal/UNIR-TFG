package com.alvaro.tfg.gymkanaturistica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.alvaro.tfg.gymkanaturistica.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * Created by Alvaro on 27/04/2017.
 */

public class OperacionesBD {


    private static GymkanaDB baseDatos;

    private static OperacionesBD instancia=new OperacionesBD();

    private OperacionesBD(){

    }

    public static OperacionesBD obtenerInstancia(Context contexto){
        if (baseDatos==null){
            baseDatos = new GymkanaDB(contexto,MainActivity.DATABASE_NAME, null, MainActivity.DATABASE_VERSION);
        }
        return instancia;
    }

    public Cursor obtenerRutas(){
        SQLiteDatabase db=baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();

        builder.setTables(RUTA_JOIN_TIPO_RUTA);
        //db.endTransaction();
        //db.close();
        return builder.query(db,proyRuta,null,null,null,null,null);


    }

    public Cursor obtenerPois(){
        SQLiteDatabase db=baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
        builder.setTables("POIS");
        return builder.query(db,proyPoi,null,null,null,null,null);

    }

    public Cursor obtenerRespuestas(){
        SQLiteDatabase db=baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
        builder.setTables("RESPUESTAS");
        return builder.query(db,proyRespuestas,null,null,null,null,null);
    }

    public List<DatosRuta> obtenerListaRutas(){

        SQLiteDatabase basedatos=baseDatos.getReadableDatabase();
        List<DatosRuta> resultado=new ArrayList<DatosRuta>();

        if (basedatos!=null){
            String sentencia="Select " +
                    "RUTAS.RUTA_ID AS RUTA_ID, RUTAS.DESCRIPCION_RUTA AS DESCRIPCION_RUTA, TIPOS_RUTAS.TIPO_RUTA_ID AS TIPO_RUTA_ID" +
                    ", TIPOS_RUTAS.DESCRIPCION AS DESCRIPCION, RUTAS.ACTIVA AS ACTIVA," +
                    " RUTAS.COMPLETADA AS COMPLETADA, " +
                    " RUTAS.PUNTUACION AS PUNTUACION," +
                    " RUTAS.DURACION AS DURACION, RUTAS.DISTANCIA AS DISTANCIA, RUTAS.IDENTIFICACION AS IDENTIFICACION" +
                    " from RUTAS INNER JOIN TIPOS_RUTAS ON RUTAS.TIPOS_RUTAS_TIPO_RUTA_ID=TIPOS_RUTAS.TIPO_RUTA_ID";

//(SELECT Sum(PUNTOS) FROM POIS WHERE POIS.RUTAS_RUTA_ID = RUTAS.RUTA_ID  )

            Log.i(MainActivity.TAG, "SEntencia==="+sentencia);

            Cursor result=basedatos.rawQuery(sentencia,null);
            if (result.moveToFirst()){
                do {
                    DatosRuta objRutaOT = new DatosRuta();
                    objRutaOT.setIdRuta(result.getInt(result.getColumnIndex("RUTA_ID")));
                    objRutaOT.setDescripcion_ruta(result.getString(result.getColumnIndex("DESCRIPCION_RUTA")));
                    objRutaOT.setTipo_Ruta_id(result.getInt(result.getColumnIndex("TIPO_RUTA_ID")));
                    objRutaOT.setDescripcion(result.getString(result.getColumnIndex("DESCRIPCION_RUTA")));
                    objRutaOT.setDuracion(result.getString(result.getColumnIndex("DURACION")));
                    objRutaOT.setDistancia(result.getString(result.getColumnIndex("DISTANCIA")));
                    objRutaOT.setIdentificacion(result.getString(result.getColumnIndex("IDENTIFICACION")));

                    if (result.getInt(result.getColumnIndex("ACTIVA"))==new Integer(1)){
                        objRutaOT.setActiva(true);
                    }else{
                        objRutaOT.setActiva(false);
                    }

                    if (result.getInt(result.getColumnIndex("COMPLETADA"))==new Integer(1)){
                        objRutaOT.setCompletada(true);
                    }else{
                        objRutaOT.setCompletada(false);
                    }
                    objRutaOT.setPuntuacion(new Integer(result.getString(result.getColumnIndex("PUNTUACION"))));
                    Log.i(MainActivity.TAG, "Puntoss==="+result.getInt(result.getColumnIndex("PUNTUACION")));
                    resultado.add(objRutaOT);
                }while (result.moveToNext());
            }


        }
        return resultado;
    }

    public Cursor obtenerTiposRutas(){
        SQLiteDatabase db=baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();

        builder.setTables("TIPOS_RUTAS");

        //db.endTransaction();
        //db.close();
        return builder.query(db,proyTipoRuta,null,null,null,null,null);

    }

    public DatosTipoRuta obtenerTiposRutaporNombre(String nombre){

        DatosTipoRuta tipoRutaOT =null;
        SQLiteDatabase db=baseDatos.getReadableDatabase();
        String[] valores_recuperar = {"TIPO_RUTA_ID", "DESCRIPCION"};
        String[] selectionArgs = {nombre};

        Log.d(MainActivity.TAG, "voy a por el tipo de ruta: "+nombre);
        Cursor c = db.query("TIPOS_RUTAS", valores_recuperar, "DESCRIPCION=?", selectionArgs,
                null, null, null);
        if(c != null && c.moveToFirst()) {

            tipoRutaOT = new DatosTipoRuta(c.getInt(0), c.getString(1));
            c.close();
        }

        //db.endTransaction();
        //db.close();

        return tipoRutaOT;

    }


    public DatosRuta obtenerRutaPorNombre(String nombre){

        SQLiteDatabase bd = baseDatos.getReadableDatabase();
        DatosRuta resultado = null;
//Si hemos abierto correctamente la base de datos
        if(bd != null)
        {
// BD ABIERTA OK
            String [] valor = {nombre};
            String sql = "SELECT * FROM RUTAS ";
            sql += "WHERE DESCRIPCION_RUTA=?";
            Cursor result = bd.rawQuery(sql, valor);
            if (result.moveToFirst())
                do
                {
                    DatosRuta ruta = new DatosRuta();
                    if ((result.getString(result.getColumnIndex("COMPLETADA"))!=null)&& result.getString(result.getColumnIndex("COMPLETADA")).equals("TRUE")) {
                        ruta.setCompletada(true);
                    }else{
                        ruta.setCompletada(false);
                    }

                    if (result.getString(result.getColumnIndex("ACTIVA"))!=null && result.getString(result.getColumnIndex("ACTIVA")).equals("1")) {
                        ruta.setActiva(true);
                    }else{
                        ruta.setActiva(false);
                    }

                    ruta.setDescripcion(result.getString(result.getColumnIndex("DESCRIPCION_RUTA")));
                    ruta.setIdRuta(result.getInt(result.getColumnIndex("RUTA_ID")));
                    ruta.setModo(result.getString(result.getColumnIndex("MODO")));
                    if (result.getString(result.getColumnIndex("PUNTUACION"))!=null) {
                        ruta.setPuntuacion(new Integer(result.getString(result.getColumnIndex("PUNTUACION"))));
                    }
                    ruta.setTipo_Ruta_id(result.getInt(result.getColumnIndex("TIPOS_RUTAS_TIPO_RUTA_ID")));
                    ruta.setDuracion(result.getString(result.getColumnIndex("DURACION")));
                    ruta.setIdentificacion(result.getString(result.getColumnIndex("IDENTIFICACION")));
                    ruta.setDistancia(result.getString(result.getColumnIndex("DISTANCIA")));

                    resultado=ruta;
                } while(result.moveToNext());

        }
        return resultado;



    }

    public Cursor obtenerRutaPorId(String id){
        SQLiteDatabase db=baseDatos.getWritableDatabase();
        String selection= String.format("%s=?", "RUTA_ID");
        String [] selectionArgs={id};

        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
        builder.setTables(RUTA_JOIN_TIPO_RUTA);
        //db.endTransaction();
        //db.close();
        return builder.query(db,proyRuta,selection,selectionArgs,null,null,null);


    }

    public String insertarTipoRuta (DatosTipoRuta tipoRuta){
        SQLiteDatabase db=baseDatos.getWritableDatabase();

        ContentValues valores=new ContentValues();
        valores.put("DESCRIPCION", tipoRuta.getDescripcion());
        //valores.put(GymkanaContract.TiposRuta.TIPO_RUTA_ID, tipoRuta.getTipoRuta_id());

        db.insertOrThrow("TIPOS_RUTAS", null, valores);
        //db.endTransaction();
        //db.close();

        return String.format ("%s#%s", tipoRuta.getTipoRuta_id().toString(), tipoRuta.getDescripcion());
    }

    public String insertarRuta (DatosRuta ruta){
        SQLiteDatabase db=baseDatos.getWritableDatabase();
        ContentValues valores=new ContentValues();
        valores.put("DESCRIPCION_RUTA", ruta.getDescripcion());
        valores.put("MODO", ruta.getModo());
        valores.put("TIPOS_RUTAS_TIPO_RUTA_ID",ruta.getTipo_Ruta_id());
        valores.put("DURACION", ruta.getDuracion());
        valores.put("DISTANCIA", ruta.getDistancia());
        valores.put("IDENTIFICACION", ruta.getIdentificacion());
        valores.put("ORDEN_SIGUIENTE_POI", ruta.getOrden_sigiuente_poi());
        valores.put("PUNTUACION",0);
        db.insertOrThrow("RUTAS", null, valores);
        //db.endTransaction();
        //db.close();
        return ruta.getDescripcion();
    }


    public String insertarPoi (DatosPoi poi){
        SQLiteDatabase db=baseDatos.getWritableDatabase();
        ContentValues valores=new ContentValues();

        valores.put("RUTAS_RUTA_ID", poi.getId_ruta());
        valores.put("ORDEN",new Integer(poi.getOrden()));
        valores.put("NOMBRE", poi.getNombre());
        valores.put("DESCRIPCION",poi.getDescripcion());
        valores.put("LONGITUD",new Float(poi.getLongitud()));
        valores.put("LATITUD", new Float(poi.getLatitud()));
        valores.put("IMAGEN", poi.getImg());
        valores.put("MASINFO", poi.getMasinfo());
        valores.put("CATEGORIA", poi.getCategoria());
        valores.put("PUNTOS", new Integer(poi.getPuntos()));
        valores.put("ORDENSIGUIENTE", new Integer(poi.getOrdenSiguientePoi()));
        valores.put("PREGUNTA", poi.getPregunta());

        db.insertOrThrow("POIS", null, valores);



        Integer poi_id=null;
        String sentencia = "SELECT seq FROM sqlite_sequence WHERE name='POIS'";
        Log.i(MainActivity.TAG, "Sentencia="+sentencia);
        Cursor result = db.rawQuery(sentencia, null);
        if (result.moveToFirst()) {
            do {
                poi_id=result.getInt(result.getColumnIndex("seq"));

            }while (result.moveToNext());

        }
        Log.i(MainActivity.TAG,"id_poi========"+poi_id);
        List<DatosRespuesta> respuestas=poi.getRespuestas();
        ContentValues valoresRes=new ContentValues();
        for(int i=0; i<respuestas.size(); i++){
            valoresRes.put("DESCRIPCION", respuestas.get(i).getTexto()   );
            valoresRes.put("ESCORRECTO", respuestas.get(i).getCorrecta()   );
            valoresRes.put("POIS_POI_ID",poi_id);
            db.insertOrThrow("RESPUESTAS",null,valoresRes);
        }



                //db.endTransaction();
        //db.close();
        return poi.getNombre();
    }






    private static final String RUTA_JOIN_TIPO_RUTA="RUTAS INNER JOIN TIPOS_RUTAS ON RUTAS.TIPOS_RUTAS_TIPO_RUTA_ID=TIPOS_RUTAS.TIPO_RUTA_ID";

    private static final String[] proyRuta = new String[]{
            "RUTA_ID",
            "DESCRIPCION_RUTA",
            "TIPO_RUTA_ID",
            "DESCRIPCION",
            "DURACION",
            "DISTANCIA",
            "IDENTIFICACION",
            "ORDEN_SIGUIENTE_POI"

    };

    private static final String[] proyTipoRuta=new String[]{
            "TIPO_RUTA_ID",
            "DESCRIPCION"
    };

    private static final String[] proyRespuestas = new String[]{
            "POIS_POI_ID",
            " DESCRIPCION",
            " ESCORRECTO"
    };

    private static final String[] proyPoi= new String[]{
            "POI_ID" ,
            "RUTAS_RUTA_ID",
            "ORDEN",
            "NOMBRE",
            "DESCRIPCION",
            "LONGITUD",
            "LATITUD",
            "IMAGEN",
            "MASINFO",
            "CATEGORIA",
            "PUNTOS",
            "ORDENSIGUIENTE",
            "PREGUNTA"
    };

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    public void setGymkanaActiva(String identificador) {
        SQLiteDatabase db=baseDatos.getWritableDatabase();
        if (db!=null){
            String sentencia="UPDATE RUTAS SET ACTIVA=1 WHERE IDENTIFICACION='"+identificador+"'";
            db.execSQL(sentencia);
            sentencia="UPDATE RUTAS SET ACTIVA=0 WHERE IDENTIFICACION!='"+identificador+"'";
            db.execSQL(sentencia);
            db.close();
        }
    }

    public DatosRuta getRutaActiva() {
        SQLiteDatabase db=baseDatos.getReadableDatabase();
        DatosRuta datosRuta = null;
        if (db!=null){
            String [] activa= {"1"};
            String sentencia="SELECT * FROM RUTAS WHERE ACTIVA=?";
            Cursor result=db.rawQuery(sentencia,activa);
            if (result.moveToFirst()){
                do{
                    datosRuta=new DatosRuta();
                    datosRuta.setTipo_Ruta_id(result.getInt(result.getColumnIndex("TIPOS_RUTAS_TIPO_RUTA_ID")));
                    datosRuta.setDescripcion(result.getString(result.getColumnIndex("DESCRIPCION_RUTA")));
                    if (result.getInt(result.getColumnIndex("ACTIVA"))==new Integer(1)){
                        datosRuta.setActiva(true);
                    }else{
                        datosRuta.setActiva(false);
                    }
                    if (result.getInt(result.getColumnIndex("COMPLETADA"))==new Integer(1)){
                        datosRuta.setCompletada(true);
                    }else{
                        datosRuta.setCompletada(false);
                    }
                    datosRuta.setPuntuacion(result.getInt(result.getColumnIndex("PUNTUACION")));
                    datosRuta.setModo(result.getString(result.getColumnIndex("MODO")));
                    datosRuta.setDuracion(result.getString(result.getColumnIndex("DURACION")));
                    datosRuta.setDistancia(result.getString(result.getColumnIndex("DISTANCIA")));
                    datosRuta.setIdentificacion(result.getString(result.getColumnIndex("IDENTIFICACION")));
                    datosRuta.setIdRuta(result.getInt(result.getColumnIndex("RUTA_ID")));
                    datosRuta.setOrden_sigiuente_poi(result.getInt(result.getColumnIndex("ORDEN_SIGUIENTE_POI")));

                }while (result.moveToNext());

            }
            db.close();

        }


        return datosRuta;

    }


    public DatosPoi siguientePoi(Integer idRuta, Integer ordenSiguientePoi){
        SQLiteDatabase db=baseDatos.getWritableDatabase();
        DatosPoi datosPoi = null;
        DatosRespuesta datosRespuesta =null;
        List<DatosRespuesta> respuestas = new ArrayList<DatosRespuesta>();

        db.beginTransaction();
        if (db!=null) {
           // String[] valores = {idRuta.toString(), ordenSiguientePoi.toString()};
            String sentencia = "SELECT * FROM POIS WHERE RUTAS_RUTA_ID="+idRuta.toString() + " AND ORDEN="+ordenSiguientePoi.toString();
            Log.i(MainActivity.TAG, "Sentencia="+sentencia);
            Cursor result = db.rawQuery(sentencia, null);
            if (result.moveToFirst()) {
                do {


                    datosPoi = new DatosPoi();

                    datosPoi.setIdPoi(result.getInt(result.getColumnIndex("POI_ID")));
                    datosPoi.setOrden(result.getInt(result.getColumnIndex("ORDEN")));
                    datosPoi.setNombre(result.getString(result.getColumnIndex("NOMBRE")));
                    datosPoi.setDescripcion(result.getString(result.getColumnIndex("DESCRIPCION")));
                    datosPoi.setLatitud(result.getDouble(result.getColumnIndex("LATITUD")));
                    datosPoi.setLongitud(result.getDouble(result.getColumnIndex("LONGITUD")));
                    datosPoi.setImg(result.getString(result.getColumnIndex("IMAGEN")));
                    datosPoi.setMasinfo(result.getString(result.getColumnIndex("MASINFO")));
                    datosPoi.setCategoria(result.getString(result.getColumnIndex("CATEGORIA")));
                    datosPoi.setPuntos(result.getInt(result.getColumnIndex("PUNTOS")));
                    datosPoi.setId_ruta(idRuta);
                    datosPoi.setOrdenSiguientePoi(result.getInt(result.getColumnIndex("ORDENSIGUIENTE")));
                    datosPoi.setPregunta(result.getString(result.getColumnIndex("PREGUNTA")));



                    sentencia = "SELECT * FROM RESPUESTAS WHERE POIS_POI_ID="+datosPoi.getIdPoi();
                    Log.i(MainActivity.TAG, "Sentencia="+sentencia);
                    Cursor resultRespuestas = db.rawQuery(sentencia, null);
                    if (resultRespuestas.moveToFirst()) {
                        do {
                            datosRespuesta = new DatosRespuesta();
                            datosRespuesta.setTexto(resultRespuestas.getString(resultRespuestas.getColumnIndex("DESCRIPCION")));
                            datosRespuesta.setCorrecta(resultRespuestas.getInt(resultRespuestas.getColumnIndex("ESCORRECTO")));
                            respuestas.add(datosRespuesta);

                        } while (resultRespuestas.moveToNext());
                    }


                    datosPoi.setRespuestas(respuestas);




                } while (result.moveToNext());
            }




            sentencia="UPDATE RUTAS SET ORDEN_SIGUIENTE_POI="+ ordenSiguientePoi.toString() +" WHERE RUTA_ID="+idRuta.toString();
            db.execSQL(sentencia);

            db.setTransactionSuccessful();
            db.endTransaction();

        }
        db.close();
        return datosPoi;
    }


    public void setRutaCompletada(Integer idRuta) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        db.beginTransaction();

        String sentencia="UPDATE RUTAS SET COMPLETADA=1  WHERE RUTA_ID="+idRuta.toString();
        db.execSQL(sentencia);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }



    public void setPuntosPoi(Integer idPoi, Integer puntos){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        db.beginTransaction();

        String sentencia="UPDATE POIS SET PUNTOS="+puntos+"  WHERE POI_ID="+idPoi;
        db.execSQL(sentencia);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }




    public void setPuntosRuta(Integer idRuta,Integer puntosAcumulados){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        db.beginTransaction();

        String sentencia="UPDATE RUTAS SET PUNTUACION="+puntosAcumulados+"  WHERE RUTA_ID="+idRuta;
        db.execSQL(sentencia);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }



    public void resetGymkana(Integer idRuta){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        db.beginTransaction();

        String sentencia="UPDATE RUTAS SET PUNTUACION=0, COMPLETADA=0, ORDEN_SIGUIENTE_POI=1 WHERE RUTA_ID="+idRuta;
        db.execSQL(sentencia);

        sentencia="UPDATE POIS SET PUNTOS=1 WHERE RUTAS_RUTA_ID="+idRuta;
        db.execSQL(sentencia);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    public List<DatosPoi> obtenerPoisYaAlcanzados(Integer idRuta, Integer rutaTerminada){

        SQLiteDatabase basedatos=baseDatos.getReadableDatabase();
        List<DatosPoi> resultado=new ArrayList<DatosPoi>();

        if (basedatos!=null){
            String sentencia="";
            if(rutaTerminada==0) {

                 sentencia = "Select *" +
                        "" +
                        " from POIS INNER JOIN RUTAS ON RUTAS.RUTA_ID=POIS.RUTAS_RUTA_ID AND POIS.ORDEN < RUTAS.ORDEN_SIGUIENTE_POI AND RUTAS.RUTA_ID=" + idRuta;
            }else{
                 sentencia = "Select *" +
                        "" +
                        " from POIS INNER JOIN RUTAS ON RUTAS.RUTA_ID=POIS.RUTAS_RUTA_ID  AND RUTAS.RUTA_ID=" + idRuta;

            }
            Cursor result=basedatos.rawQuery(sentencia,null);
            if (result.moveToFirst()){
                do {
                    DatosPoi objPoiOT = new DatosPoi();

                    objPoiOT.setIdPoi(result.getInt(result.getColumnIndex("POI_ID")));
                    objPoiOT.setId_ruta(result.getInt(result.getColumnIndex("RUTAS_RUTA_ID")));
                    objPoiOT.setNombre(result.getString(result.getColumnIndex("NOMBRE")));
                    objPoiOT.setDescripcion(result.getString(result.getColumnIndex("DESCRIPCION")));
                    objPoiOT.setLongitud(result.getDouble(result.getColumnIndex("LONGITUD")));
                    objPoiOT.setLatitud(result.getDouble(result.getColumnIndex("LATITUD")));
                    objPoiOT.setImg(result.getString(result.getColumnIndex("IMAGEN")));
                    objPoiOT.setMasinfo(result.getString(result.getColumnIndex("MASINFO")));
                    objPoiOT.setCategoria(result.getString(result.getColumnIndex("CATEGORIA")));
                    objPoiOT.setPuntos(result.getInt(result.getColumnIndex("PUNTOS")));
                    objPoiOT.setOrdenSiguientePoi(result.getInt(result.getColumnIndex("ORDENSIGUIENTE")));
                    objPoiOT.setOrden(result.getInt(result.getColumnIndex("ORDEN")));


                    resultado.add(objPoiOT);
                }while (result.moveToNext());
            }


        }
        return resultado;


    }


    public DatosPoi obtenerPoi(Integer idPoi){
        DatosPoi objPoiOT=null;

        SQLiteDatabase basedatos=baseDatos.getReadableDatabase();

        if (basedatos!=null){
            String sentencia="Select *" +
                    " from POIS WHERE POI_ID="+ idPoi;

            Cursor result=basedatos.rawQuery(sentencia,null);
            if (result.moveToFirst()){
                do {
                     objPoiOT = new DatosPoi();

                    objPoiOT.setIdPoi(result.getInt(result.getColumnIndex("POI_ID")));
                    objPoiOT.setId_ruta(result.getInt(result.getColumnIndex("RUTAS_RUTA_ID")));
                    objPoiOT.setNombre(result.getString(result.getColumnIndex("NOMBRE")));
                    objPoiOT.setDescripcion(result.getString(result.getColumnIndex("DESCRIPCION")));
                    objPoiOT.setLongitud(result.getDouble(result.getColumnIndex("LONGITUD")));
                    objPoiOT.setLatitud(result.getDouble(result.getColumnIndex("LATITUD")));
                    objPoiOT.setImg(result.getString(result.getColumnIndex("IMAGEN")));
                    objPoiOT.setMasinfo(result.getString(result.getColumnIndex("MASINFO")));
                    objPoiOT.setCategoria(result.getString(result.getColumnIndex("CATEGORIA")));
                    objPoiOT.setPuntos(result.getInt(result.getColumnIndex("PUNTOS")));
                    objPoiOT.setOrdenSiguientePoi(result.getInt(result.getColumnIndex("ORDENSIGUIENTE")));
                    objPoiOT.setOrden(result.getInt(result.getColumnIndex("ORDEN")));
                    objPoiOT.setPregunta(result.getString(result.getColumnIndex("PREGUNTA")));




                }while (result.moveToNext());
            }


        }

        return objPoiOT;
    }


    public boolean isRutaExistente(String identificacion){

        boolean rutaExistente=false;

        SQLiteDatabase basedatos=baseDatos.getReadableDatabase();

        if (basedatos!=null) {
            String sentencia = "Select IDENTIFICACION" +
                              " from RUTAS WHERE IDENTIFICACION='" + identificacion+"'";

            Cursor result = basedatos.rawQuery(sentencia, null);
            if (result.moveToFirst()) {
                rutaExistente=true;

            }else{
                rutaExistente=false;
            }
        }
        return rutaExistente;
    }

    public boolean hayRutas(){
        boolean hayRutas=false;
        SQLiteDatabase basedatos=baseDatos.getReadableDatabase();

        if (basedatos!=null) {
            String sentencia = "Select IDENTIFICACION from RUTAS";

            Cursor result = basedatos.rawQuery(sentencia, null);
            if (result.moveToFirst()) {
                hayRutas=true;

            }else{
                hayRutas=false;
            }
        }
        return hayRutas;
    }
}
