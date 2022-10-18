package com.example.scom_rest_app;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

                /* VERIFICANDO UN INICIO DE SESION ANTERIOR*/
                SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                String user = sesion.getString("user","");
                String password = sesion.getString("password","");
                String tipo = sesion.getString("tipo","");

                if(user.equals("") && password.equals("") && tipo.equals("")){
                    Intent intent = new Intent(SplashScreen.this,InicioSesion.class);
                    startActivity(intent);
                    finish();
                }else{
                    switch(tipo){
                        case "cliente":
                            Toast.makeText(SplashScreen.this, "Tipo de usuario: CLIENTE", Toast.LENGTH_SHORT).show();
                            Intent cliente = new Intent(SplashScreen.this, HomeCliente.class);
                            startActivity(cliente);
                            break;
                        case "chef":
                            Toast.makeText(SplashScreen.this, "Tipo de usuario: CHEF", Toast.LENGTH_SHORT).show();
                            Intent chef = new Intent(SplashScreen.this, HomeChef.class);
                            startActivity(chef);
                            break;
                        case "camarero":
                            Toast.makeText(SplashScreen.this, "Tipo de usuario: CAMARERO", Toast.LENGTH_SHORT).show();
                            Intent camarero = new Intent(SplashScreen.this, HomeCamarero.class);
                            startActivity(camarero);
                            break;
                    }
                    finish();
                }
            }
        },4000);
    }
}
