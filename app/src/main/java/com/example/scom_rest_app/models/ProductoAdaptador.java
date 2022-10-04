package com.example.scom_rest_app.models;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scom_rest_app.R;

public class ProductoAdaptador extends RecyclerView.Adapter<ProductoAdaptador.ViewHolder> {

    private Producto[] listaProductos;

    public ProductoAdaptador(Producto[] listaProductos) {
        this.listaProductos = listaProductos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_lista_producto,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Producto listaProducto = listaProductos[position];
        holder.nombreProducto.setText(listaProductos[position].getNombre());
        holder.precio.setText("Bs. "+listaProductos[position].getPrecio());
        holder.cantidad.setText("0");
    }

    @Override
    public int getItemCount() {
        return listaProductos.length;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombreProducto;
        private TextView precio;
        private ImageView botonEliminarCarrito;
        private ImageView botonAdicionarCarrito;
        private ImageView imagenPlatillo;
        private ImageView botonDecrementar;
        private ImageView botonIncrementar;
        private TextView cantidad;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.nombreProducto = (TextView)itemView.findViewById(R.id.tv_nombre_producto);
            this.precio = (TextView) itemView.findViewById(R.id.tv_precio);
            this.botonEliminarCarrito = (ImageView) itemView.findViewById(R.id.btn_eliminar_carrito);
            this.botonAdicionarCarrito = (ImageView) itemView.findViewById(R.id.btn_adicionar_carrito);
            this.imagenPlatillo = (ImageView) itemView.findViewById(R.id.iv_imagen_producto);
            this.botonDecrementar = (ImageView) itemView.findViewById(R.id.iv_decrementar);
            this.botonIncrementar = (ImageView) itemView.findViewById(R.id.iv_incrementar);
            this.cantidad = (TextView) itemView.findViewById(R.id.et_cantidad);
            /*INICIALIZANDO LAS FUNCIONES INICIALES*/
            this.botonAdicionarCarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    botonAdicionarCarrito.setVisibility(View.GONE);
                    botonEliminarCarrito.setVisibility(View.VISIBLE);
                    botonIncrementar.setVisibility(View.VISIBLE);
                    cantidad.setVisibility(View.VISIBLE);
                    cantidad.setText("1");
                }
            });
            this.botonEliminarCarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    botonEliminarCarrito.setVisibility(View.GONE);
                    botonAdicionarCarrito.setVisibility(View.VISIBLE);
                    botonIncrementar.setVisibility(View.GONE);
                    botonDecrementar.setVisibility(View.GONE);
                    cantidad.setVisibility(View.GONE);
                    double costo = Double.parseDouble(precio.getText().toString().split(" ")[1])/Integer.parseInt(cantidad.getText().toString());
                    precio.setText(String.valueOf(precio.getText().toString().split(" ")[0]+" "+costo));
                    cantidad.setText("0");
                }
            });
            this.botonIncrementar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cntd = Integer.parseInt(cantidad.getText().toString());
                    cntd++;
                    cantidad.setText(String.valueOf(cntd));
                    /*Incremento del costo dependiendo de la cantidad*/
                    String costo[] = precio.getText().toString().split(" ");
                    precio.setText(costo[0]+" "+String.valueOf(Double.parseDouble(costo[1])+(Double.parseDouble(costo[1])/(cntd-1))));
                    if(cntd==2){
                        botonDecrementar.setVisibility(View.VISIBLE);
                    }
                }
            });
            this.botonDecrementar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cntd = Integer.parseInt(cantidad.getText().toString());
                    cntd--;
                    cantidad.setText(String.valueOf(cntd));
                    /*Decremento del costo dependiendo de la cantidad*/
                    String costo[] = precio.getText().toString().split(" ");
                    precio.setText(costo[0]+" "+String.valueOf(Double.parseDouble(costo[1])-(Double.parseDouble(costo[1])/(cntd+1))));
                    if(cntd == 1){
                        botonDecrementar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

}
