package com.karmadev.drivecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnInicio; //Referenciar Boton de Inicio de Sesión
    Button btnRegistro; //Referenciar Boton de Registro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instaciar
        btnInicio = findViewById(R.id.btnInicio);
        btnRegistro = findViewById(R.id.btnRegistro);

        //Método para Clickear Iniciar Sesión
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicioSesion();
            }
        });

        //Método para Clickear Registro
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }
        });
    }
    private void inicioSesion() {    //Metodo para pasar de una pantalla a otra
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void registro() {    //Metodo para pasar de una pantalla a otra
        Intent intent = new Intent(MainActivity.this, SelecOptionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Crear un Intent para salir de la aplicación
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}