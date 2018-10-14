package com.example.usrgam.codigobarras;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {
TextView registro,estado;
String asistencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        registro = (TextView) findViewById(R.id.TextRegistro);
        estado = (TextView) findViewById(R.id.textestado);
        if((getIntent().getExtras().getString("loging"))!= null) {
            asistencia = (getIntent().getExtras().getString("loging"));

            registro.setText(asistencia);
            registrarAsistencia(asistencia+"");
            }
    }
    public void registrarAsistencia(final String asistencia){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        estado.setText("Usuario Repetido");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Registro.this,R.style.AlertDialogRed);
                        alertDialog.setMessage("Usuario Repetido");
                        alertDialog.show();
                        Toast toast = Toast.makeText(Registro.this, "Usuario Repetido", Toast.LENGTH_SHORT);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        toast.show();

                    } else {
                        registro(asistencia);
                        // alertDialog.setMessage("REgistrando...");
                        estado.setText("Te has registrado correctamente :)");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Registro.this,R.style.AlertDialogGreen);
                        alertDialog.setMessage("Te has registrado correctamente :)");
                        alertDialog.show();
                        Toast toast = Toast.makeText(Registro.this,"Te has registrado correctamente :)",Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.GREEN);
                        toast.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AsistenciaRequest asistenciaRequest = new AsistenciaRequest(asistencia, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(Registro.this);
        requestQueue.add(asistenciaRequest);

    }

    public void registro(final String asistencia){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                    /*   alertDialog.setMessage("Te has registrado correctamente :)");
                        alertDialog.show();
                        Toast toast = Toast.makeText(MainActivity.this,"Te has registrado correctamente :)",Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.GREEN);
                        toast.show();*/

                    } else {

                    /*    alertDialog.setMessage("Error con el registro");
                        alertDialog.show();
                        Toast toast = Toast.makeText(MainActivity.this,"Error con el registro",Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.GREEN);
                        toast.show();*/

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegistroRequest registroRequest = new RegistroRequest(asistencia, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(Registro.this);
        requestQueue.add(registroRequest);

    }
    public void moverse(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
