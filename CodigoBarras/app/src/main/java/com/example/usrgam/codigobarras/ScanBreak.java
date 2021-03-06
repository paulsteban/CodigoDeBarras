package com.example.usrgam.codigobarras;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanBreak extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    private static final int REQUESTCAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camID= Camera.CameraInfo.CAMERA_FACING_BACK;
    //  EditText Edirrecion;
    //  String dirrecion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.break_activity);
        SimpleDateFormat tiempo = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        SimpleDateFormat estado = new SimpleDateFormat("a", Locale.getDefault());
        Date date = new Date();

        String fecha = tiempo.format(date);
        String apm = estado.format(date);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // control de permisos
            if (verificarPermisos()) {
                //mensaje
                Toast.makeText(getApplicationContext(), "Scan break", Toast.LENGTH_LONG).show();
            } else {
                solicitarpermisos();
            }
        }
    }

    private void solicitarpermisos() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUESTCAMERA);

    }

    private boolean verificarPermisos() {
        return ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;

    }

    @Override
    protected void onResume() {
        super.onResume();
        int apiVersion= Build.VERSION.SDK_INT;
        if (apiVersion>= Build.VERSION_CODES.M){
            if(verificarPermisos()){
                if(scannerView==null){
                    scannerView=new ZXingScannerView(this);
                    setContentView(scannerView);

                }
                //para que arranque la camara
                scannerView.setResultHandler(this);
                scannerView.startCamera();

            }else {
                solicitarpermisos();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUESTCAMERA:
                if(grantResults.length>0){
                    boolean aceptaPermiso = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(aceptaPermiso){
                        //mensaje
                    }else{
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                requestPermissions(new String[]{Manifest.permission.CAMERA},REQUESTCAMERA);

                            }

                        }
                    }
                }
        }
    }

    @Override
    public void handleResult(Result result) {

        if(result.getText() != null) {

            onPause();
            SimpleDateFormat tiempo = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
            SimpleDateFormat estado = new SimpleDateFormat("a", Locale.getDefault());
            Date date = new Date();

            String fecha = tiempo.format(date);
            String apm = estado.format(date);
            registrarAsistencia(result.getText()+"",fecha,apm);
            Log.e("resultadoBAR:", result.getBarcodeFormat().toString());


        }
    }

    public void registrarAsistencia(final String asistencia, final String fecha, final String tiempo){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanBreak.this,R.style.AlertDialogRed);
                        alertDialog.setMessage("Usuario Repetido");

                        alertDialog.show();
                        Toast toast = Toast.makeText(ScanBreak.this, "Usuario Repetido", Toast.LENGTH_SHORT);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.RED);
                        toast.show();
                        onResume();

                    } else {

                        registro(asistencia,fecha,tiempo);
                  /*      AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanBreak.this,R.style.AlertDialogGreen);
                        alertDialog.setMessage("Te has registrado correctamente :)");
                        alertDialog.show();
                        Toast toast = Toast.makeText(ScanBreak.this,"Te has registrado correctamente :)",Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.GREEN);
                        toast.show();
                        onResume();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        BreakSelectRequest breakSelectRequest = new BreakSelectRequest(fecha, tiempo,asistencia,responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(ScanBreak.this);
        requestQueue.add(breakSelectRequest);

    }



    public void registro(final String asistencia,String fecha, String tiempo){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanBreak.this,R.style.AlertDialogGreen);
                        alertDialog.setMessage("Te has registrado correctamente :)");
                        alertDialog.show();
                        Toast toast = Toast.makeText(ScanBreak.this,"Te has registrado correctamente :)",Toast.LENGTH_LONG);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.GREEN);
                        toast.show();
                        onResume();

                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        BreakInsertRequest breakInsertRequest = new BreakInsertRequest(fecha,tiempo,asistencia,responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(ScanBreak.this);
        requestQueue.add(breakInsertRequest);

    }
}