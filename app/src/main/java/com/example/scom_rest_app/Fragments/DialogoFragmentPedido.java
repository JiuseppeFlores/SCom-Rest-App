package com.example.scom_rest_app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scom_rest_app.R;
import com.example.scom_rest_app.services.Api;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DialogoFragmentPedido extends DialogFragment {
    String[] mesas;
    TableLayout tlDetallePedido;
    //IComunicaFragments iComunicaFragments;
    MaterialButton btnEnviar, btnVolver;
    AutoCompleteTextView acNroMesa;
    TextInputLayout tilNroMesa;
    TextView tvTotal;

    ArrayList<String[]> detallePedidoPlatillo;
    ArrayList<String[]> detallePedidoBebida;

    public interface Enviar{
        public void enviarPedido(String mesa);
    }

    Enviar enviarPedido;

    double total;

    public DialogoFragmentPedido(ArrayList<String[]> detallePlatillos, ArrayList<String[]> detalleBebidas) {
        this.detallePedidoPlatillo = detallePlatillos;
        this.detallePedidoBebida = detalleBebidas;
        this.total = 0;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.setCancelable(false);
        return crearDetallePedido();
    }
    public void setInterface(DialogoFragmentPedido.Enviar enviar){
        this.enviarPedido = enviar;
    }
    public Dialog crearDetallePedido(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialogo_pedido,null);
        builder.setView(view);

        obtenerMesas();

        btnEnviar = view.findViewById(R.id.btn_enviar);
        btnVolver = view.findViewById(R.id.btn_volver);
        tilNroMesa = view.findViewById(R.id.ti_layout_nro_mesa);
        acNroMesa = view.findViewById(R.id.tv_auto_complete);
        tvTotal = view.findViewById(R.id.tv_total);

        tlDetallePedido = view.findViewById(R.id.tl_detalle_pedido);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels-80;

        for(int i = 0 ; i < this.detallePedidoPlatillo.size() ; i++){
            TableRow fila = new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            fila.setLayoutParams(lp);
            fila.setPadding(10,10,10,10);
            String[] datos = this.detallePedidoPlatillo.get(i);
            this.total = this.total + Double.parseDouble(datos[2]);
            for(int j = 0 ; j < 3 ; j++){
                TextView tv = new TextView(getActivity());
                tv.setText(datos[j].toUpperCase());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(15);
                if(j != 0){
                    tv.setGravity(Gravity.CENTER);
                }
                fila.addView(tv);
            }
            tlDetallePedido.addView(fila);
        }

        for(int i = 0 ; i < this.detallePedidoBebida.size() ; i++){
            TableRow fila = new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            fila.setLayoutParams(lp);
            fila.setPadding(10,10,10,10);
            String[] datos = this.detallePedidoBebida.get(i);
            this.total = this.total + Double.parseDouble(datos[2]);
            for(int j = 0 ; j < 3 ; j++){
                TextView tv = new TextView(getActivity());
                tv.setText(datos[j].toUpperCase());
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(15);
                if(j != 0){
                    tv.setGravity(Gravity.CENTER);
                }
                fila.addView(tv);
            }
            tlDetallePedido.addView(fila);
        }

        tvTotal.setText("Bs. "+String.valueOf(this.total));
        acNroMesa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilNroMesa.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ENVIAR",""+acNroMesa.getText());
                if(mesaValida(acNroMesa.getText().toString())){
                    //Toast.makeText(getContext(), "Pedido Enviado", Toast.LENGTH_SHORT).show();
                    enviarPedido.enviarPedido(acNroMesa.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Seleccione una mesa válida por favor", Toast.LENGTH_SHORT).show();
                    tilNroMesa.setErrorEnabled(true);
                    tilNroMesa.setError("Seleccione una mesa válida");
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }
    public void obtenerMesas(){
        Api.getClient().obtenerMesas(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                String result = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    result = reader.readLine();
                    JSONObject object = new JSONObject(result.trim());
                    Log.d("OBTENER MESAS",object.toString());
                    JSONArray data = object.getJSONArray("data");
                    mesas = new String[data.length()];
                    for(int i=0 ; i < data.length() ; i++){
                        JSONObject mesa = data.getJSONObject(i);
                        mesas[i] = mesa.getString("nroMesa");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.dropdown_item,mesas);
                    acNroMesa.setAdapter(adapter);
                    btnEnviar.setEnabled(true);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.d("OBTENER MESAS","error "+e.getMessage().toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("OBTENER MESAS","No hay conexion");
            }
        });
    }
    public boolean mesaValida(String nroMesa){
        for(int i=0 ; i < mesas.length ; i++){
            if(mesas[i].equals(nroMesa)){
                return true;
            }
        }
        return false;
    }
}