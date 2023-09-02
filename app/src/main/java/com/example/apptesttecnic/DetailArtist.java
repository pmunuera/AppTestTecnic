package com.example.apptesttecnic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
//Esta clase carga gráficamente los datos del artista que recibe por el Intent desde
// la MainActivity, mostrando la foto, el nombre y la descripción de cada fotógrafo
public class DetailArtist extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_artist);

        ImageView imageView;
        TextView nameView;
        TextView detailView;
        DetailArtistViewModel viewModel;

        viewModel = new ViewModelProvider(this).get(DetailArtistViewModel.class);

        imageView=findViewById(R.id.imageView6);
        nameView=findViewById(R.id.textView5);
        detailView=findViewById(R.id.textView2);

        //Recibimos el intent con el fotógrafo necesario y pasamos sus datos a los elementos gráficos
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ARTIST")) {
            ArtistParcelable artist = (ArtistParcelable) intent.getSerializableExtra("ARTIST");
            System.out.println(artist.toString());
            viewModel.setImage(artist.getImage());
            viewModel.setFirst_name(artist.getFirst_name());
            viewModel.setLast_name(artist.getLast_name());
            viewModel.setDescription(artist.getDescription());

            String name = viewModel.getFirst_name()+" "+viewModel.getLast_name();
            nameView.setText(name);
            detailView.setText(viewModel.getDescription());
            Picasso.get().load(viewModel.getImage()).into(imageView);

            //Funcionalidad para que el botón nos devuelva a la Activity anterior
            Button btnBack = findViewById(R.id.button4);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailArtist.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
