package layout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.alvaro.tfg.gymkanaturistica.MainActivity;
import com.alvaro.tfg.gymkanaturistica.R;
import com.alvaro.tfg.gymkanaturistica.db.DatosPoi;
import com.alvaro.tfg.gymkanaturistica.db.DatosRuta;
import com.alvaro.tfg.gymkanaturistica.db.OperacionesBD;
import com.alvaro.tfg.gymkanaturistica.util.Compass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Rumbo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Rumbo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rumbo extends Fragment implements TabHost.OnTabChangeListener,OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    String distancia = null;
    TextView txtDistancia = null;
    TextView txtPoi = null;

    private LocationManager locManager;
    private LocationListener locListener;
    private String provider;
    private Compass compass;
    Button btnComenzar;
    Button btnSiguiente;
    ImageView imgMeta;
    static View rootView;
    static DatosRuta datosRuta;
    boolean brujulaDisponible =false;

    public static DatosRuta getDatosRuta() {
        return datosRuta;
    }

    public Rumbo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rumbo.
     */
    // TODO: Rename and change types and number of parameters
    public static Rumbo newInstance(String param1, String param2) {
        Rumbo fragment = new Rumbo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, "On create....");


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(MainActivity.TAG, "On createview....");
        super.onDestroyView();



        SensorManager mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            brujulaDisponible=true;
        }
        else {
            brujulaDisponible=false;
        }




        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_rumbo, container, false);
            TabHost pestanas = (TabHost) rootView.findViewById(R.id.tabhost);

            pestanas.setup();
            TabHost.TabSpec spec = pestanas.newTabSpec("Rumbo"); //TODO: sacar esto a parametros

            spec.setContent(R.id.rumbo);
            spec.setIndicator("Rumbo", ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_menu_camera));

            pestanas.addTab(spec);

            spec = pestanas.newTabSpec("Mapa"); //TODO: sacar esto a parametros
            spec.setContent(R.id.mapa);
            spec.setIndicator("Mapa", ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_menu_camera));
            pestanas.addTab(spec);

            spec = pestanas.newTabSpec("Conseguidos"); //TODO: sacar esto a parametros
            spec.setContent(R.id.conseguido);
            spec.setIndicator("Conseguidos", ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_menu_camera));
            pestanas.addTab(spec);


        }

        TabWidget tw = (TabWidget)rootView.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i) {
            View tabView = tw.getChildTabViewAt(i);
            TextView tv = (TextView) tabView.findViewById(android.R.id.title);
            tv.setTextSize(10);
        }
        OperacionesBD datos = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());

         datosRuta = datos.getRutaActiva();

        TextView txtGymActiva = (TextView) rootView.findViewById(R.id.txtGymActiva);
        txtGymActiva.setText(datosRuta.getDescripcion_ruta());
        txtPoi = (TextView) rootView.findViewById(R.id.txtPoi);

        compass = new Compass(getActivity());
        compass.arrowView = (ImageView) rootView.findViewById(R.id.flecha);
        compass.arrowViewNorte = (ImageView) rootView.findViewById(R.id.imageNorte);
        compass.arrowViewNorte.setVisibility(View.INVISIBLE);
        compass.arrowView.setVisibility(View.INVISIBLE);
        obtenerLocalizacionActual();
        obtenerLocalizacionObjetivo(datosRuta.getIdRuta(), datosRuta.getOrden_sigiuente_poi());
        distancia = compass.obtenerDistanciaAObjetivo();
        txtDistancia = (TextView) rootView.findViewById(R.id.txtDistancia);
        txtDistancia.setText("Distancia: "+distancia);

        imgMeta = (ImageView) rootView.findViewById(R.id.imgMeta);


        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSiguiente = (Button) rootView.findViewById(R.id.btnContinuar);
        if (btnSiguiente != null) btnSiguiente.setVisibility(View.INVISIBLE);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mostrarDialogoPreguntas();
            }
        });

        Button btnDetalles = (Button) rootView.findViewById(R.id.btnVerDesc);
        btnDetalles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dialogo = createDialogoPoi(datosSiguientePoi.getIdPoi());
                dialogo.show();
            }
        });


        btnComenzar = (Button) rootView.findViewById(R.id.btnOtraVez);
        btnComenzar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setMessage("Quieres volver a comenzar esta Gymkana "  )
                        .setTitle("¿Seguro?")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        resetGymkana(datosRuta.getIdRuta());

                                    }
                                }
                        );
                builder.show();


            }
        });



        Integer completada=null;
        if (datosRuta.isCompletada()) {
            completada=1;
            mostrarUIRutaCompletada();
        }else{
            completada=0;
            mostrarUIRutaNoCompletada();
        }
        obtenerPoisYaAlcanzados(datosRuta.getIdRuta(),completada);

        return rootView;
    }


    public void mostrarDialogoPreguntas() {

        String[] respuestas = new String[datosSiguientePoi.getRespuestas().size()];
Log.i(MainActivity.TAG, "respuestas.size ......"+datosSiguientePoi.getRespuestas().size());
        for (int i=0; i<datosSiguientePoi.getRespuestas().size(); i++){
            respuestas[i]= datosSiguientePoi.getRespuestas().get(i).getTexto();
        }
        Log.i(MainActivity.TAG, "respuestas......"+respuestas);
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle(datosSiguientePoi.getPregunta())
                .setItems(respuestas, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {

                                if (datosSiguientePoi.getRespuestas().get(item).getCorrecta()==1){
                                    //Respuesta correcta sumar 5 puntos!!
                                    datosSiguientePoi.setPuntos(5);
                                    OperacionesBD datos = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
                                    datos.setPuntosPoi(datosSiguientePoi.getIdPoi(), 5);
                                    Toast.makeText(getActivity(), "Respuesta Correcta!!! \nSumas 5 puntos", Toast.LENGTH_LONG).show();

                                }else{
                                    Toast.makeText(getActivity(), "Vaya, no has acertado, \nmás suerte la próxima vez", Toast.LENGTH_LONG).show();
                                }
                                establecerSiguiente();
                            }
                });

            builder.show();

}


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

    @Override
    public void onTabChanged(String tabId) {

    }

    private GoogleMap mMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        dibujarMapa();
    }

    public void dibujarMapa(){
        LatLng siguientePoi=new LatLng(datosSiguientePoi.getLatitud(),datosSiguientePoi.getLongitud());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(siguientePoi,14));
        mMap.clear();
        mMap.addMarker(new MarkerOptions().title(datosSiguientePoi.getNombre()).snippet(" ").position(siguientePoi));
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),LOCATION_PERMISSIONS,LOCATION_PERMISSIONS_REQUEST);
            mMap.setMyLocationEnabled(true);
            // return;
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                        Manifest.permission.ACCESS_COARSE_LOCATION};
    static final int LOCATION_PERMISSIONS_REQUEST=0;

    private void obtenerLocalizacionActual(){

        if(!datosRuta.isCompletada()) {
            locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            provider = devolverProvider();
            if (provider != null) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(getActivity(), LOCATION_PERMISSIONS, LOCATION_PERMISSIONS_REQUEST);
                }

                //Obtenemos la última posición conocida
                Location loc = locManager.getLastKnownLocation(provider);
                if (loc != null) {
                    compass.setMiLatitudActual(loc.getLatitude());
                    compass.setMiLongitudActual(loc.getLongitude());

                }
                locListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        if (!datosRuta.isCompletada()) {
                            distancia = compass.obtenerDistanciaAObjetivo();
                            txtDistancia.setText(distancia);


                            if (!distancia.contains("k")) {
                                int metros = new Integer(distancia.substring(0, distancia.indexOf(" ")));
                                if (metros <= 50) {
                                    Toast.makeText(getActivity(), " Has llegado al punto! ", Toast.LENGTH_LONG).show();

                                    imgMeta.setVisibility(View.VISIBLE);
                                    btnSiguiente.setText(datosSiguientePoi.getPregunta());
                                    btnSiguiente.setVisibility(View.VISIBLE);


                                } else {
                                    if (btnSiguiente != null)
                                        btnSiguiente.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                btnSiguiente.setVisibility(View.INVISIBLE);
                            }

                            compass.setMiLatitudActual(location.getLatitude());
                            compass.setMiLongitudActual(location.getLongitude());
                        }
                    }

                    public void onProviderDisabled(String provider) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d("EjemploLocalizacion", "Provider Status: " + status);
                    }
                };
                locManager.requestLocationUpdates(provider, 10000, 0, locListener);

            } else {
                Toast.makeText(getActivity(), "No encontrado provider :\n", Toast.LENGTH_LONG).show();
            }
        }
    }


    DatosPoi datosSiguientePoi=null;
    public boolean notificado=false;
   

    private void obtenerLocalizacionObjetivo(Integer idRuta, Integer ordenSiguientePoi) {
        Log.i(MainActivity.TAG, "obtener localizacion objetivo: idRuta=" + idRuta + " ordenSgtePoi="+ordenSiguientePoi);
        if (ordenSiguientePoi!=0) {

            GetImageTask task = new GetImageTask();

            OperacionesBD datos = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
             datosSiguientePoi = datos.siguientePoi(idRuta, ordenSiguientePoi);
            txtPoi.setText("Objetivo: "+datosSiguientePoi.getNombre());
            task.execute(new String[] { datosSiguientePoi.getImg() });
            compass.setLatitudDondeIr(datosSiguientePoi.getLatitud());
            compass.setLoingitudDondeIr(datosSiguientePoi.getLongitud());
        }
    }

    protected String devolverProvider() {
        String resultado=null;
        List<String> listaProviders = locManager.getAllProviders();
        if (listaProviders.contains(LocationManager.GPS_PROVIDER))     {
            Log.d("EjemploLocalizacion", "Lista providers contiene GPS.");
            if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("EjemploLocalizacion", "GPS activado.");
                resultado=LocationManager.GPS_PROVIDER;
            }
        }     // Si ha fallado el primero pruebo con la red

        if (resultado==null &&   listaProviders.contains(LocationManager.NETWORK_PROVIDER))
        {
            if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d("EjemploLocalizacion", "Red Activada.");
                resultado=LocationManager.NETWORK_PROVIDER;
            }
        }
        return resultado;
    }



    public void establecerSiguiente(){


        notificado=false;
        Integer ordenSiguiente=datosSiguientePoi.getOrden();
        if (datosSiguientePoi.getOrdenSiguientePoi()!=0){
            ordenSiguiente++;
            obtenerLocalizacionObjetivo(datosSiguientePoi.getId_ruta(),ordenSiguiente );
            obtenerPoisYaAlcanzados(datosSiguientePoi.getId_ruta(),0);
            btnSiguiente.setVisibility(View.INVISIBLE);
            dibujarMapa();

        }else{
            //Enhorabuena!
            OperacionesBD db = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
            Toast.makeText(getActivity(),"¡ENHORABUENA!\n Ha terminado la Gymkana",  Toast.LENGTH_LONG).show();
            datosRuta.setCompletada(true);
            db.setRutaCompletada(datosSiguientePoi.getId_ruta());
            compass.arrowView.setVisibility(View.INVISIBLE);
            compass.arrowViewNorte.setVisibility(View.INVISIBLE);
            obtenerPoisYaAlcanzados(datosSiguientePoi.getId_ruta(),1);
            mostrarUIRutaCompletada();
        }
    }


    public void mostrarUIRutaNoCompletada(){
        ImageView imgCorona=(ImageView)rootView.findViewById(R.id.imgCorona);
        imgCorona.setVisibility(View.INVISIBLE);
        btnComenzar.setVisibility(View.INVISIBLE);
        Button btnVerDesc=(Button)rootView.findViewById(R.id.btnVerDesc);
        btnVerDesc.setVisibility(View.VISIBLE);
        imgMeta.setVisibility(View.VISIBLE);

        if (brujulaDisponible) {
            compass.arrowViewNorte.setVisibility(View.VISIBLE);
            compass.arrowView.setVisibility(View.VISIBLE);
            TextView btnNoBrujula=(TextView)rootView.findViewById(R.id.textNoBrujula);
            btnNoBrujula.setVisibility(View.INVISIBLE);
        }else{
            TextView btnNoBrujula=(TextView)rootView.findViewById(R.id.textNoBrujula);
            btnNoBrujula.setVisibility(View.VISIBLE);
        }


    }

    public void mostrarUIRutaCompletada(){

        ImageView imgCorona=(ImageView)rootView.findViewById(R.id.imgCorona);
        imgCorona.setVisibility(View.VISIBLE);
        btnComenzar.setVisibility(View.VISIBLE);
        txtPoi.setText("¡Gymkana completada!");
        txtDistancia.setText("");


        Button btnVerDesc=(Button)rootView.findViewById(R.id.btnVerDesc);
        btnVerDesc.setVisibility(View.INVISIBLE);
        btnSiguiente.setVisibility(View.INVISIBLE);
        imgMeta.setVisibility(View.INVISIBLE);

        ImageView imgNorte=(ImageView)rootView.findViewById(R.id.imageNorte);
        imgNorte.setVisibility(View.INVISIBLE);
        compass.arrowViewNorte.setVisibility(View.INVISIBLE);
        ImageView imgFlecha=(ImageView)rootView.findViewById(R.id.flecha);
        imgFlecha.setVisibility(View.INVISIBLE);
        compass.arrowView.setVisibility(View.INVISIBLE);
        TextView btnNoBrujula=(TextView)rootView.findViewById(R.id.textNoBrujula);
        btnNoBrujula.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onStart() {
        super.onStart();
        compass.start();
    }

    @Override
    public  void onPause() {
        Log.d(MainActivity.TAG, "On pause....");
        super.onPause();
        if (locListener!=null)     {
            if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                    getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }         else {
                locManager.removeUpdates(locListener);
            }
        }

        compass.stop();
    }

    @Override
    public void onResume() {
        Log.d(MainActivity.TAG, "On resume....");
        super.onResume();
        if (provider!=null) {
            if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                    getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return  ;
            }         else {
                locManager.requestLocationUpdates(provider, 10000, 0, locListener);
            }
        }

        compass.start();
    }

    @Override
    public void onStop() {
        Log.d(MainActivity.TAG, "On stop....");
        super.onStop();
        compass.stop();
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d(MainActivity.TAG,"on destroy view");

        Fragment f=getActivity().getSupportFragmentManager().findFragmentById(R.id.rumbo);
        if (f != null) {
            Log.d(MainActivity.TAG, "on destroy view 1");
            getFragmentManager().beginTransaction().remove(f).commit();
        }
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null) {
            Log.d(MainActivity.TAG, "on destroy view 2");
            getFragmentManager().beginTransaction().remove(mapFrag).commit();
        }

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapa));
        if (fragment!=null) Log.d(MainActivity.TAG,"destroy fragment!=Null");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (fragment!=null){
            ft.remove(fragment);
        }
        if (ft!=null){
            ft.commit();
        }




        Fragment fTab = (Fragment) getFragmentManager().findFragmentById(R.id.tabhost);
        if (fTab != null) {
            Log.d(MainActivity.TAG, "on destroy view 3");
            getFragmentManager().beginTransaction().remove(fTab).commit();
        }



    }




    public class GetImageTask extends AsyncTask<String, Void, Bitmap> {


             @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (imgMeta!=null && result!=null) {
                imgMeta.setImageBitmap(result);
            }
        }

        private Bitmap downloadImage(String url) {
            Log.i(MainActivity.TAG, "url="+url);
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                if (stream!=null) {
                    stream.close();
                }
            } catch (IOException e1) {
                bitmap = BitmapFactory.decodeResource(rootView.getResources(),R.drawable.logo);
                e1.printStackTrace();
            } catch (Exception e1) {
                bitmap = BitmapFactory.decodeResource(rootView.getResources(),R.drawable.logo);
                e1.printStackTrace();
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString) throws IOException, Exception {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                throw new Exception("error al abrir la conexión");

            }
            return stream;
        }
    }

    public void obtenerPoisYaAlcanzados(Integer idRuta, Integer rutaTerminada){
        final  RecyclerView recView;

     OperacionesBD db = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
        final List<DatosPoi> datosPois= db.obtenerPoisYaAlcanzados(idRuta, rutaTerminada);
        ArrayList<ObjetoMenu> datos=new ArrayList<ObjetoMenu>();
        ObjetoMenu objMenu=null;
        int puntosAcumulados=0;
        for (int i=0; i<datosPois.size();i++) {

            puntosAcumulados=puntosAcumulados+datosPois.get(i).getPuntos().intValue();
            objMenu=new ObjetoMenu(datosPois.get(i).getNombre(),datosPois.get(i).getPuntos());
            datos.add(objMenu);
        }
        db.setPuntosRuta(datosRuta.getIdRuta(),puntosAcumulados);

        TextView txtPuntosAcumulados=(TextView)rootView.findViewById(R.id.txtPuntosAcumulados);
        if (datosRuta.isCompletada()){
            txtPuntosAcumulados.setText("Puntos totales: " + puntosAcumulados);

        }else {
            txtPuntosAcumulados.setText("Puntos acumulados: " + puntosAcumulados);
        }
        recView = (RecyclerView)rootView.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);
        final AdaptadorReciclerView adaptador = new AdaptadorReciclerView(datos);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PruebaCardView", "Pulsado el elemento " +
                        recView.getChildAdapterPosition(v));

                dialogo= createDialogoPoi(datosPois.get(recView.getChildAdapterPosition(v)).getIdPoi());
                dialogo.show();

            }
        });
        recView.setAdapter(adaptador);
        recView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));

    }


    public void  resetGymkana(Integer idRuta){
        OperacionesBD db = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
        db.resetGymkana(idRuta);

        datosRuta.setOrden_sigiuente_poi(1);
        datosRuta.setCompletada(false);
        obtenerLocalizacionActual();
        obtenerLocalizacionObjetivo(datosRuta.getIdRuta(),1);
        obtenerPoisYaAlcanzados(datosRuta.getIdRuta(),0);
        txtPoi.setText("Objetivo: "+datosSiguientePoi.getNombre());
        distancia = compass.obtenerDistanciaAObjetivo();
        txtDistancia.setText(distancia);
        mostrarUIRutaNoCompletada();
    }

