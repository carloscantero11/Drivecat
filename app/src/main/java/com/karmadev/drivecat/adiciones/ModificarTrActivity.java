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
import com.karmadev.drivecat.TransportActivity;
import com.karmadev.drivecat.UsuarioActivity;
import com.karmadev.drivecat.fragment.MenuFragment1;

import java.util.HashMap;
import java.util.Map;

public class ModificarTrActivity extends AppCompatActivity {

    EditText modificarNombre, modificarApellido, modificarPlaca, modificarTelefono;
    Button modificar;
    FirebaseFirestore data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tr);

        modificarNombre = findViewById(R.id.modificarNombre);
        modificarApellido = findViewById(R.id.modificarApellido);
        modificarPlaca = findViewById(R.id.modificarPlaca);
        modificarTelefono = findViewById(R.id.modificarTelefono);

        modificar = findViewById(R.id.modificar);
        data = FirebaseFirestore.getInstance();

        final String cedula = getIntent().getStringExtra("cedula");

        mostrarDatos(cedula);

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarDatos(cedula);
                volver(cedula);
            }
        });
    }

    private void mostrarDatos(String cedula) {
        data.collection("Transportistas").document(cedula).get().addOnSuccessListener(documentSnapshot -> {

            // Verificamos si el documento existe
            if (documentSnapshot.exists()) {

                // Obtenemos los datos del documento
                String nombre = documentSnapshot.getString("Nombre");
                String apellido = documentSnapshot.getString("Apellido");
                String placa = documentSnapshot.getString("Placa del Vehículo");
                String telefono = documentSnapshot.getString("Teléfono");

                //Se actualizan los datos del usuario para que se muestren en pantalla
                modificarNombre.setText(nombre);
                modificarApellido.setText(apellido);
                modificarPlaca.setText(placa);
                modificarTelefono.setText(telefono);
            }
        }).addOnFailureListener(e -> {
            // Manejar cualquier error que ocurra al obtener los datos de Firestore
            Log.e("ModificarTrActivity", "Error al obtener datos de Firestore", e);
        });
    }


    private void modificarDatos(String cedula) {
        String nuevoNombre = modificarNombre.getText().toString();
        String nuevoApellido = modificarApellido.getText().toString();
        String nuevaPlaca= modificarPlaca.getText().toString();
        String nuevoTelefono = modificarTelefono.getText().toString();

        // Guardamos los datos modificados
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("Nombre", nuevoNombre);
        nuevosDatos.put("Apellido", nuevoApellido);
        nuevosDatos.put("Placa del Vehículo", nuevaPlaca);
        nuevosDatos.put("Teléfono", nuevoTelefono);

        // Actualizamos los datos en Firestore
        data.collection("Transportistas").document(cedula).update(nuevosDatos).addOnSuccessListener(aVoid -> {

            Toast.makeText(ModificarTrActivity.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {

            Toast.makeText(ModificarTrActivity.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
            Log.e("ModificarActivity", "Error al actualizar datos", e);
        });
    }

    private void volver(String cedula){
        Intent intent = new Intent (ModificarTrActivity.this, TransportActivity.class);
        intent.putExtra("cedula", cedula);
        startActivity(intent);
    }

}