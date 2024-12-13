package com.migu3lone.prueba.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.migu3lone.prueba.R;
import com.migu3lone.prueba.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Ajustar los insets del sistema para el diseño principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el ImageView o cualquier otra vista que quieras animar
        ImageView logoImageView = findViewById(R.id.logoImageView); // Suponiendo que tienes un ImageView con este ID

        // Cargar la animación desde el archivo XML
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.animation);

        // Aplicar la animación a la vista
        logoImageView.startAnimation(fadeIn);

        // Después de la animación, redirigir a la MainActivity
        logoImageView.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza SplashActivity
        }, 2500); // Espera de 2.5 segundos para que termine la animación
    }
}