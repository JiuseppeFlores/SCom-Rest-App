package com.example.scom_rest_app.models;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scom_rest_app.AdaptadorListaProductosListener;
import com.example.scom_rest_app.HomeCliente;
import com.example.scom_rest_app.R;
import com.example.scom_rest_app.databinding.HomeClienteBinding;
import com.google.android.material.card.MaterialCardView;

public class ProductoAdaptador extends RecyclerView.Adapter<ProductoAdaptador.ViewHolder> {

    private Producto[] listaProductos;
    /*private AdaptadorListaProductosListener listener;*/

    public ProductoAdaptador(Producto[] listaProductos) {
        this.listaProductos = listaProductos;
    }
/*
    public void setListener(AdaptadorListaProductosListener listener){
        this.listener = listener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_lista_producto,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Producto listaProducto = listaProductos[position];
        holder.nombreProducto.setText(listaProductos[position].getNombre()+"\n"+"Bs. "+listaProductos[position].getPrecio());
        holder.precio.setText("Total:Bs. "+listaProductos[position].getPrecio());
        holder.cantidad.setText("0");

        holder.botonAdicionarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.botonAdicionarCarrito.setVisibility(View.GONE);
                holder.botonEliminarCarrito.setVisibility(View.VISIBLE);
                holder.botonIncrementar.setVisibility(View.VISIBLE);
                holder.cantidad.setVisibility(View.VISIBLE);
                holder.cantidad.setText("1");
                holder.precio.setVisibility(View.VISIBLE);
                listaProductos[position].setCarrito(true);
                //holder.cvItemProducto.setBackgroundColor(R.color.fourth);
            }
        });
        holder.botonEliminarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.botonEliminarCarrito.setVisibility(View.GONE);
                holder.botonAdicionarCarrito.setVisibility(View.VISIBLE);
                holder.botonIncrementar.setVisibility(View.GONE);
                holder.botonDecrementar.setVisibility(View.GONE);
                holder.cantidad.setVisibility(View.GONE);
                double costo = Double.parseDouble(holder.precio.getText().toString().split(" ")[1])/Integer.parseInt(holder.cantidad.getText().toString());
                holder.precio.setText(String.valueOf(holder.precio.getText().toString().split(" ")[0]+" "+costo));
                holder.cantidad.setText("0");
                holder.precio.setVisibility(View.GONE);
                listaProductos[position].setCarrito(false);
            }
        });
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
        private MaterialCardView cvItemProducto;

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
            this.cvItemProducto = (MaterialCardView) itemView.findViewById(R.id.cv_item_producto);

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
