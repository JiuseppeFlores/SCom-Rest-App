package com.example.scom_rest_app.models;

import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PedidoAdaptador extends RecyclerView.Adapter<PedidoAdaptador.ViewHolder> implements View.OnClickListener{

    private ArrayList<Pedido> listaPedidos;
    private Context context;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public PedidoAdaptador(Context context,ArrayList<Pedido> listaPedidos) {
        this.context = context;
        this.listaPedidos = listaPedidos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_lista_pedido,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        listItem.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pedido listaPedido = listaPedidos.get(position);
        holder.idPedido.setText("Pedido: "+String.valueOf(listaPedido.getIdPedido()));
        holder.fechaPedido.setText("Fecha: "+listaPedido.getFecha());
        holder.nroMesa.setText(String.valueOf(listaPedido.getIdMesa()));
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView idPedido;
        private TextView fechaPedido;
        private TextView nroMesa;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.idPedido = (TextView)itemView.findViewById(R.id.tv_idpedido);
            this.fechaPedido = (TextView) itemView.findViewById(R.id.tv_fecha_pedido);
            this.nroMesa = (TextView) itemView.findViewById(R.id.tv_nro_mesa);
        }
    }

}
