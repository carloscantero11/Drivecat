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

public class LoginActivity extends AppCompatActivity {

    Button btnLogearse;  //Referencia de boton de Loguearse
    EditText textCedula, textContraseña; //Referenia de los campos de texto
    FirebaseFirestore data;  //Referencia de mis bases de datos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        data = FirebaseFirestore.getInstance();

        textCedula = findViewById(R.id.textCedula);
        textContraseña = findViewById(R.id.textContraseña);
        btnLogearse = findViewById(R.id.btnLogearse);

        final String rol = getIntent().getStringExtra("rol");

        //Método para Iniciar Sesión
        btnLogearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar(rol);
            }
        });
    }

    private void verificar(String rol) {
        final String cedula = textCedula.getText().toString().trim();
        final String contraseña = textContraseña.getText().toString().trim();


        if(!cedula.isEmpty() && !contraseña.isEmpty()) {
            // Consulta en la colección "usuarios"
            data.collection("Usuarios").document(cedula).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override

                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String contraseñaDB = document.getString("Contraseña");
                            if (contraseñaDB.equals(contraseña)) {  // La contraseña es válidas

                                Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                                intent.putExtra("cedula", cedula); //Validación para identificar la cedula en el intent
                                startActivity(intent);
                            } else {
                                // Contraseña incorrecta
                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Si el usuario no se encuentra en la colección "Usuarios" de Firebase
                            // Toca consultar en la colección "Transportistas"
                            data.collection("Transportistas").document(cedula).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String contraseñaDB = document.getString("Contraseña");
                                            if (contraseñaDB.equals(contraseña)) {      // Las contraseña es válida

                                                Intent intent = new Intent(LoginActivity.this, TransportActivity.class);
                                                intent.putExtra("cedula", cedula);    //Validación para identificar la cedula en el intent
                                                startActivity(intent);
                                            } else {
                                                // Contraseña incorrecta
                                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // Usuario no encontrado en la colección "Transportistas"
                                            Toast.makeText(LoginActivity.this, "Transportista no encontrado", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Error en la consulta a la colección "Transportistas"
                                        Toast.makeText(LoginActivity.this, "Error, contactate con soporte", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        // Error en la consulta a la colección "Usuarios"
                        Toast.makeText(LoginActivity.this, "Error, contactate con soporte", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

}