package com.teste1.edu.tasabendo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        MapaFragment mapaFragment = new MapaFragment();
        mapaFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragLayout,mapaFragment).commit();
    }
}
