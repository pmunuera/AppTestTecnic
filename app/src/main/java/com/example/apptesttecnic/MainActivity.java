package com.example.apptesttecnic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
//La actividad principal
public class MainActivity extends AppCompatActivity implements ArtistAdapter.OnArtistClickListener{
    private RecyclerView recyclerView;
    private final ArrayList<ArtistParcelable> artists = new ArrayList<>();

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_festivals);
        recyclerView = findViewById(R.id.recycler_view_artists);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<ArtistParcelable> b=new ArrayList<>();
        //Instanciamos una clase de artist adapter para que este sea el adapter
        // por el que se rigen los objetos del recyclerView
        ArtistAdapter adapter=new ArtistAdapter(b);
        adapter.setOnArtistClickListener(MainActivity.this);
        recyclerView.setAdapter(adapter);

        //Obtenemos la instancia de Realm para saber si hay datos y si debemos
        //contactar con la web, si no es asi, simplemente mostraremos los datos
        realm = Realm.getDefaultInstance();
        if(realm.where(Artist.class).count()==0){
            contactHTTPS();
        }
        else {
            showData();
        }

    }
    //Función que se usa para conectar-se con https a la web y extraer los datos,
    //lanzaremos una request tipo get al URL proporcionado y nos prepararemos para recibir la
    //respuesta en forma de JSON, después exploraremos la lista elemento a elemento para
    //añadir cada uno de los artistas a Realm
    public void contactHTTPS(){
        String url = "https://inphototest.app2u.es/api/photographer/";
        String username = "test@gmail.com";
        String password = "1234";
        //Antes de contactar comprobamos que realm se ha inicializado correctamente
        if (Realm.getDefaultInstance() != null) {
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject artista = jsonArray.getJSONObject(i);

                                    // Agregar una tarea
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            try {
                                                Artist artist = realm.createObject(Artist.class, artista.getInt("id"));
                                                artist.setEmail(artista.getString("email"));
                                                artist.setFirst_name(artista.getString("first_name"));
                                                artist.setLast_name(artista.getString("last_name"));
                                                artist.setDescription(artista.getString("description"));
                                                artist.setImage(artista.getString("image"));
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            // Handle the response
                            showData();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", error.getMessage());
                            // Handle the error
                        }
                    }) {
                //Pasamos el username y la password para que se nos permita autenticar
                //en la página web, daremos como header esta autorización
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = username + ":" + password;
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            //Usaremos la librería externa Volley para hacer la request habiendo añadido
            //el header necesario
            Volley.newRequestQueue(getApplicationContext()).add(getRequest);
        }
    }

    //Función que recupera todos los Artist de Realm para añadirlos a una lista de
    // Artist Parcelable, que así se vean los elementos de la lista y puedan pasar
    // datos a otra Activity cuando se pulsan.
    public void showData(){
        // Recuperar tareas
        RealmResults<Artist> artistRealmResults = realm.where(Artist.class).findAll();
        for (Artist a : artistRealmResults) {
            // Hacer algo con las tareas recuperadas
            ArtistParcelable ar = new ArtistParcelable(a.getId(),a.getEmail(),a.getFirst_name(),a.getLast_name(),a.getDescription(),a.getImage());
            artists.add(ar);
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                ArtistAdapter adapter = new ArtistAdapter(artists);
                adapter.setOnArtistClickListener(MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    //Definimos la función que se eecutará cuando pulsemos algún elemento, lanzaremos
    //otra Activity donde pasaremos los datos de la lista de esta clase
    @Override
    public void onArtistClick(ArtistParcelable artist) {
        Intent intent = new Intent(this, DetailArtist.class);
        intent.putParcelableArrayListExtra("ARTIST", artist);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}