package layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alvaro.tfg.gymkanaturistica.MainActivity;
import com.alvaro.tfg.gymkanaturistica.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alvaro.tfg.gymkanaturistica.db.DatosPoi;
import com.alvaro.tfg.gymkanaturistica.db.DatosRespuesta;
import com.alvaro.tfg.gymkanaturistica.db.OperacionesBD;
import com.alvaro.tfg.gymkanaturistica.db.DatosRuta;
import com.alvaro.tfg.gymkanaturistica.db.DatosTipoRuta;
import com.alvaro.tfg.gymkanaturistica.util.GymkanaXmlParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Descargar extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View rootView=null;
    OperacionesBD datos;
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    ProgressDialog pdialog;
    TextView txtExitoDescarga=null;
    Button btnEscoger=null;


    public static boolean refreshDisplay = true;

    public static String sPref = null;

    private NetworkReceiver receiver = new NetworkReceiver();



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Descargar() {
        // Required empty public constructor
    }


    public static Descargar newInstance(String param1, String param2) {
        Descargar fragment = new Descargar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         rootView=inflater.inflate(R.layout.fragment_descargar,container,false);

        txtExitoDescarga = (TextView)rootView.findViewById(R.id.txtExitoDescarga);
        txtExitoDescarga.setVisibility(View.INVISIBLE);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        getActivity().registerReceiver(receiver, filter);
        EditText url=(EditText)rootView.findViewById(R.id.txtUrl) ;

        url.setText(R.string.urltext);


        Button btnDescargar=(Button)rootView.findViewById(R.id.btnDescargar);

        btnDescargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                datos= OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());

                EditText url=(EditText)rootView.findViewById(R.id.txtUrl) ;

                Log.i(MainActivity.TAG,"url obtenida="+url.getText().toString());

                pdialog = new ProgressDialog(getActivity());
                pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pdialog.setMessage("Procesando...");
                pdialog.setCancelable(true);
                pdialog.setMax(100);
                pdialog.setProgress(0);
                pdialog.show();

                new DownloadXmlTask().execute(url.getText().toString());

            }
        });



        btnEscoger=(Button)rootView.findViewById(R.id.btnEscoger);
        btnEscoger.setVisibility(View.INVISIBLE);
        btnEscoger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Seleccionar fragmentoSeleccionar=new Seleccionar();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenidoFragmentos,fragmentoSeleccionar).commit();


            }
        });



        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            Log.i(MainActivity.TAG,"Cargando contenido externo");
            try {

                pdialog.incrementProgressBy(10);

                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                if (pdialog!=null) {
                    pdialog.dismiss();
                }
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                if (pdialog!=null) {
                    pdialog.dismiss();
                }
                return getResources().getString(R.string.xml_error);

            }finally {
                if (receiver!=null) {
                    getActivity().unregisterReceiver(receiver);
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {

            txtExitoDescarga.setText(result);
            txtExitoDescarga.setVisibility(View.VISIBLE);

            if (!result.contains("Error")) {
                btnEscoger.setVisibility(View.VISIBLE);
            }


        }
    }




    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

        Log.i(MainActivity.TAG,"Cargando contenido y parseando " + urlString);

        String resultado=" gymkanas descargadas.";
        InputStream stream = null;
        GymkanaXmlParser gymkanaXmlParser = new GymkanaXmlParser();
        List<GymkanaXmlParser.Ruta> rutas = null;

        try {
            stream = downloadUrl(urlString);
            pdialog.incrementProgressBy(30);
            rutas=gymkanaXmlParser.parse(stream);
            pdialog.incrementProgressBy(30);
            Log.i(MainActivity.TAG,"Hemos parseado las rutas");

        }catch(Exception e){
            Log.i(MainActivity.TAG,"Error"+e);

            throw new IOException("ERROR: "+e);
        } finally {
            if (stream != null) {

                stream.close();
            }
        }

        Log.i(MainActivity.TAG, "voy a comenzar en background");
        datos.getDb().beginTransaction();

        OperacionesBD datos;
        datos= OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());

        Cursor c=datos.obtenerTiposRutas();
        if (c!= null && c.moveToFirst()) {
            Log.i(MainActivity.TAG, "Los Datos iniciales ya existían");

        }else{
            //Insertar datos iniciales
            DatosTipoRuta tipoRuta = new DatosTipoRuta(1, "En coche");
            datos.insertarTipoRuta(tipoRuta);
            tipoRuta = new DatosTipoRuta(2, "A pie");
            datos.insertarTipoRuta(tipoRuta);
            tipoRuta = new DatosTipoRuta(3, "En bici");
            datos.insertarTipoRuta(tipoRuta);
            Log.i(MainActivity.TAG, "Datos iniciales insertados");
        }
        pdialog.incrementProgressBy(5);

        Log.i(MainActivity.TAG,"Continuamos");
        Integer contador=0;
        for (GymkanaXmlParser.Ruta ruta: rutas) {


            Log.i(MainActivity.TAG, "Id de tipo ruta:"+ datos.obtenerTiposRutaporNombre(ruta.tipo).getTipoRuta_id());

            if (!datos.isRutaExistente(ruta.identificacion)){

                contador++;

                DatosRuta rut= new DatosRuta(ruta.nombre,true, false, new Integer(0) ,"Gymkana", datos.obtenerTiposRutaporNombre(ruta.tipo).getTipoRuta_id(),ruta.duracion, ruta.distancia, ruta.identificacion, new Integer("1"));
                rut.setOrden_sigiuente_poi(new Integer(1));
                Log.i(MainActivity.TAG, "orden siguiente poi="+rut.getOrden_sigiuente_poi());
                datos.insertarRuta(rut);

                DatabaseUtils.dumpCursor(datos.obtenerTiposRutas());
                DatabaseUtils.dumpCursor(datos.obtenerRutas());
                DatosRuta rutadeBd = datos.obtenerRutaPorNombre(ruta.nombre);

                for (GymkanaXmlParser.Poi poi:ruta.pois){


                    List<DatosRespuesta> datosRespuestas = new ArrayList();
                    for (GymkanaXmlParser.Respuesta respuesta:poi.respuestas){
                        DatosRespuesta datosRespuesta=new DatosRespuesta(respuesta.texto, respuesta.correcta);
                        datosRespuestas.add(datosRespuesta);
                    }


                    DatosPoi datosPoi=new DatosPoi(new Integer(poi.orden), poi.nombre, poi.descripcion, new Double(poi.longitud), new Double(poi.latitud), poi.img, poi.masinfo, poi.categoria, new Integer(poi.puntos), new Integer(poi.ordenSiguientePoi), rutadeBd.getIdRuta(), poi.pregunta, datosRespuestas);
                    datos.insertarPoi(datosPoi);

                    DatabaseUtils.dumpCursor(datos.obtenerPois());
                    DatabaseUtils.dumpCursor(datos.obtenerRespuestas());

                }
            }

        }
        pdialog.incrementProgressBy(20);
        datos.getDb().setTransactionSuccessful();
        datos.getDb().endTransaction();
        pdialog.dismiss();
        return contador + resultado;
    }





    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000 /* milisegundos */);
        conn.setConnectTimeout(18000 /* milisegundos */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }



    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


            if (WIFI.equals(sPref) && networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                refreshDisplay = true;
                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();


            } else if (ANY.equals(sPref) && networkInfo != null) {
                refreshDisplay = true;


            } else {
                refreshDisplay = false;
            }
        }
    }


}
