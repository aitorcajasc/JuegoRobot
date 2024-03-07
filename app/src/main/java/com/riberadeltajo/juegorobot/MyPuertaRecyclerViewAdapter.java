package com.riberadeltajo.juegorobot;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riberadeltajo.juegorobot.placeholder.PlaceholderContent.PlaceholderItem;
import com.riberadeltajo.juegorobot.databinding.FragmentPuertaBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPuertaRecyclerViewAdapter extends RecyclerView.Adapter<MyPuertaRecyclerViewAdapter.ViewHolder> {

    private final List<Puerta> mValues;

    public MyPuertaRecyclerViewAdapter(List<Puerta> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentPuertaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPuerta.setImageBitmap(mValues.get(position).puerta);
        Bitmap puertaselec=mValues.get(position).puerta;
        holder.mPuerta.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i=new Intent(v.getContext(), ActividadJuego.class);
                i.putExtra("puerta", puertaselec);
                v.getContext().startActivity(i);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mPuerta;
        public Puerta mItem;

        public ViewHolder(FragmentPuertaBinding binding) {
            super(binding.getRoot());
            mPuerta = binding.puerta;
        }
    }
}