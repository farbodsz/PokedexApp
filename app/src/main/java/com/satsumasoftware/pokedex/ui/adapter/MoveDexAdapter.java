/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.move.MiniMove;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;

public class MoveDexAdapter extends RecyclerView.Adapter<MoveDexAdapter.MoveViewHolder>
        implements INameableAdapter, ICustomAdapter {

    @Override
    public String getCustomStringForElement(int element) {
        int generationId = mArrayMoves.get(element).toMove(mContext).getGenerationId();
        return "Gen. " + DataUtilsKt.genIdToRoman(generationId);
    }

    @Override
    public Character getCharacterForElement(int element) {
        return mArrayMoves.get(element).getName().charAt(0);
    }

    public static class MoveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        MoveViewHolder(View itemView) {
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

    private Context mContext;
    private ArrayList<MiniMove> mArrayMoves;

    public MoveDexAdapter(Context context, ArrayList<MiniMove> arrayMoves) {
        mContext = context;
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
