package layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alvaro.tfg.gymkanaturistica.R;
import com.alvaro.tfg.gymkanaturistica.db.DatosPoi;

import java.util.List;

public class AdaptadorListaConseguidos extends ArrayAdapter {

    Context context;
    List datos;
    DatosPoi objDatosPoi;


    public AdaptadorListaConseguidos(Activity context, List datos) {
        super(context, R.layout.list_item_rutas, datos);
        this.context=context;
        this.datos=datos;
    }


    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      //  LayoutInflater inflater=context.getLayoutInflater();
        View row= inflater.inflate(R.layout.list_item_conseguidos, parent, false);
        this.setNotifyOnChange(true);

        objDatosPoi=(DatosPoi)datos.get(position);
        TextView lblTitulo=(TextView)row.findViewById(R.id.tituloConseguidos);
        lblTitulo.setText(objDatosPoi.getDescripcion());


        TextView txtPuntos=(TextView)row.findViewById(R.id.textPuntosConseguidos);
        txtPuntos.setText(objDatosPoi.getPuntos());



        return(row);
    }
}
