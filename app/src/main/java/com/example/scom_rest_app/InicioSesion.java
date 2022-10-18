package com.example.scom_rest_app;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    TextInputLayout tiLayoutUsuario;
    TextInputLayout tiLayoutPassword;

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

        etUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tiLayoutUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tiLayoutPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tiLayoutUsuario = findViewById(R.id.ti_layout_usuario);
        tiLayoutPassword = findViewById(R.id.ti_layout_password);

        btnIniciarSesion = findViewById(R.id.btn_iniciar_Sesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cpiCargando.setVisibility(View.VISIBLE);
                cambiarEstadoFormulario(View.GONE, View.VISIBLE);
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
                                    //Toast.makeText(InicioSesion.this, "INICIO SESION: "+error.toString(), Toast.LENGTH_SHORT).show();
                                    if(data.length()==0){
                                        //Toast.makeText(InicioSesion.this, "Datos Incorrectos: "+error.toString(), Toast.LENGTH_SHORT).show();
                                        Log.d("INICIAR SESION","Error Data : "+error.toString());
                                        if(error.toString().toLowerCase().indexOf("usuario")!=(-1)){
                                            //Toast.makeText(InicioSesion.this, "Usuario", Toast.LENGTH_SHORT).show();
                                            Log.d("INICIAR SESION",""+error.get(0).toString());
                                            tiLayoutUsuario.setErrorEnabled(true);
                                            tiLayoutUsuario.setError(error.get(0).toString());
                                        }else if(error.toString().toLowerCase().indexOf("contraseña")!=(-1)){
                                            //Toast.makeText(InicioSesion.this, "Contraseña", Toast.LENGTH_SHORT).show();
                                            Log.d("INICIAR SESION",""+error.get(0).toString());
                                            tiLayoutPassword.setErrorEnabled(true);
                                            tiLayoutPassword.setError(error.get(0).toString());
                                        }
                                        /*String passError = null;
                                        toggleTextInputLayoutError(etUsuario,"error");
                                        etUsuario.setError("Error");*/
                                    }else{
                                        //Toast.makeText(InicioSesion.this, "Iniciando Sesion: "+data.toString(), Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = data.getString("tipoUsuario");
                                        //Log.d("INICIO SESION",tipoUsuario);

                                        SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sesion.edit();
                                        editor.putString("user",data.getString("nombreUsuario"));
                                        editor.putString("password",data.getString("contraseña"));
                                        editor.putString("tipo",data.getString("tipoUsuario"));
                                        editor.commit();

                                        switch(tipoUsuario){
                                            case "cliente":
                                                //Toast.makeText(InicioSesion.this, "Tipo de usuario: CLIENTE", Toast.LENGTH_SHORT).show();
                                                Intent cliente = new Intent(InicioSesion.this, HomeCliente.class);
                                                startActivity(cliente);
                                                finish();
                                                break;
                                            case "chef":
                                                //Toast.makeText(InicioSesion.this, "Tipo de usuario: CHEF", Toast.LENGTH_SHORT).show();
                                                Intent chef = new Intent(InicioSesion.this, HomeChef.class);
                                                startActivity(chef);
                                                finish();
                                                break;
                                            case "camarero":
                                                //Toast.makeText(InicioSesion.this, "Tipo de usuario: CAMARERO", Toast.LENGTH_SHORT).show();
                                                Intent camarero = new Intent(InicioSesion.this, HomeCamarero.class);
                                                startActivity(camarero);
                                                finish();
                                                break;
                                        }
                                    }
                                }catch(Exception e){
                                    Log.d("INICIAR SESION","Error: "+e.getMessage().toString());
                                }
                                cambiarEstadoFormulario(View.VISIBLE,View.GONE);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(InicioSesion.this, "INICIO SESION: Error de Conexion "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                cambiarEstadoFormulario(View.VISIBLE,View.GONE);
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

    private void cambiarEstadoFormulario(int card, int progress) {
        CardView layoutBase = findViewById(R.id.layout_base);
        layoutBase.setVisibility(card);
        CircularProgressIndicator cargando = findViewById(R.id.cpi_cargando);
        cargando.setVisibility(progress);
    }
}
