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
