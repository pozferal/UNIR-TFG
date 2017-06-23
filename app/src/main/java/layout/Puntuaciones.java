package layout;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alvaro.tfg.gymkanaturistica.MainActivity;
import com.alvaro.tfg.gymkanaturistica.R;
import com.alvaro.tfg.gymkanaturistica.db.DatosRuta;
import com.alvaro.tfg.gymkanaturistica.db.OperacionesBD;

import java.util.ArrayList;
import java.util.List;


public class Puntuaciones extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Puntuaciones() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Puntuaciones newInstance(String param1, String param2) {
        Puntuaciones fragment = new Puntuaciones();
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

        View rootView=inflater.inflate(R.layout.fragment_puntuaciones,container,false);
        String resultado = "";
        Log.d(MainActivity.TAG, "Vamos a pintar el fragment de selección");


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        OperacionesBD datos;
        datos = OperacionesBD.obtenerInstancia(getActivity().getApplicationContext());
        if (datos!=null) {

            AdaptadorListaPuntuaciones adaptador=null;
            List<DatosRuta> listadatos = new ArrayList();

            Log.d(MainActivity.TAG, "Vamos a pintar el fragment de selección3");

            listadatos=datos.obtenerListaRutas();

            adaptador = new AdaptadorListaPuntuaciones(getActivity(),listadatos);

            ListView listaElementos = (ListView)rootView.findViewById(R.id.listaElementos);
            listaElementos.setAdapter(adaptador);




        }



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
