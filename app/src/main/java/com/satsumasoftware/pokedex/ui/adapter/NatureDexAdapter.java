package com.satsumasoftware.pokedex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.nature.MiniNature;

import java.util.ArrayList;

public class NatureDexAdapter extends RecyclerView.Adapter<NatureDexAdapter.NatureViewHolder> {

    public static class NatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        NatureViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text1);
        }

        @Override
        public void onClick(View v) {
            if (mOnRowClickListener != null) {
                mOnRowClickListener.onRowClick(v, getPosition());
            }
        }
    }

    private ArrayList<MiniNature> mArrayNatures;

    public NatureDexAdapter(ArrayList<MiniNature> arrayNatures) {
        mArrayNatures = arrayNatures;
    }

    @Override
    public int getItemCount() {
        return mArrayNatures.size();
    }

    @Override
    public NatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple, parent, false);
        return new NatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NatureViewHolder holder, int position) {
        MiniNature nature = mArrayNatures.get(position);
        holder.textView.setText(nature.getName());
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
