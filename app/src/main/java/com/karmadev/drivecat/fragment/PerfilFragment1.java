package com.karmadev.drivecat.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karmadev.drivecat.LoginActivity;
import com.karmadev.drivecat.MainActivity;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.SelecOptionActivity;
import com.karmadev.drivecat.adiciones.RutaExtraurbanaActivity;
import com.karmadev.drivecat.adiciones.RutaUrbanaActivity;


public class PerfilFragment1 extends Fragment {

    FirebaseFirestore data;
    TextView perfilUsuario;
    Button urbana, extraurbana;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfil1, container, false);

        perfilUsuario = root.findViewById(R.id.perfilUsuario);

        urbana = root.findViewById(R.id.urbana);
        extraurbana = root.findViewById(R.id.extraurbana);


        String cedula = getActivity().getIntent().getStringExtra("cedula");

        data = FirebaseFirestore.getInstance();
        obtenerDatosPerfil(cedula);

        urbana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rutaUrbana();
            }
        });

        extraurbana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rutaExtraurbana();
            }
        });
        return root;
    }

    private void rutaUrbana() {    //Metodo para pasar de una pantalla a otra
        Intent intent = new Intent(getActivity(), RutaUrbanaActivity.class);
        startActivity(intent);
    }

    private void rutaExtraurbana() {    //Metodo para pasar de una pantalla a otra
        Intent intent = new Intent(getActivity(), RutaExtraurbanaActivity.class);
        startActivity(intent);
    }

    private void obtenerDatosPerfil(String cedula){
        data.collection("Usuarios").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nombreUsuario = document.getString("Nombre");
                    String apellidoUsuario = document.getString("Apellido");
                    String carreraUsuario = document.getString("Carrera");
                    String telefonoUsuario = document.getString("Teléfono");

                    perfilUsuario.setText(nombreUsuario+" "+apellidoUsuario+"\n\n"+cedula+"\n\n"+telefonoUsuario+"\n\n"+carreraUsuario);
                }
                else {
                    perfilUsuario.setText("Documento no encontrado"); //Validaciones de emergencia :c
                }
            }
            else {
                perfilUsuario.setText("Error al obtener el perfil de usuario");  //Validaciones de emergencia  :c
            }
        });
    }
}