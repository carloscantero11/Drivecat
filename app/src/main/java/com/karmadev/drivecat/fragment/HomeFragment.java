package com.karmadev.drivecat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.karmadev.drivecat.CaptureAct;
import com.karmadev.drivecat.LoginActivity;
import com.karmadev.drivecat.MainActivity;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.adiciones.SoporteActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class HomeFragment extends Fragment {

    Button scan1, scan2, scan3, finalizar, soporte;
    TextView usuarios1, usuarios2, usuarios3;
    FirebaseFirestore data;
    boolean scan1Pressed, scan2Pressed, scan3Pressed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        scan1 = root.findViewById(R.id.scan1);
        scan2 = root.findViewById(R.id.scan2);
        scan3 = root.findViewById(R.id.scan3);

        usuarios1 = root.findViewById(R.id.usuarios1);
        usuarios2 = root.findViewById(R.id.usuarios2);
        usuarios3 = root.findViewById(R.id.usuarios3);

        finalizar = root.findViewById(R.id.finalizar);
        soporte = root.findViewById(R.id.soporte);

        data = FirebaseFirestore.getInstance();

        

        String cedula = getActivity().getIntent().getStringExtra("cedula");
        mostrarViajes(cedula);

        scan1.setOnClickListener(v -> {
            scan1Pressed = true;
            scan2Pressed = false;
            scan3Pressed = false;
            scanCode();
        });

        scan2.setOnClickListener(v -> {
            scan1Pressed = false;
            scan2Pressed = true;
            scan3Pressed = false;
            scanCode();
        });

        scan3.setOnClickListener(v -> {
            scan1Pressed = false;
            scan2Pressed = false;
            scan3Pressed = true;
            scanCode();
        });

        finalizar.setOnClickListener(v -> {
            finalizarViajes(cedula);
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

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("                     ¡Por favor!\n Escanear el QR de la tarjeta Drivecat\n\nEncender Flash: Tecla de subir volumen.\nApagar Flash:  Tecla de bajar volumen.");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result != null && !result.getContents().isEmpty()) {
            String scanearContenido = result.getContents();
            data.collection("Usuarios").document(scanearContenido).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        manejarResultado(scanearContenido, document);
                    }
                    else {
                        // Documento no encontrado para el código escaneado
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Error");
                        builder.setMessage("El código QR que escaneo, no es válido");
                        builder.setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss()).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Error al consultar la base de datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

    private void manejarResultado(String scanearContenido, DocumentSnapshot document) {
        String key = document.getString("Cédula");
        String nombre = document.getString("Nombre");
        String apellido = document.getString("Apellido");
        Long urbana = document.getLong("Ruta Urbana");
        Long extraurbana = document.getLong("Ruta Extraurbana");
        Long administrativa = document.getLong("Ruta Administrativa");

        String cedula = getActivity().getIntent().getStringExtra("cedula");

        if (scanearContenido.equals(key)) {
            if (scan1Pressed) {
                actualizarRuta(key, urbana, scanearContenido, "Ruta Urbana", nombre, apellido, cedula);
            }
            else if (scan2Pressed) {
                actualizarRuta(key, extraurbana, scanearContenido, "Ruta Extraurbana", nombre, apellido, cedula);
            }
            else if (scan3Pressed) {
                actualizarRuta(key, administrativa, scanearContenido, "Ruta Administrativa", nombre, apellido, cedula);
            }
        }
        else {
            // QR no válido
            Toast.makeText(getActivity(), "El QR escaneado no es válido.", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarRuta(String key, Long rutaContador, String scanearContenido, String tipoRuta, String nombre, String apellido, String cedula) {

        if (rutaContador != null && rutaContador > 0) {
            data.collection("Usuarios").document(scanearContenido).update(tipoRuta, rutaContador - 1).addOnSuccessListener(aVoid -> {

                // Actualizar los datos del transportista
                data.collection("Transportistas").document(cedula).get().addOnSuccessListener(documentSnapshot -> {

                    Long rutaTransportista = documentSnapshot.getLong(tipoRuta);

                    if (rutaTransportista != null) {  // Actualizar la ruta del transportista

                        data.collection("Transportistas").document(cedula).update(tipoRuta, rutaTransportista + 1).addOnSuccessListener(aVoid1 -> {
                            // Mostrar diálogo de pago exitoso
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Pago Exitoso");
                            builder.setMessage(nombre + " " + apellido + " realizó un pago");
                            builder.setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss()).show();

                            registroPago(key, nombre, apellido, tipoRuta, cedula);

                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al actualizar la base de datos del transportista", Toast.LENGTH_SHORT).show());
                    }
                    else {
                        Toast.makeText(getActivity(), "Error al obtener la ruta del transportista", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al obtener los datos del transportista", Toast.LENGTH_SHORT).show());

            }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al actualizar la base de datos", Toast.LENGTH_SHORT).show());
        }
        else {
            // El usuario no tiene viajes disponibles en esta ruta
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(nombre + " " + apellido);
            builder.setMessage("No tiene viajes disponibles en esta ruta");
            builder.setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss()).show();
        }
    }

    private void mostrarViajes(String cedula){
        data.collection("Transportistas").document(cedula).addSnapshotListener((documentSnapshot, talk) -> {
            if (talk != null) {
                Toast.makeText(getActivity(), "Error al obtener los datos del transportista: " + talk.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                Long urbana = documentSnapshot.getLong("Ruta Urbana");
                Long extraurbana = documentSnapshot.getLong("Ruta Extraurbana");
                Long administrativa = documentSnapshot.getLong("Ruta Administrativa");

                usuarios1.setText(String.valueOf(urbana != null ? urbana : "N/a"));
                usuarios2.setText(String.valueOf(extraurbana != null ? extraurbana : "N/a"));
                usuarios3.setText(String.valueOf(administrativa != null ? administrativa : "N/a"));
            }
            else {
                usuarios1.setText("N/a");
                usuarios2.setText("N/a");
                usuarios3.setText("N/a");
            }
        });
    }

    private void finalizarViajes(String cedula) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Finalizar Viajes");
        builder.setMessage("¿Está seguro de finalizar los viajes?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Obtener el valor actual de los viajes de las rutas
                Long urbana = Long.parseLong(usuarios1.getText().toString());
                Long extraurbana = Long.parseLong(usuarios2.getText().toString());
                Long administrativa = Long.parseLong(usuarios3.getText().toString());

                // Sumar los viajes de las tres rutas que el transportista tiene
                final Long totalViajes = urbana + extraurbana + administrativa;

                // Actualizar los viajes totales en la base de datos "Transportistas"
                data.collection("Transportistas").document(cedula).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long totalActual = document.getLong("Viajes Totales");

                            // Si no hay ningún valor actual, asumimos 0
                            if (totalActual == null) {
                                totalActual = 0L;
                            }

                            // Sumar los viajes actuales al total obtenido de la base de datos
                            long totalViajesActualizados = totalViajes + totalActual;

                            // Actualizar el total de viajes en la base de datos "Transportistas"
                            data.collection("Transportistas").document(cedula)
                                    .update("Viajes Totales", totalViajesActualizados,
                                            "Ruta Urbana", 0,
                                            "Ruta Extraurbana", 0,
                                            "Ruta Administrativa", 0).addOnSuccessListener(aVoid -> {

                                        // Mostrar mensaje de éxito y actualizar la interfaz de usuario para reflejar los cambios
                                        Toast.makeText(getActivity(), "Viajes finalizados con éxito", Toast.LENGTH_SHORT).show();
                                        usuarios1.setText("0");
                                        usuarios2.setText("0");
                                        usuarios3.setText("0");
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error al actualizar los viajes totales: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            // El documento del transportista no existe
                            Toast.makeText(getActivity(), "El documento del transportista no existe", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error al obtener el documento del transportista
                        Toast.makeText(getActivity(), "Error al obtener el documento del transportista: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void registroPago(String cedula, String nombre, String apellido, String tiporuta, String cedulaTransportista) {
        // Obtener la fecha y hora actual en el huso horario de Venezuela
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Caracas"));
        String fecha = dateFormat.format(new Date());

        data.collection("Movimientos").document(cedula).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                long id;
                if (document.exists()) {
                    // Si el documento existe, obtener el valor del contador y aumentarlo en 1
                    id = document.getLong("contador") + 1;
                } else {
                    // Si el documento no existe, establecer el contador en 1
                    id = 1;
                }

                // Actualizar el contador en la base de datos
                Map<String, Object> contadorData = new HashMap<>();
                contadorData.put("contador", id);
                data.collection("Movimientos").document(cedula).set(contadorData, SetOptions.merge());

                // Obtener los detalles del transportista
                data.collection("Transportistas").document(cedulaTransportista).get().addOnCompleteListener(transportistaTask -> {
                    if (transportistaTask.isSuccessful()) {
                        DocumentSnapshot transportistaSnapshot = transportistaTask.getResult();
                        if (transportistaSnapshot.exists()) {
                            String nombreTransportista = transportistaSnapshot.getString("Nombre");
                            String apellidoTransportista = transportistaSnapshot.getString("Apellido");
                            String placa = transportistaSnapshot.getString("Placa del Vehículo");

                            // Crear el mapa para el registro
                            Map<String, Object> user = new HashMap<>();
                            user.put("ID", id);
                            user.put("Cédula", cedula);
                            user.put("Nombre", nombre);
                            user.put("Apellido", apellido);
                            user.put("Ruta", tiporuta);
                            user.put("Cédula Transportista", cedulaTransportista);
                            user.put("Nombre Transportista", nombreTransportista);
                            user.put("Apellido Transportista", apellidoTransportista);
                            user.put("Placa del Vehículo", placa);
                            user.put("Fecha", fecha); // Agregar la fecha y hora de Venezuela al mapa

                            // Guardar el registro en la colección "Movimientos"
                            data.collection("Movimientos").document(cedula).collection("Registros").document(String.valueOf(id)).set(user);
                        } else {
                            Toast.makeText(getActivity(), "No se encontró el transportista con la cédula especificada", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error al obtener información del transportista", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Error al obtener información del contador", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
