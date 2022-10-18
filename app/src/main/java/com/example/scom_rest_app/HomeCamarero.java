package com.example.scom_rest_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scom_rest_app.models.Pedido;
import com.example.scom_rest_app.models.PedidoAdaptador;
import com.example.scom_rest_app.models.Producto;
import com.example.scom_rest_app.models.ProductoAdaptador;
import com.example.scom_rest_app.services.Api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeCamarero extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_camarero);

        listarPedidos();
    }
    public void listarPedidos(){
        Api.getClient().obtenerPedidos(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    JSONArray data = object.getJSONArray("data");
                    Log.d("LISTA PEDIDOS",""+data.toString());
                    Pedido[] pedido = new Pedido[data.length()];
                    for(int i=0;i<data.length();i++){
                        JSONObject pedidoApi = data.getJSONObject(i);
                        pedido[i] = new Pedido(
                                //pedidoApi.getInt("ciCamarero"),//idProducto
                                //pedidoApi.getInt("ciChef"),//estado
                                //pedidoApi.getInt("codfactura"),//nombre
                                pedidoApi.getString("estado"),//precio
                                pedidoApi.getString("fecha")/*,//tipoProducto
                                pedidoApi.getInt("idpedido")*/
                        );
                    }

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lista_pedidos);
                    PedidoAdaptador adapter = new PedidoAdaptador(pedido);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeCamarero.this));
                    recyclerView.setAdapter(adapter);

                    Log.d("PRUEBA",""+adapter.getItemId(0));

                } catch (Exception e) {
                    Log.d("LISTA PRODUCTOS","Error "+e.getMessage().toString());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(HomeCamarero.this, "LISTA PRODUCTOS: Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
