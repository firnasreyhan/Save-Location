package com.android.tracking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tracking.R;
import com.android.tracking.model.LocationModel;
import com.android.tracking.viewmodel.MainViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<LocationModel> list;
    private MainViewModel viewModel;

    public LocationAdapter(List<LocationModel> list, MainViewModel viewModel) {
        this.list = list;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        holder.latitude.setText(list.get(position).latitude);
        holder.longitude.setText(list.get(position).longitude);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView latitude, longitude;
        private MaterialButton hapus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            hapus = itemView.findViewById(R.id.hapus);

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.deleteImage(list.get(getAdapterPosition()));
                }
            });
        }
    }
}
