package com.example.scom_rest_app;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scom_rest_app.models.Pedido;
import com.example.scom_rest_app.models.Producto;
import com.example.scom_rest_app.models.ProductoAdaptador;
import com.example.scom_rest_app.services.Api;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeCliente extends AppCompatActivity {

    MaterialButton btnRealizarPedido;
    private Producto[] producto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_cliente);

        btnRealizarPedido = findViewById(R.id.btn_realizar_pedido);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = 0;
                for(int i=0;i<producto.length;i++){
                    if(producto[i].getCarrito()){
                        c++;
                    };
                }
                Log.d("PRODUCTOS",""+c);
                Pedido pedido = new Pedido();
            }
        });

        listarProductos();
    }
    public void listarProductos(){
        Api.getClient().obtenerProductos(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    JSONArray data = object.getJSONArray("data");
                    Log.d("LISTA PRODUCTOS",""+data.toString());
                    producto = new Producto[data.length()];
                    for(int i=0;i<data.length();i++){
                        JSONObject productoApi = data.getJSONObject(i);
                        producto[i] = new Producto(
                          productoApi.getInt("idproducto"),//idProducto
                          productoApi.getString("estado"),//estado
                          productoApi.getString("nombre"),//nombre
                          productoApi.getDouble("precio"),//precio
                          productoApi.getString("tipoProducto")//tipoProducto
                        );
                    }

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lista_productos);
                    ProductoAdaptador adapter = new ProductoAdaptador(producto);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeCliente.this));
                    recyclerView.setAdapter(adapter);
                    //recyclerView.setOnClickListener(HomeCliente.this);

                    Log.d("PRUEBA",""+adapter.getItemId(0));

                } catch (Exception e) {
                    Log.d("LISTA PRODUCTOS","Error "+e.getMessage().toString());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(HomeCliente.this, "LISTA PRODUCTOS: Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
