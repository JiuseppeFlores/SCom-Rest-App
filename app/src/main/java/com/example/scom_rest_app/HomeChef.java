package com.example.scom_rest_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scom_rest_app.models.Pedido;
import com.example.scom_rest_app.models.PedidoAdaptador;
import com.example.scom_rest_app.models.Producto;
import com.example.scom_rest_app.models.ProductoAdaptador;
import com.example.scom_rest_app.services.Api;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeChef extends AppCompatActivity {

    private ArrayList<Pedido> listaPedidos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_chef);
        Toolbar toolbar = findViewById(R.id.tb_chef);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeChef.super.onBackPressed();
            }
        });
        listaPedidos = new ArrayList<Pedido>();

        listarPedidos();
    }

    public void listarPedidos(){
        /*Api.getClient().obtenerPedidos(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    listaPedidos.clear();
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    JSONArray data = object.getJSONArray("data");
                    for(int i=0;i<data.length();i++){
                        JSONObject pedidoApi = data.getJSONObject(i);
                        String estadoPedido = pedidoApi.getString("estado");
                        Pedido pedido = new Pedido(
                                pedidoApi.getInt("idpedido"),
                                pedidoApi.getString("estado"),
                                pedidoApi.getString("fecha"),
                                pedidoApi.getInt("idMesa")
                        );
                        switch(estadoPedido){
                            case "habilitado":
                                listaPedidos.add(pedido);
                                break;
                        }
                    }
                    // LISTA DE PEDIDOS NUEVOS
                    RecyclerView recyclerViewPedido = (RecyclerView) findViewById(R.id.lista_pedidos_hebilitados);
                    PedidoAdaptador adapterNuevos = new PedidoAdaptador(HomeChef.this,listaPedidos);
                    adapterNuevos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(HomeChef.this, "Ver pedidos habilitados", Toast.LENGTH_SHORT).show();
                            //Pedido pedido = listaNuevos.get(recyclerViewNuevos.getChildAdapterPosition(view));
                            //Intent intent = new Intent(HomeCamarero.this,ActivityPedido.class);
                            //intent.putExtra("idPedido",String.valueOf(pedido.getIdPedido()));
                            //startActivity(intent);
                        }
                    });
                    recyclerViewNuevos.setHasFixedSize(true);
                    recyclerViewNuevos.setLayoutManager(new LinearLayoutManager(HomeCamarero.this));
                    recyclerViewNuevos.setAdapter(adapterNuevos);
                    CircularProgressIndicator cpiNuevos = findViewById(R.id.cpi_cargando_nuevos);;
                    cpiNuevos.setVisibility(View.GONE);
                    recyclerViewNuevos.setVisibility(View.VISIBLE);

                    // LISTA DE PEDIDOS POR ENTREGAR
                    RecyclerView recyclerViewEntregar = (RecyclerView) findViewById(R.id.lista_pedidos_entregar);
                    PedidoAdaptador adapterEntregar = new PedidoAdaptador(HomeCamarero.this,listaEntregar);
                    adapterEntregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Pedido pedido = listaEntregar.get(recyclerViewEntregar.getChildAdapterPosition(view));
                            //Toast.makeText(HomeCamarero.this, "Ver pedido por entregar", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder alerta = new AlertDialog.Builder(HomeCamarero.this);
                            alerta.setMessage("¿Desea entregar el pedido?")
                                    .setCancelable(false)
                                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                                            String ci = sesion.getString("ci","0");
                                            Api.getClient().entregarPedido(pedido.getIdPedido(), Integer.parseInt(ci), new Callback<Response>() {
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
                                                            Toast.makeText(HomeCamarero.this, "Pedido Entregado correctamente.", Toast.LENGTH_SHORT).show();
                                                            actualizarPedidos();
                                                        }
                                                    }catch(Exception e){
                                                        Log.d("ENTREGAR PEDIDO","Error "+e.getMessage().toString());
                                                    }
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    Log.d("ENTREGAR PEDIDO","Error de conexion");
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
                    recyclerViewEntregar.setHasFixedSize(true);
                    recyclerViewEntregar.setLayoutManager(new LinearLayoutManager(HomeCamarero.this));
                    recyclerViewEntregar.setAdapter(adapterEntregar);
                    CircularProgressIndicator cpiEntregar = findViewById(R.id.cpi_cargando_entregar);
                    cpiEntregar.setVisibility(View.GONE);
                    recyclerViewEntregar.setVisibility(View.VISIBLE);
                    btnActualizarPedidos.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.d("LISTA PRODUCTOS","Error "+e.getMessage().toString());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(HomeCamarero.this, "LISTA PRODUCTOS: Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sesion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mn_salir:
                cerrarSesion();
                break;
            case R.id.mn_actualizar:
                //actualizarProductos();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void cerrarSesion(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(HomeChef.this);
        alerta.setMessage("¿Desea cerrar la sesión actual?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(HomeChef.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                        SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sesion.edit();
                        editor.putString("user","");
                        editor.putString("password","");
                        editor.putString("tipo","");
                        editor.putString("ci","");
                        editor.commit();
                        Intent intent = new Intent(HomeChef.this,SplashScreen.class);
                        startActivity(intent);
                        finish();
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
}
