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
import com.karmadev.drivecat.LoginTaquillaActivity;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.adiciones.HistorialActivity;

public class PerfilFragment extends Fragment {

    TextView perfilTransportista;
    Button historial;
    FirebaseFirestore data;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        perfilTransportista = root.findViewById(R.id.perfilTransportista);
        historial = root.findViewById(R.id.historial);

        String cedula = getActivity().getIntent().getStringExtra("cedula");

        data = FirebaseFirestore.getInstance();

        // Método para Clickear Historial
        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historial(cedula);
            }
            private void historial(String cedula) {    // Método para pasar de una pantalla a otra
                Intent intent = new Intent(getActivity(), HistorialActivity.class);
                intent.putExtra("cedula", cedula); //Validación para identificar la cedula en el intent
                startActivity(intent);
            }
        });
        obtenerDatosPerfil(cedula);

        return root;
    }

    private void obtenerDatosPerfil(String cedula){
        data.collection("Transportistas").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nombreTransportista = document.getString("Nombre");
                    String apellidoTransportista = document.getString("Apellido");
                    String PlacaTransportista = document.getString("Placa del Vehículo");
                    String telefonoTransportista = document.getString("Teléfono");
                    Long totalViajes = document.getLong("Viajes Totales");

                    perfilTransportista.setText(nombreTransportista+" "+apellidoTransportista+"\n\n"+cedula+"\n\n"+telefonoTransportista+"\n\n"+PlacaTransportista);
                }
                else {
                    perfilTransportista.setText("Documento no encontrado"); //Validaciones de emergencia :c
                }
            }
            else {
                perfilTransportista.setText("Error al obtener el perfil de usuario");  //Validaciones de emergencia  :c
            }
        });
    }
}