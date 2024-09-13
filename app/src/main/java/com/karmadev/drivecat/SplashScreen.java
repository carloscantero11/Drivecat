package com.karmadev.drivecat;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Este método se ejecutará una vez que el temporizador haya finalizado
                // Aquí puedes iniciar tu actividad principal
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                // Cerrar esta actividad
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}