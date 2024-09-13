package com.karmadev.drivecat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karmadev.drivecat.databinding.ActivityTransportBinding;
import com.karmadev.drivecat.fragment.ActividadFragment;
import com.karmadev.drivecat.fragment.HomeFragment;
import com.karmadev.drivecat.fragment.MenuFragment;
import com.karmadev.drivecat.fragment.PerfilFragment;

public class TransportActivity extends AppCompatActivity {

    ActivityTransportBinding binding; // Variable para el enlace de vista utilizando View Binding
    FirebaseFirestore data;
    TextView bienvenidoTransportista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        data = FirebaseFirestore.getInstance();
        bienvenidoTransportista = findViewById(R.id.bienvenidoTransportista);

        String cedula = getIntent().getStringExtra("cedula");
        obtenerNombre(cedula);


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {  // Configurar el escuchador de eventos para la barra de navegación inferior
            // Verificar qué elemento de la barra de navegación se ha seleccionado
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            }

            else if (item.getItemId() == R.id.reservacion) {
                replaceFragment(new ActividadFragment());
            }

            else if (item.getItemId() == R.id.perfil) {
                replaceFragment(new PerfilFragment());
            }

            else {
                replaceFragment(new MenuFragment());
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
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void obtenerNombre(String cedula){
        data.collection("Transportistas").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nombreUsuario = document.getString("Nombre");
                    if (nombreUsuario != null) {
                        bienvenidoTransportista.setText("¡Hola, " + nombreUsuario+"!");
                    } else {
                        bienvenidoTransportista.setText("Nombre de usuario no disponible");  //Validaciones de emergencia :c
                    }
                } else {
                    bienvenidoTransportista.setText("Documento no encontrado"); //Validaciones de emergencia :c
                }
            } else {
                bienvenidoTransportista.setText("Error al obtener el nombre de usuario");  //Validaciones de emergencia :c
            }
        });
    }
}
