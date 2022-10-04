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

public class PedidoAdaptador extends RecyclerView.Adapter<PedidoAdaptador.ViewHolder> {

    private Pedido[] listaPedidos;

    public PedidoAdaptador(Pedido[] listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_lista_pedido,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pedido listaPedido = listaPedidos[position];
        holder.idPedido.setText(String.valueOf(listaPedidos[position].getIdpedido()));
        holder.fechaPedido.setText(listaPedidos[position].getFecha());
    }

    @Override
    public int getItemCount() {
        return listaPedidos.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView idPedido;
        private TextView fechaPedido;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.idPedido = (TextView)itemView.findViewById(R.id.tv_idpedido);
            this.fechaPedido = (TextView) itemView.findViewById(R.id.tv_fecha_pedido);
        }
    }

}
