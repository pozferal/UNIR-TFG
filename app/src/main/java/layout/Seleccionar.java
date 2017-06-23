package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.alvaro.tfg.gymkanaturistica.MainActivity;
import com.alvaro.tfg.gymkanaturistica.R;
import com.alvaro.tfg.gymkanaturistica.db.DatosRuta;
import com.alvaro.tfg.gymkanaturistica.db.OperacionesBD;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Seleccionar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Seleccionar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Seleccionar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Seleccionar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Seleccionar.
     */
    public static Seleccionar newInstance(String param1, String param2) {
        Seleccionar fragment = new Seleccionar();
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
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_seleccionar,container,false);
        String resultado = "";
        Log.d(MainActivity.TAG, "Vamos a pintar el fragment de selección");


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        OperacionesBD datos;
        Log.d(MainActivity.TAG, "Vamos a pintar el fragment de selección2");
        datos = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
        if (datos!=null) {

            AdaptadorLista adaptador=null;
            List<DatosRuta> listadatos = new ArrayList();

            Log.d(MainActivity.TAG, "Vamos a pintar el fragment de selección3");

            listadatos=datos.obtenerListaRutas();

            adaptador = new AdaptadorLista(getActivity(),listadatos);

            ListView listaElementos = (ListView)rootView.findViewById(R.id.listaElementos);
            listaElementos.setAdapter(adaptador);

           listaElementos.setOnItemClickListener(new AdapterView.OnItemClickListener()
           {

               @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
               {
                   DatosRuta  elemento=(DatosRuta)arg0.getAdapter().getItem(position);
                   final String identificador=elemento.getIdentificacion();
                   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                   builder.setMessage("Quieres activar la Gymkana " + elemento.getDescripcion() )
                           .setTitle("¿Seguro?")
                           .setNegativeButton(android.R.string.cancel, null)
                           .setPositiveButton(android.R.string.ok, new
                                   DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           setGymkanaActiva(identificador);

                                           FragmentTransaction tr = getFragmentManager().beginTransaction();
                                           Rumbo fragmentoRumbo=new Rumbo();
                                           tr.replace(R.id.contenidoFragmentos,fragmentoRumbo);

                                            tr.commit();

                                       }
                                   }
                           );
                   builder.show();

               }
           });


        }




        return rootView;
    }
    public void setGymkanaActiva(String identificador){
        OperacionesBD datos = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
        datos.setGymkanaActiva(identificador);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
