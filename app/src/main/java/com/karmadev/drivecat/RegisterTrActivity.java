package com.karmadev.drivecat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterTrActivity extends AppCompatActivity {

    private EditText cedula1, nombre1, apellido1, placa1, telefono1, contraseña1, contraseñac1;
    private Button btnRegistrarse;
    private FirebaseFirestore data;
    private int viajeTotales = 0;
    private int viajeUrbana = 0;
    private int viajeExtraurbana = 0;
    private int viajeAdministrativo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tr);

        data = FirebaseFirestore.getInstance();

        cedula1 = findViewById(R.id.cedula1);
        nombre1 = findViewById(R.id.nombre1);
        apellido1 = findViewById(R.id.apellido1);
        placa1 = findViewById(R.id.placa1);
        telefono1 = findViewById(R.id.telefono1);
        contraseña1 = findViewById(R.id.contraseña1);
        contraseñac1 = findViewById(R.id.contraseñac1);
        btnRegistrarse= findViewById(R.id.btnRegistrarse);

        final String rol = getIntent().getStringExtra("rol");

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroTransporte(rol);
            }
        });
    }

    private void registroTransporte(String rol) {
        String cedula = cedula1.getText().toString();
        String nombre = nombre1.getText().toString();
        String apellido = apellido1.getText().toString();
        String placa = placa1.getText().toString();
        String telefono = telefono1.getText().toString();
        String contraseña = contraseña1.getText().toString();
        String confirmacionContraseña = contraseñac1.getText().toString();

        if (!cedula.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !placa.isEmpty() && !telefono.isEmpty() && !contraseña.isEmpty() && !confirmacionContraseña.isEmpty()) {
            if (!contraseña.equals(confirmacionContraseña)) {
                Toast.makeText(RegisterTrActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                contraseña1.setText("");
                contraseñac1.setText("");
            }
            else {
                guardarDatos(cedula, nombre, apellido, placa, telefono, contraseña, rol);
                Intent intent = new Intent(RegisterTrActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(RegisterTrActivity.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarDatos(String cedula, String nombre, String apellido, String placa, String telefono, String contraseña, String rol){

        Map<String, Object> user = new HashMap<>();
        user.put("Cédula", cedula);
        user.put("Nombre", nombre);
        user.put("Apellido", apellido);
        user.put("Placa del Vehículo", placa);
        user.put("Teléfono", telefono);
        user.put("Contraseña", contraseña);
        //--------------------------------------------------------
        user.put("Viajes Totales", viajeTotales);
        user.put("Ruta Urbana", viajeUrbana);
        user.put("Ruta Extraurbana", viajeExtraurbana);
        user.put("Ruta Administrativa", viajeAdministrativo);

        data.collection(rol).document(cedula).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterTrActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                cedula1.setText("");
                nombre1.setText("");
                apellido1.setText("");
                placa1.setText("");
                telefono1.setText("");
                contraseña1.setText("");
                contraseñac1.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterTrActivity.this, "Error en el registro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}