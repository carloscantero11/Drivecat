package com.karmadev.drivecat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.karmadev.drivecat.R;
import com.karmadev.drivecat.model.Actividad;
import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    private Context context;
    private List<Actividad> actividadList;
    public ActividadAdapter(Context context, List<Actividad> actividadList) {
        this.context = context;
        this.actividadList = actividadList;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_card, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = actividadList.get(position);
        String nombreCompleto = actividad.getNombre();
        holder.nombreTextView.setText(nombreCompleto);
        holder.rutaTextView.setText(actividad.getRuta());
    }


    @Override
    public int getItemCount() {
        return actividadList.size();
    }

    public static class ActividadViewHolder extends RecyclerView.ViewHolder {

        TextView nombreTextView, rutaTextView;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre);
            rutaTextView = itemView.findViewById(R.id.ruta);
        }
    }
}
