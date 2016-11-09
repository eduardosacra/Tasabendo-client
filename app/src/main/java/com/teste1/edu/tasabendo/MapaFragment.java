package com.teste1.edu.tasabendo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapaFragment extends Fragment implements OnMapReadyCallback {


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
        //ativa o botao para mostrar minha localizacao

        //cria o objeto LatLng com a coordenada do exemplo
        LatLng location = new LatLng(-23.478951, -46.746379); //coordenada de exemplo
        //Posiciona o mapa na coordenada indicada (zoom = 13)
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,13);
        this.map.moveCamera(update);
        //Marcador no local
        this.map.addMarker(new MarkerOptions().title("Lugar Exemplo").snippet("Lugar Familiar").position(location));
        //tipo do mapa (normal, satelite, terreno ou hibrido)
        this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }


}
