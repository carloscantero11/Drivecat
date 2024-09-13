package com.karmadev.drivecat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.karmadev.drivecat.MainActivity;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.adiciones.ModificarActivity;


public class MenuFragment1 extends Fragment {

    Button cerrarsesion1, modificarDatos1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu1, container, false);
        cerrarsesion1 = root.findViewById(R.id.cerrarsesion1);
        modificarDatos1 = root.findViewById(R.id.modificarDatos1);

        String cedula = getActivity().getIntent().getStringExtra("cedula");


        modificarDatos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), ModificarActivity.class);
                intent.putExtra("cedula",cedula);
                startActivity(intent);
            }
        });


        // Método para Clickear Cerrar Sesión
        cerrarsesion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cerrarSesion();
            }

            private void cerrarSesion() {    // Método para pasar de una pantalla a otra
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Cerrar Sesión");
                builder.setMessage("¿Está seguro de cerrar sesión?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }

                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aqui no se hace nada y la vida sigueXD
                    }
                });
                // Mostrar el diálogo
                builder.show();
            }
        });


        return root;
    }
}