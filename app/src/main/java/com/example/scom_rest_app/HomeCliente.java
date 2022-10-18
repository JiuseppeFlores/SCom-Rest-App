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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.scom_rest_app.Fragments.DialogoFragmentPedido;
import com.example.scom_rest_app.Fragments.FragmentBebidas;
import com.example.scom_rest_app.Fragments.FragmentPlatillos;
import com.example.scom_rest_app.models.PagerController;
import com.example.scom_rest_app.models.Pedido;
import com.example.scom_rest_app.models.Producto;
import com.example.scom_rest_app.models.ProductoAdaptador;
import com.example.scom_rest_app.services.Api;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeCliente extends AppCompatActivity implements DialogoFragmentPedido.Enviar{

    MaterialButton btnRealizarPedido;

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerController pageAdapter;

    private Pedido pedido;

    private ArrayList<Producto> listaPlatillos;
    private ArrayList<Producto> listaBebidas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_cliente);
        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeCliente.super.onBackPressed();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.setupWithViewPager(viewPager);

        pageAdapter = new PagerController(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pageAdapter.addFragment(new FragmentPlatillos(),"PLATILLOS");
        pageAdapter.addFragment(new FragmentBebidas(),"BEBIDAS");

        viewPager.setAdapter(pageAdapter);

        listaPlatillos = new ArrayList<Producto>();
        listaBebidas = new ArrayList<Producto>();

        btnRealizarPedido = findViewById(R.id.btn_realizar_pedido);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String[]> detallePedidoPlatillo = new ArrayList<String[]>();
                for(int i=0 ; i < listaPlatillos.size() ; i++){
                    if(listaPlatillos.get(i).getCarrito()){
                        int idProducto = listaPlatillos.get(i).getIdProducto();
                        String nombre = listaPlatillos.get(i).getNombre();
                        int cantidad = listaPlatillos.get(i).getCantidad();
                        double total = cantidad * listaPlatillos.get(i).getPrecio();
                        String[] detalle = {
                                nombre,
                                String.valueOf(cantidad),
                                String.valueOf(total)
                        };
                        detallePedidoPlatillo.add(detalle);
                    };
                }

                ArrayList<String[]> detallePedidoBebida = new ArrayList<String[]>();

                for(int i=0 ; i < listaBebidas.size() ; i++){
                    if(listaBebidas.get(i).getCarrito()){
                        int idProducto = listaBebidas.get(i).getIdProducto();
                        String nombre = listaBebidas.get(i).getNombre();
                        int cantidad = listaBebidas.get(i).getCantidad();
                        double total = cantidad * listaBebidas.get(i).getPrecio();
                        String[] detalle = {
                                nombre,
                                String.valueOf(cantidad),
                                String.valueOf(total)
                        };
                        detallePedidoBebida.add(detalle);
                    };
                }

                if(detallePedidoPlatillo.size() == 0 && detallePedidoBebida.size() == 0){
                    Toast.makeText(HomeCliente.this, "No selecciono ningún producto", Toast.LENGTH_SHORT).show();
                }else{
                    DialogoFragmentPedido dialogoFragmentPedido = new DialogoFragmentPedido(detallePedidoPlatillo,detallePedidoBebida);
                    dialogoFragmentPedido.setInterface(new DialogoFragmentPedido.Enviar() {
                        @Override
                        public void enviarPedido(String mesa) {
                            Toast.makeText(HomeCliente.this, ""+mesa, Toast.LENGTH_SHORT).show();
                            reiniciarListado(true);
                            /* PROCEDIMIENTO PARA REALIZAR EL PEDIDO */
                            Map<String,Integer> productos = new HashMap<String,Integer>();
                            for(int i=0;i<listaPlatillos.size();i++){
                                if(listaPlatillos.get(i).getCarrito()){
                                    int idProducto = listaPlatillos.get(i).getIdProducto();
                                    int cantidad = listaPlatillos.get(i).getCantidad();
                                    productos.put(String.valueOf(idProducto),cantidad);
                                };
                            }

                            for(int i=0;i<listaBebidas.size();i++){
                                if(listaBebidas.get(i).getCarrito()){
                                    int idProducto = listaBebidas.get(i).getIdProducto();
                                    int cantidad = listaBebidas.get(i).getCantidad();
                                    productos.put(String.valueOf(idProducto),cantidad);
                                };
                            }

                            Date date = new Date();
                            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
                            Pedido pedido = new Pedido("espera",fecha.format(date));

                            JSONObject prod = new JSONObject(productos);
                            Log.d("PRODUCTOS",prod.toString());
                            Api.getClient().realizarPedido(
                                    pedido.getEstado(),
                                    pedido.getFecha(),
                                    prod.toString(),
                                    new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response response2) {
                                            BufferedReader reader = null;
                                            String result = "";
                                            try {
                                                reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                                                result = reader.readLine();
                                                JSONObject object = new JSONObject(result.trim());
                                                int id = object.getInt("data");
                                                pedido.setIdpedido(id);
                                                Log.d("PEDIDO",String.valueOf(pedido.getIdpedido()));
                                                btnRealizarPedido.setEnabled(true);
                                                Toast.makeText(HomeCliente.this, "Pedido Realizado con Éxito!, espere a nuestro camarero por favor", Toast.LENGTH_SHORT).show();
                                            }catch(Exception e){
                                                e.printStackTrace();
                                                Log.d("REALIZAR PEDIDO",""+e.getMessage().toString());
                                                reiniciarListado(false);
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.d("REALIZAR PEDIDO","Error de Conexion");
                                            Log.d("REALIZAR PEDIDO",""+error.getMessage().toString());
                                            reiniciarListado(false);
                                        }
                                    });
                            listarProductos();
                        }
                    });
                    dialogoFragmentPedido.show(getSupportFragmentManager(),"REALIZAR PEDIDO");
                }
            }
        });

        listarProductos();
    }
    public void listarProductos(){
        Api.getClient().obtenerProductosHabilitados(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    JSONArray data = object.getJSONArray("data");
                    for(int i=0;i<data.length();i++){
                        JSONObject productoApi = data.getJSONObject(i);
                        String tipoProducto = productoApi.getString("tipoProducto");
                        if(tipoProducto.equals("platillo")){
                            Producto producto = new Producto(
                                    productoApi.getInt("idproducto"),//idProducto
                                    productoApi.getString("estado"),//estado
                                    productoApi.getString("nombre"),//nombre
                                    productoApi.getDouble("precio"),//precio
                                    productoApi.getString("tipoProducto"),//tipoProducto
                                    //productoApi.getString("imagen")
                                    "img1"
                            );
                            //Log.d("PRODUCTO",""+producto.getImagen());
                            listaPlatillos.add(producto);
                        }
                        if(tipoProducto.equals("bebida")){
                            Producto producto = new Producto(
                                    productoApi.getInt("idproducto"),//idProducto
                                    productoApi.getString("estado"),//estado
                                    productoApi.getString("nombre"),//nombre
                                    productoApi.getDouble("precio"),//precio
                                    productoApi.getString("tipoProducto"),//tipoProducto
                                    //productoApi.getString("imagen")
                                    "img1"
                            );
                            listaBebidas.add(producto);
                        }
                    }
                    /* LISTA DE PLATILLOS */
                    RecyclerView recyclerViewPlatillos = (RecyclerView) findViewById(R.id.lista_platillos);
                    ProductoAdaptador adapterPlatillos = new ProductoAdaptador(HomeCliente.this,listaPlatillos);
                    adapterPlatillos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Producto producto = listaPlatillos.get(recyclerViewPlatillos.getChildAdapterPosition(view));
                            //Toast.makeText(HomeCliente.this, ""+producto.getNombre(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeCliente.this,ActivityProducto.class);
                            intent.putExtra("id",String.valueOf(producto.getIdProducto()));
                            intent.putExtra("nombre",producto.getNombre());
                            intent.putExtra("precio",String.valueOf(producto.getPrecio()));
                            intent.putExtra("tipo",producto.getTipoProducto());
                            startActivity(intent);
                        }
                    });
                    recyclerViewPlatillos.setHasFixedSize(true);
                    recyclerViewPlatillos.setLayoutManager(new LinearLayoutManager(HomeCliente.this));
                    recyclerViewPlatillos.setAdapter(adapterPlatillos);
                    CircularProgressIndicator cpiPlatillos = findViewById(R.id.cpi_cargando_platillos);;
                    cpiPlatillos.setVisibility(View.GONE);
                    recyclerViewPlatillos.setVisibility(View.VISIBLE);

                    /* LISTA DE BEBIDAS */
                    RecyclerView recyclerViewBebidas = (RecyclerView) findViewById(R.id.lista_bebidas);
                    ProductoAdaptador adapterBebidas = new ProductoAdaptador(HomeCliente.this,listaBebidas);
                    adapterBebidas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Producto producto = listaBebidas.get(recyclerViewBebidas.getChildAdapterPosition(view));
                            //Toast.makeText(HomeCliente.this, ""+listaBebidas.get(recyclerViewBebidas.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeCliente.this,ActivityProducto.class);
                            intent.putExtra("id",String.valueOf(producto.getIdProducto()));
                            intent.putExtra("nombre",producto.getNombre());
                            intent.putExtra("precio",String.valueOf(producto.getPrecio()));
                            intent.putExtra("tipo",producto.getTipoProducto());
                            startActivity(intent);
                        }
                    });
                    recyclerViewBebidas.setHasFixedSize(true);
                    recyclerViewBebidas.setLayoutManager(new LinearLayoutManager(HomeCliente.this));
                    recyclerViewBebidas.setAdapter(adapterBebidas);
                    CircularProgressIndicator cpiBebidas = findViewById(R.id.cpi_cargando_bebidas);
                    cpiBebidas.setVisibility(View.GONE);
                    recyclerViewBebidas.setVisibility(View.VISIBLE);
                    btnRealizarPedido.setVisibility(View.VISIBLE);
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
                actualizarProductos();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cerrarSesion(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(HomeCliente.this);
        alerta.setMessage("¿Desea cerrar la sesión actual?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(HomeCliente.this, "Cerrando Sesion", Toast.LENGTH_SHORT).show();
                        SharedPreferences sesion = getSharedPreferences("SComRestApp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sesion.edit();
                        editor.putString("user","");
                        editor.putString("password","");
                        editor.putString("tipo","");
                        editor.commit();
                        Intent intent = new Intent(HomeCliente.this,SplashScreen.class);
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
    public void actualizarProductos(){
        reiniciarListado(true);
        listarProductos();
    }
    @Override
    public void enviarPedido(String mesa){
        Toast.makeText(this, "Mesa: "+mesa, Toast.LENGTH_SHORT).show();
    }
    public void reiniciarListado(boolean sw){
        RecyclerView recyclerViewPlatillos = (RecyclerView) findViewById(R.id.lista_platillos);
        RecyclerView recyclerViewBebidas = (RecyclerView) findViewById(R.id.lista_bebidas);
        CircularProgressIndicator cpiPlatillos = findViewById(R.id.cpi_cargando_platillos);
        CircularProgressIndicator cpiBebidas = findViewById(R.id.cpi_cargando_bebidas);
        if(sw){
            recyclerViewPlatillos.setVisibility(View.GONE);
            recyclerViewBebidas.setVisibility(View.GONE);
            cpiPlatillos.setVisibility(View.VISIBLE);
            cpiBebidas.setVisibility(View.VISIBLE);
            btnRealizarPedido.setVisibility(View.GONE);
        }else{
            recyclerViewPlatillos.setVisibility(View.VISIBLE);
            recyclerViewBebidas.setVisibility(View.VISIBLE);
            cpiPlatillos.setVisibility(View.GONE);
            cpiBebidas.setVisibility(View.GONE);
            btnRealizarPedido.setVisibility(View.VISIBLE);
        }
    }
}
