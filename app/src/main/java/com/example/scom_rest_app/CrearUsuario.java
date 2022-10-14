package com.example.scom_rest_app;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scom_rest_app.services.Api;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
public class CrearUsuario extends AppCompatActivity {
    CircularProgressIndicator cpiCargando;
    TextInputEditText etCI;
    TextInputEditText etUsuario;
    TextInputEditText etPassword;
    TextInputEditText etEmail;
    TextInputEditText etNombre;
    TextInputEditText etApellidoPat;
    TextInputEditText etApellidoMat;
    TextInputEditText etFechaNaci;
    TextInputEditText etNIT;

    MaterialButton btnCrearCuenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
        cpiCargando = findViewById(R.id.cpi_cargando);
        btnCrearCuenta = findViewById(R.id.btn_Crear_Cuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api.getClient().crearCuenta(
                        etCI.getText().toString(),
                        etUsuario.getText().toString(),
                        etPassword.getText().toString(),
                        etEmail.getText().toString(),
                        etNombre.getText().toString(),
                        etApellidoPat.getText().toString(),
                        etApellidoMat.getText().toString(),
                        etFechaNaci.getText().toString(),
                        etNIT.getText().toString(),
                        "habilitado",
                        new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                BufferedReader reader = null;
                                String result = "";
                                try {
                                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                    result = reader.readLine();
                                    JSONObject object = new JSONObject(result.trim());
                                    JSONObject data = object.getJSONObject("data");
                                    JSONArray error = object.getJSONArray("error");
                                    Toast.makeText(CrearUsuario.this, "CREAR USUARIO: " + object.toString(), Toast.LENGTH_SHORT).show();
                                    if (data.length() == 0) {
                                        Toast.makeText(CrearUsuario.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                                        /*String passError = null;
                                        toggleTextInputLayoutError(etUsuario,"error");
                                        etUsuario.setError("Error");*/
                                    } else {
                                        Toast.makeText(CrearUsuario.this, "Iniciando Sesion: " + data.toString(), Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = data.getString("tipoUsuario");
                                        Log.d("INICIO SESION", tipoUsuario);
                                        switch (tipoUsuario) {
                                            case "cliente":
                                                Toast.makeText(CrearUsuario.this, "Tipo de usuario: CLIENTE", Toast.LENGTH_SHORT).show();
                                                Intent cliente = new Intent(CrearUsuario.this, HomeCliente.class);
                                                startActivity(cliente);
                                                break;
                                            case "chef":
                                                Toast.makeText(CrearUsuario.this, "Tipo de usuario: CHEF", Toast.LENGTH_SHORT).show();
                                                Intent chef = new Intent(CrearUsuario.this, HomeChef.class);
                                                startActivity(chef);
                                                break;
                                            case "camarero":
                                                Toast.makeText(CrearUsuario.this, "Tipo de usuario: CAMARERO", Toast.LENGTH_SHORT).show();
                                                Intent camarero = new Intent(CrearUsuario.this, HomeCamarero.class);
                                                startActivity(camarero);
                                                break;
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d("INICIAR SESION", "Error: " + e.getMessage().toString());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(CrearUsuario.this, "CREAR USUARIO: Error de Conexion " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }
}



