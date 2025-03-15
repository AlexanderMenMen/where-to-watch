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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

/**
 * AdaptadorSeries extiende de FirebaseRecyclerAdapter para mostrar objetos Series en un RecyclerView.
 * Utiliza el layout 'elemento_selector.xml' para representar cada elemento.
 */
public class AdaptadorSeries extends FirebaseRecyclerAdapter<Series, AdaptadorSeries.ViewHolder>
{
    private LayoutInflater inflador;
    private Context contexto;
    private View.OnClickListener onClickListener;
    protected DatabaseReference seriesReference;

    /**
     * Establece el listener para los clics en los ítems del RecyclerView.
     *
     * @param onClickListener Listener que se ejecuta al pulsar un elemento.
     */
    public void setOnItemClickListener(View.OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    /**
     * Constructor del AdaptadorSeries.
     *
     * @param contexto  Contexto de la aplicación.
     * @param reference Referencia a la base de datos donde se encuentran las series.
     */
    public AdaptadorSeries(Context contexto, DatabaseReference reference)
    {
        super(Series.class, R.layout.elemento_selector, AdaptadorSeries.ViewHolder.class, reference);
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.seriesReference = reference;
        this.contexto = contexto;
    }

    /**
     * Clase ViewHolder que contiene las vistas de cada elemento.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView portada;
        public TextView titulo;
        // Se puede agregar TextView para temporadas si se requiere

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            // temporadas = (TextView) itemView.findViewById(R.id.temporadas);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = inflador.inflate(R.layout.elemento_selector, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    protected void populateViewHolder(ViewHolder holder, Series serie, int posicion)
    {
        // Se instancia FirebaseStorage (opcional, si se requiere para otras operaciones)
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Se utiliza Glide para cargar la imagen desde la URL proporcionada
        Glide.with(contexto)
                .load(serie.getRecursoImagen())
                .into(holder.portada);

        // Se asigna el título de la serie al TextView
        holder.titulo.setText(serie.getTitulo());
    }

    /**
     * Inserta una nueva serie en la base de datos.
     *
     * @param series Objeto Series a insertar.
     */
    public void insertarLibro(Series series)
    {
        seriesReference.push().setValue(series);
    }

    /**
     * Elimina una serie de la base de datos según la posición en el adaptador.
     *
     * @param posicion Posición del elemento a eliminar.
     */
    public void borrarLibro(int posicion)
    {
        DatabaseReference referencia = getRef(posicion);
        referencia.removeValue();
    }
}

