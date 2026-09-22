package com.example.myapplication1.db;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication1.R;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.PantryViewHolder> {

    public interface OnpantryListener{
        void onEditClick(pantry item);
        void onDeleteClick(pantry item);
    }

    private final Context context;
    private final List<pantry> pantries;
    private final OnpantryListener listener;

    public Adapter(Context context, List<pantry> pantries, OnpantryListener listener){//constructor
        this.context = context;
        this.pantries = pantries;
        this.listener = listener;

    }
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.p_item, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position){

        if (position < 0 || position >= pantries.size()){
            return;
        }
        pantry item = pantries.get(position);
        if (item == null){
            return;
        }

        String name = item.getName();
        holder.textIngredientName.setText(name == null || name.trim().isEmpty() ? "unnamed ingredient" : name);
        String unit = item.getUnit();
        if (unit == null || unit.trim().isEmpty()){
            unit = "";
        }
        String quantity = String.format(Locale.getDefault(), "%.2f %s", item.getQuantity(), unit);
        holder.textQuantity.setText(quantity.trim());

        String expiry = item.getExpiryD();
        if (expiry != null && ! expiry.trim().isEmpty()){
            holder.textExpiry.setVisibility(View.VISIBLE);
            holder.textExpiry.setText("Expires: " + expiry);

        }else{
            holder.textExpiry.setVisibility(View.GONE);
        }
        holder.buttonEdit.setOnClickListener( view ->{
                if(listener != null) {
                    listener.onEditClick(item);
                }
        });
        holder.buttonDelete.setOnClickListener( view -> {

            if (listener != null) {
                listener.onDeleteClick(item);
            }
        });
    }
    @Override
    public int getItemCount(){

        return pantries == null ? 0 : pantries.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder{
        TextView textIngredientName;
        TextView textQuantity;
        TextView textExpiry;

        ImageButton buttonEdit;
        ImageButton buttonDelete;

        PantryViewHolder(@NonNull View itemView){
            super(itemView);
            textIngredientName = itemView.findViewById(R.id.textIngredientName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textExpiry = itemView.findViewById(R.id.textExpiry);

            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

        }
    }
}
