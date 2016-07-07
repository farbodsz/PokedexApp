package com.satsumasoftware.pokedex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.move.MiniMove;

import java.util.ArrayList;

public class MoveDexAdapter extends RecyclerView.Adapter<MoveDexAdapter.MoveViewHolder> {

    public static class MoveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        MoveViewHolder(View itemView) {
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

    private ArrayList<MiniMove> mArrayMoves;

    public MoveDexAdapter(ArrayList<MiniMove> arrayMoves) {
        mArrayMoves = arrayMoves;
    }

    @Override
    public int getItemCount() {
        return mArrayMoves.size();
    }

    @Override
    public MoveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple, parent, false);
        return new MoveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoveViewHolder holder, int position) {
        MiniMove aMove = mArrayMoves.get(position);
        holder.textView.setText(aMove.getName());
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
