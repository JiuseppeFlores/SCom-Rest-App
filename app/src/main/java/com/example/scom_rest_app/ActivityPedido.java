package com.example.scom_rest_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scom_rest_app.models.Producto;
import com.example.scom_rest_app.models.ProductoAdaptador;
import com.example.scom_rest_app.services.Api;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityPedido extends AppCompatActivity {
    private int idPedido;
    ArrayList<Producto> listaProductos;
    MaterialButton btnConfirmarPedido, btnCancelarPedido;
    TextView tvSinProducto;
    Reiniciar reiniciar;
    public interface Reiniciar{
        public void reiniciarListado();
    }
    public void setInterface(ActivityPedido.Reiniciar reiniciar){
        this.reiniciar = reiniciar;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        Toolbar toolbar = findViewById(R.id.tb_producto);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityPedido.super.onBackPressed();
            }
        });
        this.idPedido = Integer.parseInt(getIntent().getStringExtra("idPedido"));
        listaProductos = new ArrayList<Producto>();
        btnConfirmarPedido = findViewById(R.id.btn_confirmar_pedido);
        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityPedido.this);
                alerta.setMessage("¿Desea confirmar el pedido actual?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                                String ci = sesion.getString("ci","0");
                                Api.getClient().confirmarPedido(idPedido, Integer.parseInt(ci), new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response response2) {
                                        BufferedReader reader = null;
                                        String result = "";
                                        try {
                                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                            result = reader.readLine();
                                            JSONObject object = new JSONObject(result.trim());
                                            Log.d("CONFIRMAR PEDIDO",object.toString());
                                            if(object.getBoolean("response")){
                                                //reiniciar.reiniciarListado();
                                                Toast.makeText(ActivityPedido.this, "Pedido Confirmado correctamente.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }catch(Exception e){
                                            Log.d("CONFIRMAR PEDIDO","Error "+e.getMessage().toString());
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.d("CONFIRMAR PEDIDO","Error de conexion");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog mensaje = alerta.create();
                mensaje.setTitle("SCom-Rest-App");
                mensaje.show();
            }
        });
        btnCancelarPedido = findViewById(R.id.btn_cancelar_pedido);
        btnCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityPedido.this);
                alerta.setMessage("¿Desea cancelar el pedido actual?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                                String ci = sesion.getString("ci","0");
                                Api.getClient().cancelarPedido(idPedido, Integer.parseInt(ci), new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response response2) {
                                        BufferedReader reader = null;
                                        String result = "";
                                        try {
                                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                            result = reader.readLine();
                                            JSONObject object = new JSONObject(result.trim());
                                            Log.d("CANCELAR PEDIDO",object.toString());
                                            if(object.getBoolean("response")){
                                                //reiniciar.reiniciarListado();
                                                Toast.makeText(ActivityPedido.this, "Pedido Cancelado Correctamente", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }catch(Exception e){
                                            Log.d("CANCELAR PEDIDO","Error "+e.getMessage().toString());
                                        }
                                    }
                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.d("CANCELAR PEDIDO","Error de conexcion");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog mensaje = alerta.create();
                mensaje.setTitle("SCom-Rest-App");
                mensaje.show();
            }
        });
        tvSinProducto = findViewById(R.id.tv_sin_productos);
        listarProductosPedido();
    }
    public void listarProductosPedido(){
        Api.getClient().obtenerPedido(this.idPedido, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    JSONArray data = object.getJSONArray("productos");
                    Log.d("PRODUCTO PEDIDO",object.toString());
                    for(int i=0;i<data.length();i++){
                        JSONObject productoApi = data.getJSONObject(i);
                        Producto producto = new Producto(
                                productoApi.getInt("idproducto"),//idProducto
                                productoApi.getString("estado"),//estado
                                productoApi.getString("nombre"),//nombre
                                productoApi.getDouble("precio"),//precio
                                productoApi.getString("tipoProducto"),//tipoProducto
                                //productoApi.getString("imagen")
                                "img1"
                        );
                        producto.setCantidad(productoApi.getInt("cantidad"));
                        producto.setCarrito(true);
                        listaProductos.add(producto);
                    }
                    /* LISTA DE PRODUCTOS */
                    RecyclerView recyclerViewProductos = (RecyclerView) findViewById(R.id.lista_platillos_pedido);
                    ProductoAdaptador adapterProductos = new ProductoAdaptador(ActivityPedido.this,listaProductos);
                    adapterProductos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Producto producto = listaProductos.get(recyclerViewProductos.getChildAdapterPosition(view));
                            //Toast.makeText(HomeCliente.this, ""+producto.getNombre(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityPedido.this,ActivityProducto.class);
                            intent.putExtra("id",String.valueOf(producto.getIdProducto()));
                            intent.putExtra("nombre",producto.getNombre());
                            intent.putExtra("precio",String.valueOf(producto.getPrecio()));
                            intent.putExtra("tipo",producto.getTipoProducto());
                            startActivity(intent);
                        }
                    });
                    CircularProgressIndicator cpiProductos = findViewById(R.id.cpi_cargando_pedido_producto);
                    cpiProductos.setVisibility(View.GONE);
                    if(listaProductos.size()==0){
                        tvSinProducto.setVisibility(View.VISIBLE);
                        btnConfirmarPedido.setVisibility(View.GONE);
                        btnCancelarPedido.setVisibility(View.VISIBLE);
                    }else{
                        recyclerViewProductos.setHasFixedSize(true);
                        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(ActivityPedido.this));
                        recyclerViewProductos.setAdapter(adapterProductos);
                        recyclerViewProductos.setVisibility(View.VISIBLE);
                        btnConfirmarPedido.setVisibility(View.VISIBLE);
                        btnCancelarPedido.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    Log.d("PRODUCTO PEDIDO","Error "+e.getMessage().toString());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("PRODUCTOS PEDIDO","Error de conexion");
            }
        });
    }
}