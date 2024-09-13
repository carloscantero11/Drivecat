package com.karmadev.drivecat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karmadev.drivecat.databinding.ActivityUsuarioBinding;
import com.karmadev.drivecat.fragment.ActividadFragment1;
import com.karmadev.drivecat.fragment.HomeFragment1;
import com.karmadev.drivecat.fragment.MenuFragment1;
import com.karmadev.drivecat.fragment.PerfilFragment1;

public class UsuarioActivity extends AppCompatActivity {

    ActivityUsuarioBinding binding; // Variable para el enlace de vista utilizando View Binding
    FirebaseFirestore data;
    TextView bienvenidoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment1());

        data = FirebaseFirestore.getInstance();
        bienvenidoUsuario = findViewById(R.id.bienvenidoUsuario);

        String cedula = getIntent().getStringExtra("cedula");
        obtenerNombre(cedula);

        binding.bottomNavigationView1.setOnItemSelectedListener(item -> {  // Configurar los eventos para la barra de navegación inferior
            // Verificar qué elemento de la barra de navegación se ha seleccionado
            if (item.getItemId() == R.id.home1) {
                replaceFragment(new HomeFragment1());
            }

            else if (item.getItemId() == R.id.movimiento) {
                replaceFragment(new ActividadFragment1());
            }

            else if (item.getItemId() == R.id.perfil1) {
                replaceFragment(new PerfilFragment1());
            }

            else {
                replaceFragment(new MenuFragment1());
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        // Crear un Intent para salir de la aplicación
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void replaceFragment(Fragment fragment){  // Método para reemplazar un fragmento en el contenedor especificado
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout1,fragment);
        fragmentTransaction.commit();
    }
    private void obtenerNombre(String cedula){
        data.collection("Usuarios").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nombreUsuario = document.getString("Nombre");
                    if (nombreUsuario != null) {
                        bienvenidoUsuario.setText("¡Hola, " + nombreUsuario+"!");
                    } else {
                        bienvenidoUsuario.setText("Nombre de usuario no disponible");  //Validaciones de emergencia :c
                    }
                } else {
                    bienvenidoUsuario.setText("Documento no encontrado"); //Validaciones de emergencia :c
                }
            } else {
                bienvenidoUsuario.setText("Error al obtener el nombre de usuario");  //Validaciones de emergencia  :c
            }
        });
    }
}