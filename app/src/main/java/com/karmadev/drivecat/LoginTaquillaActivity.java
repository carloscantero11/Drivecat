package com.karmadev.drivecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginTaquillaActivity extends AppCompatActivity {

    Button logearse;   //Referenia de mi boton
    EditText taquillaContraseña; //Referenia de los campos de texto
    FirebaseFirestore data;  //Referencia de mis bases de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_taquilla);

        //Instaciar
        logearse = findViewById(R.id.logearse);
        taquillaContraseña = findViewById(R.id.taquillaContraseña);

        data = FirebaseFirestore.getInstance();

        final String cedula = getIntent().getStringExtra("cedula");

        //Método para Iniciar Sesión
        logearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar(cedula);
            }
        });
    }

    private void verificar(String cedula) {
        final String taquilla = taquillaContraseña.getText().toString().trim();

        data.collection("Transportistas").document(cedula).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.contains("Taquilla")) {
                            String contraseñaDB = document.getString("Taquilla");
                            if (contraseñaDB.equals(taquilla)) {  // La contraseña es válida
                                Intent intent = new Intent(LoginTaquillaActivity.this, TaquillaActivity.class);
                                intent.putExtra("cedula", cedula); // Validación para identificar la cedula en el intent
                                startActivity(intent);
                            } else {
                                // Contraseña incorrecta
                                Toast.makeText(LoginTaquillaActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Campo "Taquilla" no encontrado
                            Toast.makeText(LoginTaquillaActivity.this, "Acceso Denegado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Usuario no encontrado en la colección "Transportistas"
                        Toast.makeText(LoginTaquillaActivity.this, "Usted no tiene acceso al Modo Taquilla", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error en la consulta a la colección "Transportistas"
                    Toast.makeText(LoginTaquillaActivity.this, "Usted no tiene acceso al Modo Taquilla", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}