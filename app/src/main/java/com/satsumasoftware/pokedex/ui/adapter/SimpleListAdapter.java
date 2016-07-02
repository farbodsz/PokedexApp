package com.satsumasoftware.pokedex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;

import java.util.ArrayList;

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        SimpleViewHolder(View itemView) {
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

    private ArrayList<String> mArrayList;

    public SimpleListAdapter(ArrayList<String> arrayList) {
        mArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.textView.setText(mArrayList.get(position));
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
