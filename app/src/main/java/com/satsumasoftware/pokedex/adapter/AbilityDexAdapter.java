package com.satsumasoftware.pokedex.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniAbility;

import java.util.ArrayList;

public class AbilityDexAdapter extends RecyclerView.Adapter<AbilityDexAdapter.AbilityViewHolder> {

    public static class AbilityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        AbilityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.item_simple_text1);
        }

        @Override
        public void onClick(View v) {
            if (mOnRowClickListener != null) {
                mOnRowClickListener.onRowClick(v, getPosition());
            }
        }
    }

    private ArrayList<MiniAbility> mArrayAbilities;

    public AbilityDexAdapter(ArrayList<MiniAbility> arrayAbilities) {
        mArrayAbilities = arrayAbilities;
    }

    @Override
    public int getItemCount() {
        return mArrayAbilities.size();
    }

    @Override
    public AbilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple, parent, false);
        return new AbilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AbilityViewHolder holder, int position) {
        MiniAbility anAbility = mArrayAbilities.get(position);
        holder.textView.setText(anAbility.getAbility());
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
