package com.satsumasoftware.pokedex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.item.MiniItem;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.item_simple_text1);
        }

        @Override
        public void onClick(View v) {
            if (sOnRowClickListener != null) {
                sOnRowClickListener.onRowClick(v, getLayoutPosition());
            }
        }
    }

    private ArrayList<MiniItem> mArrayItems;

    public ItemsAdapter(ArrayList<MiniItem> arrayItems) {
        mArrayItems = arrayItems;
    }

    @Override
    public int getItemCount() {
        return mArrayItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MiniItem item = mArrayItems.get(position);
        holder.textView.setText(item.getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnRowClickListener {
        void onRowClick(View view, int position);
    }

    private static OnRowClickListener sOnRowClickListener;

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        sOnRowClickListener = onRowClickListener;
    }
}
