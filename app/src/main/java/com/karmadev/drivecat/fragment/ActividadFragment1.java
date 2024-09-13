package com.karmadev.drivecat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.adapter.ActividadAdapter;
import com.karmadev.drivecat.adapter.MovimientosAdapter;
import com.karmadev.drivecat.model.Actividad;
import com.karmadev.drivecat.model.Movimientos;

import java.util.ArrayList;
import java.util.List;

public class ActividadFragment1 extends Fragment {



    private RecyclerView recyclerView;
    private MovimientosAdapter adapter;
    private List<Movimientos> registroMovimientosList;
    private FirebaseFirestore data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividad1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        data = FirebaseFirestore.getInstance();
        registroMovimientosList = new ArrayList<>();

        String cedula = getActivity().getIntent().getStringExtra("cedula");
        // Extraer datos de Firestore
        data.collection("Movimientos").document(cedula).collection("Registros").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener datos del documento
                        Long id = document.getLong("ID");
                        String nombre = document.getString("Nombre");
                        String apellido = document.getString("Apellido");
                        String ruta = document.getString("Ruta");
                        String nombreTransportista = document.getString("Nombre Transportista");
                        String apellidoTransportista = document.getString("Apellido Transportista");
                        String placaVehiculo = document.getString("Placa del Vehículo");
                        String fecha = document.getString("Fecha");

                        // Agregar datos a la lista
                        registroMovimientosList.add(new Movimientos(id, "Nombre: " + nombre + " " + apellido,  "Ruta: " + ruta,  "Chofer: " + nombreTransportista + " " +  apellidoTransportista, "Placa: " +  placaVehiculo,  fecha));
                    }

                    // Actualizar el RecyclerView después de obtener los datos
                    adapter.notifyDataSetChanged();
                } else {
                    // Manejar errores
                }
            }
        });

        adapter = new MovimientosAdapter(getContext(), registroMovimientosList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}