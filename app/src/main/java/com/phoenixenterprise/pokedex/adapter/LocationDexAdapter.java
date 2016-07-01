package com.phoenixenterprise.pokedex.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.R;
import com.phoenixenterprise.pokedex.object.Location;

import java.util.ArrayList;

public class LocationDexAdapter extends RecyclerView.Adapter<LocationDexAdapter.LocationViewHolder> {

    public static class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvRegion;

        LocationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView) itemView.findViewById(R.id.item_location_text1);
            tvRegion = (TextView) itemView.findViewById(R.id.item_location_text2);
        }

        @Override
        public void onClick(View v) {
            if (mOnRowClickListener != null) {
                mOnRowClickListener.onRowClick(v, getPosition());
            }
        }
    }

    private ArrayList<Location> mArrayLocations;
    private boolean mDisplayRegion;

    public LocationDexAdapter(ArrayList<Location> arrayLocations, boolean displayRegion) {
        mArrayLocations = arrayLocations;
        mDisplayRegion = displayRegion;
    }

    @Override
    public int getItemCount() {
        return mArrayLocations.size();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        Location location = mArrayLocations.get(position);
        holder.tvName.setText(location.getLocation());
        if (mDisplayRegion) {
            holder.tvRegion.setText("(" + location.getRegion() + ")");
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnRowClickListener {
        void onRowClick(View view, int position);
    }

    private static OnRowClickListener mOnRowClickListener;

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        mOnRowClickListener = onRowClickListener;
    }
}
