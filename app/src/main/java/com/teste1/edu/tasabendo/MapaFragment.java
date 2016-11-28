package com.teste1.edu.tasabendo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {
    MarkerOptions markerOptions;
    Marker marker;

    private GoogleMap map;// obj que controla o Google Maps

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        //recupera o fragment que esta no layout
        //utiliza o getChildFramgentManager() pois e um fragment dentro do outro
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);
        //Inicia o google maps dentro do fragment
        mapFragment.getMapAsync(this);
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // o metodo onMapReady(map)
        this.map = googleMap;
        //cria o objeto LatLng com a coordenada do exemplo
        //LatLng location = new LatLng(-23.478951, -46.746379); //coordenada de exemplo
        LatLng location = new LatLng(this.map.getCameraPosition().target.latitude, this.map.getCameraPosition().target.longitude); //coordenada de exemplo
        //Posiciona o mapa na coordenada indicada (zoom = 13)
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,13);
        this.map.moveCamera(update);
        //Marcador no local
        this.marker = this.map.addMarker(new MarkerOptions().title("Lugar Exemplo").snippet("Lugar Familiar").position(location));
        //tipo do mapa (normal, satelite, terreno ou hibrido)
        this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // a propria classe implenta o Listener
        this.map.setOnCameraMoveListener(this);
        this.map.setOnCameraIdleListener(this);

        Toast toast = Toast.makeText(getContexto(), "Mapa mudou "+ this.map.getCameraPosition().target.latitude+ " "+this.map.getCameraPosition().target.longitude ,Toast.LENGTH_LONG);
        toast.show();
    }

    public Context getContexto(){
        return this.getContext();
    }

    @Override
    public void onCameraMove() {
        this.marker.setPosition(new LatLng(this.map.getCameraPosition().target.latitude,this.map.getCameraPosition().target.longitude));
        //Toast toast = Toast.makeText(getContexto(), "Camera Move " + this.map.getCameraPosition().target.latitude + " " + this.map.getCameraPosition().target.longitude, Toast.LENGTH_LONG);
        //toast.show();
    }

    @Override
    public void onCameraIdle() {
        Log.d("CameraIdle","CameraIdle "+ this.map.getCameraPosition().target.latitude+ " "+this.map.getCameraPosition().target.longitude );
    }
}
