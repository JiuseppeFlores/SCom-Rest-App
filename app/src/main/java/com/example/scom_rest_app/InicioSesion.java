package com.example.scom_rest_app;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.scom_rest_app.services.Api;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InicioSesion extends AppCompatActivity {

    CircularProgressIndicator cpiCargando;

    TextInputEditText etUsuario;
    TextInputEditText etPassword;

    MaterialButton btnIniciarSesion;
    MaterialButton btnIniciarInvitado;
    MaterialButton btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        cpiCargando = findViewById(R.id.cpi_cargando);

        etUsuario = findViewById(R.id.et_user);
        etPassword = findViewById(R.id.et_password);

        btnIniciarSesion = findViewById(R.id.btn_iniciar_Sesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cpiCargando.setVisibility(View.VISIBLE);
                Api.getClient().iniciarSesion(
                        etUsuario.getText().toString(),
                        etPassword.getText().toString(),
                        new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                BufferedReader reader = null;
                                String result = "";
                                try{
                                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                    result = reader.readLine();
                                    JSONObject object = new JSONObject(result.trim());
                                    JSONObject data = object.getJSONObject("data");
                                    JSONArray error = object.getJSONArray("error");
                                    Toast.makeText(InicioSesion.this, "INICIO SESION: "+object.toString(), Toast.LENGTH_SHORT).show();
                                    if(data.length()==0){
                                        Toast.makeText(InicioSesion.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                                        /*String passError = null;
                                        toggleTextInputLayoutError(etUsuario,"error");
                                        etUsuario.setError("Error");*/
                                    }else{
                                        Toast.makeText(InicioSesion.this, "Iniciando Sesion: "+data.toString(), Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = data.getString("tipoUsuario");
                                        Log.d("INICIO SESION",tipoUsuario);
                                        switch(tipoUsuario){
                                            case "cliente":
                                                Toast.makeText(InicioSesion.this, "Tipo de usuario: CLIENTE", Toast.LENGTH_SHORT).show();
                                                Intent cliente = new Intent(InicioSesion.this, HomeCliente.class);
                                                startActivity(cliente);
                                                break;
                                            case "chef":
                                                Toast.makeText(InicioSesion.this, "Tipo de usuario: CHEF", Toast.LENGTH_SHORT).show();
                                                Intent chef = new Intent(InicioSesion.this, HomeChef.class);
                                                startActivity(chef);
                                                break;
                                            case "camarero":
                                                Toast.makeText(InicioSesion.this, "Tipo de usuario: CAMARERO", Toast.LENGTH_SHORT).show();
                                                Intent camarero = new Intent(InicioSesion.this, HomeCamarero.class);
                                                startActivity(camarero);
                                                break;
                                        }
                                    }
                                }catch(Exception e){
                                    Log.d("INICIAR SESION","Error: "+e.getMessage().toString());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(InicioSesion.this, "INICIO SESION: Error de Conexion "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
        btnIniciarInvitado = findViewById(R.id.btn_iniciar_invitado);
        btnIniciarInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InicioSesion.this, "Modo Invitado", Toast.LENGTH_SHORT).show();
            }
        });
        btnCrearCuenta = findViewById(R.id.btn_crear_cuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InicioSesion.this, "Crear Cuenta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        textInputLayout.setErrorEnabled(msg != null);
    }
}
