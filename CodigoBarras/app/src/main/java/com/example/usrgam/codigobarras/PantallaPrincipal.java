package com.example.usrgam.codigobarras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
    }

    public void moverseSalida(View view){
        Intent intent = new Intent(getApplicationContext(),ScanAsistencia.class);
        String estado = "Salida";
        intent.putExtra("Entrada", estado);
        startActivity(intent);
    }
    public void moverseEntrada(View view){
        Intent intent = new Intent(getApplicationContext(),ScanAsistencia.class);
        String estado = "Entrada";
        intent.putExtra("Entrada", estado);
        startActivity(intent);
    }
    public void moverseBreak(View view){
        Intent intent = new Intent(getApplicationContext(),ScanBreak.class);
        startActivity(intent);
    }
}
