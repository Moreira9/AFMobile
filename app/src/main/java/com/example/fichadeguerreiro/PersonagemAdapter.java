package com.example.fichadeguerreiro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonagemAdapter extends RecyclerView.Adapter<PersonagemAdapter.ViewHolder> {

    private List<Personagem> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Personagem personagem);
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PersonagemAdapter(List<Personagem> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item (certifique-se que R.layout.item_personagem existe e tem txtNomePersonagem)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personagem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Personagem p = lista.get(position);
        holder.txtNome.setText(p.getNome());


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomePersonagem);
        }
    }
}
