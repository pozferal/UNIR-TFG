package layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.alvaro.tfg.gymkanaturistica.R;

import java.util.ArrayList;


public class AdaptadorReciclerView extends
        RecyclerView.Adapter<AdaptadorReciclerView.ReciclerViewHolder>
        implements View.OnClickListener{
    private ArrayList<ObjetoMenu> datos;
    private View.OnClickListener listener;
    public AdaptadorReciclerView(ArrayList<ObjetoMenu> datos) {
        this.datos = datos;
    }
    public static class ReciclerViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtPuntos;
        private TextView txtTitulo;
        private TextView txtLabel;
        public ReciclerViewHolder(View itemView) {
            super(itemView);
            txtPuntos = (TextView) itemView.findViewById(R.id.txtPuntos);
            txtTitulo = (TextView)itemView.findViewById(R.id.TituloMenu);
            txtLabel=(TextView)itemView.findViewById(R.id.textLabel);

        }
        public void bindTitular(ObjetoMenu t) {

            txtTitulo.setText(t.getTitulo());
            txtPuntos.setText(t.getPuntos()+"");
            if (t.getPuntos()>1){
                txtLabel.setText("Puntos");
            }else{
                txtLabel.setText("Punto");
            }

        }


    }

    @Override
    public ReciclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_cardview, null, false);
        itemView.setOnClickListener(this);
        ReciclerViewHolder tvh = new ReciclerViewHolder(itemView);
        return tvh;
    }
    @Override
    public void onBindViewHolder(ReciclerViewHolder viewHolder, int pos) {
        ObjetoMenu item = datos.get(pos);
        viewHolder.bindTitular(item);
    }
    @Override
    public int getItemCount() {
        return datos.size();
    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

}