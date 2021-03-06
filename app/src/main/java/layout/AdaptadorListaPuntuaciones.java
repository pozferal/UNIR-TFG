package layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alvaro.tfg.gymkanaturistica.R;
import com.alvaro.tfg.gymkanaturistica.db.DatosRuta;

import java.util.List;



public class AdaptadorListaPuntuaciones extends ArrayAdapter {

    Activity context;
    List datos;
    DatosRuta objRuta;

    public AdaptadorListaPuntuaciones(Activity context, List datos) {
        super(context, R.layout.list_item_rutas, datos);
        this.context=context;
        this.datos=datos;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View row= inflater.inflate(R.layout.list_item_rutas_punt, parent, false);
        this.setNotifyOnChange(true);

        objRuta=(DatosRuta)datos.get(position);
        TextView lblTitulo=(TextView)row.findViewById(R.id.titulo);
        lblTitulo.setText(objRuta.getDescripcion());

        ImageView imgTipoRuta=(ImageView) row.findViewById(R.id.imgTipoRuta);
        Integer tipoRuta=objRuta.getTipo_Ruta_id();
        if (tipoRuta !=null) {
            if (tipoRuta == 1) {
                imgTipoRuta.setImageResource(R.drawable.ic_coche);
            } else if (tipoRuta == 2) {
                imgTipoRuta.setImageResource(R.drawable.ic_andando);
            } else if (tipoRuta == 3) {
                imgTipoRuta.setImageResource(R.drawable.ic_bici);
            }
        }

        ImageView imgFin = (ImageView)row.findViewById(R.id.imgFin);

        if (objRuta.isCompletada()) {
            imgFin.setVisibility(View.VISIBLE);
        }else{
            imgFin.setVisibility(View.INVISIBLE);
        }

        TextView txtDuracion=(TextView)row.findViewById(R.id.txtDuracion);
        txtDuracion.setText(objRuta.getDuracion());

        TextView txtDistancia=(TextView)row.findViewById(R.id.txtDistancia);
        txtDistancia.setText(" - " + objRuta.getDistancia());



        TextView txtPuntos=(TextView)row.findViewById(R.id.textPuntos);
        if (txtPuntos!=null) txtPuntos.setText(objRuta.getPuntuacion()+" Ptos.");

        return(row);
    }
}
