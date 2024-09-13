package com.karmadev.drivecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelecOptionActivity extends AppCompatActivity {

    Button btnUsuario; //Referenciar Boton de Usuario
    Button btnTransportista; //Referenciar Boton de Transportista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_option);

        //Instaciar
        btnTransportista = findViewById(R.id.btnTransportista);
        btnUsuario = findViewById(R.id.btnUsuario);

        //Método para Clickear Transportista
        btnTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transportista();  //Llamar método
            }
        });

        //Método para Clickear Usuario
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario(); //Llamar método
            }
        });
    }

    private void Transportista() {    ////Registro de Transportista
        //Metodo para pasar de una pantalla a otra
        Intent intent = new Intent(SelecOptionActivity.this, RegisterTrActivity.class);
        intent.putExtra("rol","Transportistas"); //Validación para identificar el proposito del intent
        startActivity(intent);
    }

    private void Usuario() {    //Registro de usuario
        //Metodo para pasar de una pantalla a otra
        Intent intent = new Intent(SelecOptionActivity.this, RegisterActivity.class);
        intent.putExtra("rol","Usuarios"); //Validación para identificar el proposito del intent
        startActivity(intent);
    }
}
