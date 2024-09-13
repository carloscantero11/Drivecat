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

import com.karmadev.drivecat.LoginTaquillaActivity;
import com.karmadev.drivecat.MainActivity;
import com.karmadev.drivecat.R;

import com.karmadev.drivecat.adiciones.ModificarTrActivity;

public class MenuFragment extends Fragment {

    Button cerrarsesion;
    Button taquilla, modificarDatos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        // Instanciar botones
        cerrarsesion = root.findViewById(R.id.cerrarsesion);
        taquilla = root.findViewById(R.id.taquilla);
        modificarDatos = root.findViewById(R.id.modificarDatos);

        String cedula = getActivity().getIntent().getStringExtra("cedula");

        // Método para Clickear Cerrar Sesión
        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        // Método para Clickear Taquilla
        taquilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginTaquillaActivity(cedula);
            }
            private void goToLoginTaquillaActivity(String cedula) {    // Método para pasar de una pantalla a otra
                Intent intent = new Intent(getActivity(), LoginTaquillaActivity.class);
                intent.putExtra("cedula", cedula); //Validación para identificar la cedula en el intent
                startActivity(intent);
            }
        });

        modificarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginTaquillaActivity(cedula);
            }
            private void goToLoginTaquillaActivity(String cedula) {    // Método para pasar de una pantalla a otra
                Intent intent = new Intent(getActivity(), ModificarTrActivity.class);
                intent.putExtra("cedula", cedula);
                startActivity(intent);
            }
        });


        return root;
    }

    private void goToMainActivity() {    // Método para pasar de una pantalla a otra
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
}
