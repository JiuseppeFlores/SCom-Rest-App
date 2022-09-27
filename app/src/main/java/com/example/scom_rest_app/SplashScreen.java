package com.example.scom_rest_app;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //Animaciones
        Animation animacionArriba = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacionAbajo = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);
        Animation animacionDesvanecer = AnimationUtils.loadAnimation(this,R.anim.desvanecer_centro);

        TextView version = findViewById(R.id.version_app);
        ImageView logo = findViewById(R.id.logo_scom_rest_app);
        ImageView fondo = findViewById(R.id.fondo_splash_screen);
        fondo.setAnimation(animacionDesvanecer);
        version.setAnimation(animacionAbajo);
        logo.setAnimation(animacionArriba);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,InicioSesion.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
