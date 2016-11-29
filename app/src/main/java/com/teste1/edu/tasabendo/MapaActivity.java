package com.teste1.edu.tasabendo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teste1.edu.tasabendo.model.Evento;
import com.teste1.edu.tasabendo.util.HttpHelper;
import com.teste1.edu.tasabendo.util.PermissionUtils;
import org.json.JSONException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMyLocationButtonClickListener, OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    String[] categorias = new String[]{"Incendio", "Roubo", "Acidente", "Enchente"};
    MarkerOptions markerOptions;
    Marker marker;
    Marker markerDelete;
    List<Evento> listaEvento;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;


    private GoogleMap map;// obj que controla o Google Maps
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        map = null;
        listaEvento = new ArrayList<>();

        //recupera o fragment que esta no layout
        //utiliza o getChildFramgentManager() pois e um fragment dentro do outro
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //Inicia o google maps dentro do fragment
        mapFragment.getMapAsync(this);

        Spinner comboEventos = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_item);
        comboEventos.setAdapter(adaptador);

        Button btGET = (Button) findViewById(R.id.btGET);
        btGET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HttpRequestElastic http = new HttpRequestElastic();
                map.clear();
                marker = map.addMarker(markerOptions.position(map.getCameraPosition().target));
                HttpRequestElastic http = new HttpRequestElastic(map.getCameraPosition().target.latitude,map.getCameraPosition().target.longitude);
                http.execute();
            }
        });

        Button btPOST = (Button) findViewById(R.id.btPOST);
        btPOST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner sp = (Spinner) findViewById(R.id.spinner);
                String categoria = (String) sp.getSelectedItem();
                Evento evento = new Evento();
                evento.setCatecategory(categoria);
                evento.setLatitude(String.valueOf(map.getCameraPosition().target.latitude));
                evento.setLongitude(String.valueOf(map.getCameraPosition().target.longitude));
                evento.setCreate_at(Calendar.getInstance());

                HttpPostElastic httpPost = new HttpPostElastic(evento);
                httpPost.execute();
            }
        });

        Button btDELETE = (Button) findViewById(R.id.btDELETE);
        btDELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpDeleteElastic delete = new HttpDeleteElastic(markerDelete.getSnippet());
                delete.execute();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // o metodo onMapReady(map)
        this.map = googleMap;
        //cria o objeto LatLng com a coordenada do exemplo
        LatLng location = new LatLng(-23.478951, -46.746379); //coordenada de exemplo
        //LatLng location = new LatLng(this.map.getCameraPosition().target.latitude, this.map.getCameraPosition().target.longitude); //coordenada de exemplo
        //Posiciona o mapa na coordenada indicada (zoom = 13)
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
        this.map.moveCamera(update);
        //Marcador no local
        markerOptions = new MarkerOptions().title("Lugar Exemplo").snippet("Lugar Familiar").position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        this.marker = this.map.addMarker(markerOptions);
        //tipo do mapa (normal, satelite, terreno ou hibrido)
        this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // a propria classe implenta o Listener
        this.map.setOnCameraMoveListener(this);
        this.map.setOnCameraIdleListener(this);

        Toast toast = Toast.makeText(getContexto(), "Mapa mudou " + this.map.getCameraPosition().target.latitude + " " + this.map.getCameraPosition().target.longitude, Toast.LENGTH_LONG);
        toast.show();
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (map != null) {
            // Access to the location has been granted to the app.
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    public Context getContexto() {
        return this;
    }

    @Override
    public void onCameraMove() {
        this.marker.setPosition(new LatLng(this.map.getCameraPosition().target.latitude, this.map.getCameraPosition().target.longitude));
    }

    @Override
    public void onCameraIdle() {
        //Log.d("CameraIdle","CameraIdle "+ this.map.getCameraPosition().target.latitude+ " "+this.map.getCameraPosition().target.longitude );
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", LENGTH_SHORT).show();
        return false;
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Mapa Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.markerDelete = marker;
        return true;
    }

    private class HttpRequestElastic extends AsyncTask<ArrayList, Void, ArrayList> {
        public Double lat, lon;
        HttpRequestElastic(Double lat,Double lon){
            this.lat = lat;
            this.lon = lon;
        }
        HttpRequestElastic(){

        }
        @Override
        protected void onPreExecute() {

            Toast toast = Toast.makeText(getContexto(), "Execultando GET.....", LENGTH_SHORT);
            toast.show();

        }

        @Override
        protected ArrayList doInBackground(ArrayList... params) {

            try {
                //ArrayList <Evento> eventos = HttpHelper.doGet(this.lat,this.lon);
                //ArrayList <Evento> eventos = HttpHelper.doGet( -23.475401,-46.746479);
                ArrayList <Evento> eventos = HttpHelper.getEventos();

                return eventos;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.w("erro-actv", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.w("erro-actv", e.getMessage());
            } catch (ParseException e) {
                Log.w("erro-actv", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(final ArrayList s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listaEvento = s;
                    int tamanho = s.size();
                    for (int i = 0; i < tamanho ; i++) {

                        switch (((Evento) s.get(i)).getCatecategory() ){
                            case "incendio":
                        }
                        LatLng location = new LatLng(Double.parseDouble(((Evento) s.get(i)).getLatitude()),
                                Double.parseDouble(((Evento) s.get(i)).getLongitude()));
                        map.addMarker(new MarkerOptions().position(location).title((((Evento) s.get(i)).getCatecategory()))
                                .icon(((Evento)s.get(i)).getCatecategory().equals("Incendio")?
                                        BitmapDescriptorFactory.defaultMarker(HUE_RED) :
                                        ((Evento)s.get(i)).getCatecategory().equals("Roubo")?
                                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA):
                                                ((Evento)s.get(i)).getCatecategory().equals("Acidente")?
                                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW):
                                                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE) ).snippet(((Evento)s.get(i)).getId()));

                    }

                }
            });
        }
    }

    private class HttpPostElastic extends AsyncTask<Boolean, Void, Boolean> {
        public Evento ocorrido;
        HttpPostElastic(Evento evento){
            ocorrido = evento;
        }
        @Override
        protected void onPreExecute() {

            Toast toast = Toast.makeText(getContexto(), "Execultando.....", LENGTH_SHORT);
            toast.show();

        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            Boolean t = HttpHelper.doPost(ocorrido);
            return t;

        }

        @Override
        protected void onPostExecute(final Boolean s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getContexto(), "Salvo Com sucesso.....", LENGTH_SHORT);
                    toast.show();

                }
            });
        }
    }

    private class HttpDeleteElastic extends AsyncTask<Boolean, Void, Boolean> {
        public String id;
        HttpDeleteElastic(String id){
            this.id = id;
        }
        @Override
        protected void onPreExecute() {

            Toast toast = Toast.makeText(getContexto(), "Excluido Evento.....", LENGTH_SHORT);
            toast.show();

        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            Boolean t = HttpHelper.doDelete(this.id);
            return t;

        }

        @Override
        protected void onPostExecute(final Boolean s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getContexto(), "Excluido Com sucesso.....", LENGTH_SHORT);
                    toast.show();

                }
            });
        }
    }
}
