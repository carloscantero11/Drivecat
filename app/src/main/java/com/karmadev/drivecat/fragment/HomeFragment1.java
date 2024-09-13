package com.karmadev.drivecat.fragment;

import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karmadev.drivecat.LoginActivity;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.adiciones.SoporteActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HomeFragment1 extends Fragment {

    TextView horario1, horario2, horario3;
    FirebaseFirestore data;
    TextView viajeUrbana, viajeExtraurbana, viajeAdministrativa;
    Button reservar, soporte;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home1, container, false);

        horario1 = root.findViewById(R.id.horario1);
        horario2 = root.findViewById(R.id.horario2);
        horario3 = root.findViewById(R.id.horario3);

        viajeUrbana = root.findViewById(R.id.viajeUrbana);
        viajeExtraurbana = root.findViewById(R.id.viajeExtraurbana);
        viajeAdministrativa = root.findViewById(R.id.viajeAdministrativa);

        reservar = root.findViewById(R.id.reservar);
        soporte = root.findViewById(R.id.soporte);

        data = FirebaseFirestore.getInstance();
        String cedula = getActivity().getIntent().getStringExtra("cedula");

        mostrarViajes(cedula);

        // Se calcula el tiempo hasta el próximo horario de ruta
        long timeUntilNextRoute = calculateTimeUntilNextRoute();

        // Se inicia el temporizador
        startCountdownTimer(timeUntilNextRoute);

        //Botón de Reserva
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaEmergente();
            }
        });

        soporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soporte();
            }
            private void soporte() {    //Metodo para pasar de una pantalla a otra
                Intent intent = new Intent(getActivity(), SoporteActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private long calculateTimeUntilNextRoute() {
        // Se obtiene la hora actual en el uso horario de Venezuela
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Caracas"), Locale.getDefault());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Aquí se definen los horarios de la ruta
        int[] routeHours = {6, 7, 8, 10, 12, 13, 15, 17, 20, 20};
        int[] routeMinutes = {40, 40, 40, 20, 0, 40, 20, 30, 50, 30};

        // Se encuentra el próximo horario de ruta
        for (int i = 0; i < routeHours.length; i++) {
            if ((currentHour < routeHours[i]) || (currentHour == routeHours[i] && currentMinute < routeMinutes[i])) {
                // Se calcula la diferencia de tiempo en milisegundos hasta el próximo horario de ruta
                Calendar nextRouteTime = Calendar.getInstance(TimeZone.getTimeZone("America/Caracas"), Locale.getDefault());
                nextRouteTime.set(Calendar.HOUR_OF_DAY, routeHours[i]);
                nextRouteTime.set(Calendar.MINUTE, routeMinutes[i]);
                nextRouteTime.set(Calendar.SECOND, 0);
                nextRouteTime.set(Calendar.MILLISECOND, 0);

                return nextRouteTime.getTimeInMillis() - System.currentTimeMillis();
            }
        }

        // Si ya ha pasado el último horario de ruta, calcula el tiempo hasta el primer horario del día siguiente
        Calendar nextDayTime = Calendar.getInstance(TimeZone.getTimeZone("America/Caracas"), Locale.getDefault());
        nextDayTime.add(Calendar.DAY_OF_MONTH, 1);
        nextDayTime.set(Calendar.HOUR_OF_DAY, routeHours[0]);
        nextDayTime.set(Calendar.MINUTE, routeMinutes[0]);
        nextDayTime.set(Calendar.SECOND, 0);
        nextDayTime.set(Calendar.MILLISECOND, 0);

        return nextDayTime.getTimeInMillis() - System.currentTimeMillis();
    }

    private void startCountdownTimer(long millisInFuture) {
        new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                // Aqui se actualiza el TextView con el tiempo restante en formato mm (minutos)
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%01d", minutes, seconds);
                horario1.setText(timeLeftFormatted);
                horario2.setText(timeLeftFormatted);
                horario3.setText(timeLeftFormatted);
            }

            public void onFinish() {
                //reservaCaducada();
                startCountdownTimer(calculateTimeUntilNextRoute());
            }
        }.start();
    }

    private void mostrarViajes(String cedula){
        data.collection("Usuarios").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long urbana = document.getLong("Ruta Urbana");
                    Long extraurbana = document.getLong("Ruta Extraurbana");
                    Long administrativa = document.getLong("Ruta Administrativa");

                    viajeUrbana.setText(String.valueOf(urbana));
                    viajeExtraurbana.setText(String.valueOf(extraurbana));
                    viajeAdministrativa.setText(String.valueOf(administrativa));

                }
                else {
                    viajeUrbana.setText("N/a");
                    viajeExtraurbana.setText("N/a");
                    viajeAdministrativa.setText("N/a");//Validaciones de emergencia :c
                }
            }
            else {
                viajeUrbana.setText("N/a");
                viajeExtraurbana.setText("N/a");
                viajeAdministrativa.setText("N/a");  //Validaciones de emergencia  :c
            }
        });
    }

    private void ventanaEmergente() {
        // Inflar el diseño del XML en un objeto Dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.my_option);

        // Enlazar vistas del diseño
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);

        // Acción del botón "Aceptar"
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del radio button seleccionado
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String ruta = "Null";

                // Determinar qué radio button fue seleccionado
                if (selectedId == R.id.radioUrbana) {
                    ruta= "Ruta Urbana";
                } else if (selectedId == R.id.radioExtraurbana) {
                    ruta = "Ruta Extraurbana";
                } else if (selectedId == R.id.radioAdministrativa) {
                    ruta = "Ruta Administrativa";
                }
                else{

                }
                cargaDatos(ruta);

                // Cerrar la ventana emergente
                dialog.dismiss();
            }
        });

        // Mostrar la ventana emergente
        dialog.show();
    }

    private void cargaDatos(String ruta){

        String cedula = getActivity().getIntent().getStringExtra("cedula");

        data.collection("Usuarios").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String nombre = document.getString("Nombre");
                    String apellido = document.getString("Apellido");

                    reservarViaje(nombre, apellido, ruta);
                }
                else {
                    Toast.makeText(getActivity(), "Error al cargar al usuario ", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "Error al cargar la base de datos: ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void reservarViaje(String nombre, String apellido, String ruta) {

        String cedula = getActivity().getIntent().getStringExtra("cedula");

        data.collection("Reservación").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Si existe una reserva, eliminarla antes de guardar la nueva
                    data.collection("Reservación").document(cedula).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Guardar la nueva reserva
                            guardarReserva(nombre, apellido, ruta);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Manejar el error al eliminar la reserva existente
                            Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Si no existe una reserva, guardar la nueva reserva directamente
                    guardarReserva(nombre, apellido, ruta);
                }
            } else {
                Toast.makeText(getActivity(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarReserva(String nombre, String apellido, String ruta) {

        String cedula = getActivity().getIntent().getStringExtra("cedula");

        Map<String, Object> reservacion = new HashMap<>();
        reservacion.put("Cédula",cedula);
        reservacion.put("Nombre", nombre);
        reservacion.put("Apellido", apellido);
        reservacion.put("Ruta", ruta);

        data.collection("Reservación").document(cedula).set(reservacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Reservación Exitosa", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Valida hasta la siguiente hora de salida", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

  /*  private void reservaCaducada() {
        String cedula = getActivity().getIntent().getStringExtra("cedula");

        data.collection("Reservación").document(cedula).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Su reserva a caducado.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    } */
}
