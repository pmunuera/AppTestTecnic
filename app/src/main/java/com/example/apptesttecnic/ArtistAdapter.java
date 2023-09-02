package com.example.apptesttecnic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

//Esta clase sirve para pasar una lista con los fotógrafos y darle a cada un Listener
//para que cuando los pulsemos vayan a la pestaña de detalles de cada artista
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private final List<ArtistParcelable> artists;
    public ArtistAdapter(List<ArtistParcelable> artists) {
        this.artists = artists;
    }
    public interface OnArtistClickListener {
        void onArtistClick(ArtistParcelable artist);
    }

    private OnArtistClickListener listener;

    public void setOnArtistClickListener(OnArtistClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArtistParcelable artist = artists.get(position);
        holder.bind(artist);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onArtistClick(artist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
    //Esta clase viewHolder nos hacer cargar gráficamente los datos que hemos recibido
    //de cada elemento de la lista en "item_artist"
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        private final TextView mNombreTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mNombreTextView = itemView.findViewById(R.id.nombreTextView);
        }

        public void bind(ArtistParcelable artist) {
            String name = artist.getFirst_name()+" "+artist.getLast_name();
            mNombreTextView.setText(name);
            Picasso.get().load(artist.getImage()).into(mImageView);
        }
    }
}
