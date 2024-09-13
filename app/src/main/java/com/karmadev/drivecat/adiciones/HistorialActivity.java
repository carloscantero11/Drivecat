package com.karmadev.drivecat.adiciones;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karmadev.drivecat.R;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistorialActivity extends AppCompatActivity {

    TextView dia, semana, mes;
    FirebaseFirestore data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        dia = findViewById(R.id.dia);
        semana = findViewById(R.id.semana);
        mes = findViewById(R.id.mes);

        data = FirebaseFirestore.getInstance();

        // Recuperar registros de Firebase
        data.collection("Movimientos").document().collection("Registros")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Obtener la fecha actual
                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();

                    // Inicializar contadores para día, semana y mes
                    int countToday = 0;
                    int countThisWeek = 0;
                    int countThisMonth = 0;

                    // Iterar sobre los registros
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Obtener la fecha del registro
                        Date recordDate = document.getDate("Fecha");

                        // Calcular la diferencia de tiempo en días entre la fecha actual y la fecha del registro
                        long diffInMillis = currentDate.getTime() - recordDate.getTime();
                        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                        // Clasificar el registro en función de la diferencia de tiempo
                        if (diffInDays == 0) {
                            // Registro del día actual
                            countToday++;
                        } else if (diffInDays <= 7) {
                            // Registro de la semana actual
                            countThisWeek++;
                        } else if (isSameMonth(calendar, recordDate)) {
                            // Registro del mes actual
                            countThisMonth++;
                        }
                    }

                    // Mostrar los conteos en los TextView correspondientes
                    dia.setText(String.valueOf(countToday));
                    semana.setText(String.valueOf(countThisWeek));
                    mes.setText(String.valueOf(countThisMonth));
                })
                .addOnFailureListener(e -> {
                    // Manejar errores
                });
    }

    // Método para verificar si dos fechas están en el mismo mes
    private boolean isSameMonth(Calendar calendar, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                && calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR);
    }
}
