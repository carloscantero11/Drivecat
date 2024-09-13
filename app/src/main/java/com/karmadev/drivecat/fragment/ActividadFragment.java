package com.karmadev.drivecat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.adapter.ActividadAdapter;
import com.karmadev.drivecat.model.Actividad;


import java.util.ArrayList;
import java.util.List;

public class ActividadFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActividadAdapter adapter;
    private List<Actividad> actividadList;
    private FirebaseFirestore data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividad, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        data = FirebaseFirestore.getInstance();
        actividadList = new ArrayList<>();

        // Extraer datos de Firestore
        data.collection("Reservación").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Obtener datos del documento
                                String cedula = document.getString("Cédula");
                                String nombre = document.getString("Nombre");
                                String apellido = document.getString("Apellido");
                                String ruta = document.getString("Ruta");

                                // Agregar datos a la lista
                                actividadList.add(new Actividad(nombre+" "+apellido, ruta, cedula));
                            }

                            // Actualizar el RecyclerView después de obtener los datos
                            adapter.notifyDataSetChanged();
                        } else {
                            // Manejar errores
                        }
                    }
                });

        adapter = new ActividadAdapter(getContext(), actividadList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
