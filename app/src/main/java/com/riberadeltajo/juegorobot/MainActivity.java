package com.riberadeltajo.juegorobot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PuertaRecycler.listaPuertas.clear();
        PuertaRecycler.listaPuertas.add(new Puerta(BitmapFactory.decodeResource(getResources(), R.drawable.door)));
        PuertaRecycler.listaPuertas.add(new Puerta(BitmapFactory.decodeResource(getResources(), R.drawable.door2)));
        PuertaRecycler.listaPuertas.add(new Puerta(BitmapFactory.decodeResource(getResources(), R.drawable.door3)));
        PuertaRecycler.listaPuertas.add(new Puerta(BitmapFactory.decodeResource(getResources(), R.drawable.door4)));

        Intent i = getIntent();
        int segundos = i.getIntExtra("segundos", 0);
        if(segundos>0){
            TextView tiempo = findViewById(R.id.tiempo);
            tiempo.setText("TIEMPO:\n" + segundos);
        }
    }
}