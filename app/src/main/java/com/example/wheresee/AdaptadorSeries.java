package com.example.wheresee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

/**
 * AdaptadorSeries extiende de FirebaseRecyclerAdapter para mostrar objetos Series en un RecyclerView.
 * Utiliza el layout 'elemento_selector.xml' para representar cada elemento.
 */
public class AdaptadorSeries extends FirebaseRecyclerAdapter<Series, AdaptadorSeries.ViewHolder> {

    private final LayoutInflater inflador;
    private final Context contexto;
    private View.OnClickListener onClickListener;
    protected DatabaseReference seriesReference;

    /**
     * Constructor del AdaptadorSeries.
     *
     * @param contexto  Contexto de la aplicación.
     * @param reference Referencia a la base de datos donde se encuentran las series.
     */
    public AdaptadorSeries(Context contexto, DatabaseReference reference) {
        super(new FirebaseRecyclerOptions.Builder<Series>()
                .setQuery(reference, Series.class)
                .build());
        this.inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.seriesReference = reference;
        this.contexto = contexto;
    }

    /**
     * Establece el listener para los clics en los ítems del RecyclerView.
     *
     * @param onClickListener Listener que se ejecuta al pulsar un elemento.
     */
    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Clase ViewHolder que contiene las vistas de cada elemento.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView portada;
        public TextView titulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portada = itemView.findViewById(R.id.portada);
            titulo  = itemView.findViewById(R.id.titulo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.elemento_selector, parent, false);
        if (onClickListener != null) v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Sustituye populateViewHolder por onBindViewHolder (API nueva)
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Series serie) {
        // (Opcional) por si lo usas en otro sitio
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Glide.with(contexto)
                .load(serie.getRecursoImagen())
                .into(holder.portada);

        holder.titulo.setText(serie.getTitulo());
    }

    /**
     * Inserta una nueva serie en la base de datos.
     *
     * @param series Objeto Series a insertar.
     */
    public void insertarSerie(Series series) {
        seriesReference.push().setValue(series);
    }
    /**
     * Elimina una serie de la base de datos según la posición en el adaptador.
     *
     * @param posicion Posición del elemento a eliminar.
     */
    public void borrarSerie(int posicion) {
        DatabaseReference referencia = getRef(posicion);
        referencia.removeValue();
    }
}

