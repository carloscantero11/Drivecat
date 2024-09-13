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

public class RegisterActivity extends AppCompatActivity {

    private EditText cedula, nombre, apellido, carrera, telefono, contraseña, contraseñac;
    private Button registrarse;
    private FirebaseFirestore data;
    private int viajeUrbana = 0;
    private int viajeExtraurbana = 0;
    private int viajeAdministrativo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        data = FirebaseFirestore.getInstance();

        cedula = findViewById(R.id.cedula);
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        carrera = findViewById(R.id.carrera);
        telefono = findViewById(R.id.telefono);
        contraseña = findViewById(R.id.contraseña);
        contraseñac = findViewById(R.id.contraseñac);
        registrarse= findViewById(R.id.registrarse);

        final String rol = getIntent().getStringExtra("rol");

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroUsuario(rol);
            }
        });
    }

    private void registroUsuario(String rol) {
        String cedula1 = cedula.getText().toString();
        String nombre1 = nombre.getText().toString();
        String apellido1 = apellido.getText().toString();
        String carrera1 = carrera.getText().toString();
        String telefono1 = telefono.getText().toString();
        String contraseña1 = contraseña.getText().toString();
        String confirmacionContraseña1 = contraseñac.getText().toString();


        if (!cedula1.isEmpty() && !nombre1.isEmpty() && !apellido1.isEmpty() && !carrera1.isEmpty() && !telefono1.isEmpty() && !contraseña1.isEmpty() && !confirmacionContraseña1.isEmpty()) {
            if (!contraseña1.equals(confirmacionContraseña1)) {
                Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                contraseña.setText("");
                contraseñac.setText("");
            }
            else {
                guardarDatos(cedula1, nombre1, apellido1, carrera1, telefono1, contraseña1, rol);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(RegisterActivity.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarDatos(String cedula1, String nombre1, String apellido1, String carrera1, String telefono1, String contraseña1, String rol){

        Map<String, Object> user = new HashMap<>();
        user.put("Cédula", cedula1);
        user.put("Nombre", nombre1);
        user.put("Apellido", apellido1);
        user.put("Carrera", carrera1);
        user.put("Teléfono", telefono1);
        user.put("Contraseña", contraseña1);
        //--------------------------------------------------------
        user.put("Ruta Urbana", viajeUrbana);
        user.put("Ruta Extraurbana", viajeExtraurbana);
        user.put("Ruta Administrativa", viajeAdministrativo);

        data.collection(rol).document(cedula1).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                cedula.setText("");
                nombre.setText("");
                apellido.setText("");
                carrera.setText("");
                telefono.setText("");
                contraseña.setText("");
                contraseñac.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error en el registro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}