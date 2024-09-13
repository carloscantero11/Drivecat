package com.karmadev.drivecat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.FileReader;

public class TaquillaActivity extends AppCompatActivity {

    Button cerrarsesion2, recarga;
    private TextView viaje1, viaje2, viaje3;
    private int viajes1 = 0, viajes2 = 0, viajes3 = 0;
    FirebaseFirestore data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taquilla);

        viaje1 = findViewById(R.id.viaje1);
        viaje2 = findViewById(R.id.viaje2);
        viaje3 = findViewById(R.id.viaje3);
        cerrarsesion2 = findViewById(R.id.cerrarsesion2);
        recarga = findViewById(R.id.recarga);

        data = FirebaseFirestore.getInstance();

        recargasViajes();

        cerrarsesion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectAuth();
            }
            private void goToSelectAuth() {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaquillaActivity.this);
                builder.setTitle("Cerrar Sesión");
                builder.setMessage("¿Está seguro de cerrar sesión?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TaquillaActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aqui no se hace nada y la vida sigueXD
                    }
                });
                builder.show();
            }
        });

        recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
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


    private void recargasViajes() {
        findViewById(R.id.disminuir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disminuirViajes(viaje1);
            }
        });

        findViewById(R.id.aumentar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aumentarViajes(viaje1);
            }
        });

        findViewById(R.id.disminuir1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disminuirViajes(viaje2);
            }
        });

        findViewById(R.id.aumentar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aumentarViajes(viaje2);
            }
        });

        findViewById(R.id.disminuir2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disminuirViajes(viaje3);
            }
        });

        findViewById(R.id.aumentar2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aumentarViajes(viaje3);
            }
        });
    }

    private void aumentarViajes(TextView viaje) {
        if (viaje == viaje1 && viajes1 < 20) {
            viajes1++;
            viaje.setText(String.valueOf(viajes1));
        } else if (viaje == viaje2 && viajes2 < 20) {
            viajes2++;
            viaje.setText(String.valueOf(viajes2));

        } else if (viaje == viaje3 && viajes3 < 20) {
            viajes3++;
            viaje.setText(String.valueOf(viajes3));
        }
    }

    private void disminuirViajes(TextView viaje) {
        if (viaje == viaje1 && viajes1 > 0) {
            viajes1--;
            viaje.setText(String.valueOf(viajes1));
        } else if (viaje == viaje2 && viajes2 > 0) {
            viajes2--;
            viaje.setText(String.valueOf(viajes2));
        } else if (viaje == viaje3 && viajes3 > 0) {
            viajes3--;
            viaje.setText(String.valueOf(viajes3));
        }
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
                        // Verificar si el usuario tiene una cédula válida
                        Long urbana = document.getLong("Ruta Urbana");
                        Long extraurbana = document.getLong("Ruta Extraurbana");
                        Long administrativa = document.getLong("Ruta Administrativa");

                        updateRoute(scanearContenido, urbana, extraurbana,administrativa);
                        Toast.makeText(TaquillaActivity.this, "Recarga Exitosa", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        // Documento no encontrado para el código escaneado
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaquillaActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("El código QR que escaneo, no es válido");
                        builder.setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss()).show();
                    }
                }
                else {
                    Toast.makeText(TaquillaActivity.this, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

    private void updateRoute(String scanearContenido, Long urbana, Long extraurbana, Long administrativa) {
        // Aqui se van actualizar los viajes para cada tipo de ruta
        data.collection("Usuarios").document(scanearContenido)
                .update("Ruta Urbana", viajes1 + urbana,
                        "Ruta Extraurbana", viajes2 + extraurbana,
                        "Ruta Administrativa", viajes3 + administrativa)
                .addOnSuccessListener(aVoid -> {

                    viajes1 = 0;
                    viajes2 = 0;
                    viajes3 = 0;

                    viaje1.setText(String.valueOf(viajes1));
                    viaje2.setText(String.valueOf(viajes2));
                    viaje3.setText(String.valueOf(viajes3));
                    Toast.makeText(TaquillaActivity.this, "Recarga Exitosa", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(TaquillaActivity.this, "Error al Recargar", Toast.LENGTH_SHORT).show());
    }



}