android.app.AlertDialog dialogo;

    public AlertDialog createDialogoPoi(Integer idPoi) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View v = inflater.inflate(R.layout.dialogo_descripcion, null);

            builder.setView(v);

            OperacionesBD db = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
            DatosPoi datosPoi = db.obtenerPoi(idPoi);

            WebView webView = (WebView) v.findViewById(R.id.webViewDialogo);
            StringBuilder htmlString = new StringBuilder();
            htmlString.append("<h3>" + datosPoi.getNombre() + "</h3>");


            if (conectadoAInternet()) {
                htmlString.append("<p> <img src='" + datosPoi.getImg() + "' width='100%' /></p>");
            }
            htmlString.append("<p>" + datosPoi.getDescripcion() + "</p>");
            htmlString.append("<p><a href='" + datosPoi.getMasinfo() + "'>Más información</a></p>");

            webView.loadDataWithBaseURL(null, htmlString.toString(), "text/html", "UTF-8", null);


            Button btnCerrar = (Button) v.findViewById(R.id.btnCerrar);

            ((Button) v.findViewById(R.id.btnCerrar)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (dialogo != null) {
                        dialogo.dismiss();
                    }

                }
            });

            return builder.create();
        }catch (Exception e){
            return null;
        }
    }


    public boolean conectadoAInternet() throws InterruptedException, IOException
    {
        boolean resultado=false;
        String comando = "ping -c 1 google.com";

        resultado=(Runtime.getRuntime().exec(comando).waitFor() == 0);
        if (!resultado){
            comando = "ping -c 1 amazon.com";
            resultado=(Runtime.getRuntime().exec(comando).waitFor() == 0);
        }
        Log.i(MainActivity.TAG, "Hay internet?"+resultado);
        return resultado;
    }


public Bitmap downloadImage(String url) {
    URL imageUrl = null;
    HttpURLConnection conn = null;
    Bitmap imagen = null;

    try {

        imageUrl = new URL(url);
        Log.i(MainActivity.TAG, "url a descargar"+url);
        conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        imagen = BitmapFactory.decodeStream(conn.getInputStream());

    } catch (Exception e) {

        e.printStackTrace();

    }
    return imagen;
}


}
