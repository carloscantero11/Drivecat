package com.karmadev.drivecat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karmadev.drivecat.R;
import com.karmadev.drivecat.model.Movimientos;

import java.util.List;

public class MovimientosAdapter extends RecyclerView.Adapter<MovimientosAdapter.MovimientosViewHolder> {
    private List<Movimientos> registroMovimientosList;
    private Context context;

    // Constructor
    public MovimientosAdapter(Context context, List<Movimientos> registroMovimientosList) {
        this.context = context;
        this.registroMovimientosList = registroMovimientosList;
    }

    // ViewHolder
    public static class MovimientosViewHolder extends RecyclerView.ViewHolder {
        TextView id, usuario, ruta, transporte, placa,fecha;
        // Otros campos

        public MovimientosViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idMovimiento);
            usuario = itemView.findViewById(R.id.usuario);
            ruta = itemView.findViewById(R.id.ruta);
            transporte = itemView.findViewById(R.id.transportista);
            placa = itemView.findViewById(R.id.placa);
            fecha = itemView.findViewById(R.id.fecha);
        }
    }

    // Métodos de RecyclerView.Adapter
    @NonNull
    @Override
    public MovimientosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card_movements, parent, false);
        return new MovimientosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientosViewHolder holder, int position) {
        Movimientos registro = registroMovimientosList.get(position);
        holder.id.setText(String.valueOf(registro.getId()));
        holder.usuario.setText(registro.getUsuario());
        holder.ruta.setText(registro.getRuta());
        holder.transporte.setText(registro.getTransporte());
        holder.placa.setText(registro.getPlaca());
        holder.fecha.setText(registro.getFecha());
    }

    @Override
    public int getItemCount() {
        return registroMovimientosList.size();
    }
}

