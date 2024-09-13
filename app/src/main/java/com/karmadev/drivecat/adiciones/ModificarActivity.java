package com.karmadev.drivecat.adiciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.UsuarioActivity;
import com.karmadev.drivecat.fragment.MenuFragment1;

import java.util.HashMap;
import java.util.Map;

public class ModificarActivity extends AppCompatActivity {

    EditText modificarNombre1, modificarApellido1, modificarCarrera1, modificarTelefono1;
    Button modificar1;
    FirebaseFirestore data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        modificarNombre1 = findViewById(R.id.modificarNombre1);
        modificarApellido1 = findViewById(R.id.modificarApellido1);
        modificarCarrera1 = findViewById(R.id.modificarCarrera1);
        modificarTelefono1 = findViewById(R.id.modificarTelefono1);

        modificar1 = findViewById(R.id.modificar1);

        data = FirebaseFirestore.getInstance();

        final String cedula = getIntent().getStringExtra("cedula");

        mostrarDatos(cedula);

        modificar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarDatos(cedula);
                volver(cedula);
            }
        });
    }

    private void mostrarDatos(String cedula) {
        data.collection("Usuarios").document(cedula).get().addOnSuccessListener(documentSnapshot -> {

            // Verificamos si el documento existe
            if (documentSnapshot.exists()) {

                // Obtenemos los datos del documento
                String nombre = documentSnapshot.getString("Nombre");
                String apellido = documentSnapshot.getString("Apellido");
                String carrera = documentSnapshot.getString("Carrera");
                String telefono = documentSnapshot.getString("Teléfono");

                //Se actualizan los datos del usuario para que se muestren en pantalla
                modificarNombre1.setText(nombre);
                modificarApellido1.setText(apellido);
                modificarCarrera1.setText(carrera);
                modificarTelefono1.setText(telefono);
            }
        });
    }

    private void modificarDatos(String cedula) {
        String nuevoNombre = modificarNombre1.getText().toString();
        String nuevoApellido = modificarApellido1.getText().toString();
        String nuevaCarrera = modificarCarrera1.getText().toString();
        String nuevoTelefono = modificarTelefono1.getText().toString();

        // Guardamos los datos modificados
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("Nombre", nuevoNombre);
        nuevosDatos.put("Apellido", nuevoApellido);
        nuevosDatos.put("Carrera", nuevaCarrera);
        nuevosDatos.put("Teléfono", nuevoTelefono);

        // Actualizamos los datos en Firestore
        data.collection("Usuarios").document(cedula).update(nuevosDatos).addOnSuccessListener(aVoid -> {

                    Toast.makeText(ModificarActivity.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> {

                    Toast.makeText(ModificarActivity.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
                    Log.e("ModificarActivity", "Error al actualizar datos", e);
                });
    }

    private void volver(String cedula){
        Intent intent = new Intent (ModificarActivity.this, UsuarioActivity.class);
        intent.putExtra("cedula", cedula);
        startActivity(intent);
    }

}