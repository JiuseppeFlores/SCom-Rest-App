package com.example.scom_rest_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scom_rest_app.services.Api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityProducto extends AppCompatActivity {

    TextView tvNombre, tvPrecio, tvTipo, tvGradoAlcoholico, tvIngredientes;
    int idProducto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        Toolbar toolbar = findViewById(R.id.tb_producto);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityProducto.super.onBackPressed();
            }
        });
        idProducto = Integer.parseInt(getIntent().getStringExtra("id"));
        String nombre = getIntent().getStringExtra("nombre");
        String precio = getIntent().getStringExtra("precio");
        String tipo = getIntent().getStringExtra("tipo");

        tvNombre = findViewById(R.id.tv_nombre);
        tvNombre.setText(nombre.toUpperCase());
        tvPrecio = findViewById(R.id.tv_precio);
        tvPrecio.setText("Bs. "+precio);
        tvTipo = findViewById(R.id.tv_tipo);
        tvTipo.setText(tipo.substring(0,1).toUpperCase()+tipo.substring(1));
        tvIngredientes = findViewById(R.id.tv_ingredientes);
        tvGradoAlcoholico = findViewById(R.id.tv_grado_alcoholico);

        cargarDatos();
    }

    public void cargarDatos(){
        Api.getClient().obtenerProducto(this.idProducto, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    JSONObject data = object.getJSONObject("data");
                    Log.d("CARGAR PRODUCTO",data.toString());
                    tvNombre.setText(data.getString("nombre").toUpperCase());
                    tvPrecio.setText("Bs. "+String.valueOf(data.getDouble("precio")));
                    String tipo = data.getString("tipoProducto");
                    tvTipo.setText(tipo.substring(0,1).toUpperCase()+tipo.substring(1).toLowerCase());
                    if(tipo.equals("platillo")){
                        JSONArray ingredientes = data.getJSONArray("ingredientes");
                        String lista = "";
                        for(int i=0 ; i < ingredientes.length() ; i++){
                            String ing = ingredientes.get(i).toString();
                            lista = lista + " - "+(ing.substring(0,1).toUpperCase()+ing.substring(1))+"\n";
                            Log.d("dato",lista);
                        }
                        tvIngredientes.setText(lista);
                        LinearLayout extraPlatillo = findViewById(R.id.extra_platillo);
                        extraPlatillo.setVisibility(View.VISIBLE);
                    }
                    if(tipo.equals("bebida")){
                        tvGradoAlcoholico.setText(data.getString("gradoAlcoholico")+"%");
                        LinearLayout extraBebida = findViewById(R.id.extra_bebida);
                        extraBebida.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    Log.d("CARGAR PRODUCTO","error "+e.getMessage().toString());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("CARGAR PRODUCTO","Error de conexion");
            }
        });
    }
}
