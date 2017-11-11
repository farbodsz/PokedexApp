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

import java.util.ArrayList;
import java.util.Collections;

public class ChangelogAdapter extends RecyclerView.Adapter<ChangelogAdapter.ChangelogViewHolder> {

    public static class ChangelogViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;

        ChangelogViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvDescription = (TextView) itemView.findViewById(R.id.body_text);
        }
    }

    private ArrayList<Integer> mArrayVersionCodes;
    private ArrayList<ArrayList<String>> mArrayDescriptions;

    public ChangelogAdapter(ArrayList<Integer> arrayVersionCodes, ArrayList<ArrayList<String>> arrayDescriptions, boolean displayReverseOrder) {
        mArrayVersionCodes = arrayVersionCodes;
        mArrayDescriptions = arrayDescriptions;
        if (displayReverseOrder) {
            Collections.reverse(mArrayVersionCodes);
            Collections.reverse(mArrayDescriptions);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayVersionCodes.size();
    }

    @Override
    public ChangelogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_changelog, parent, false);
        return new ChangelogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChangelogViewHolder holder, int position) {
        String versionCode = String.valueOf(mArrayVersionCodes.get(position));
        String digitOne = versionCode.substring(0, 1);
        String digitTwo = versionCode.substring(1, 2);
        String digitThree = versionCode.substring(2, 3);
        String versionName;
        if (digitThree.equals("0")) {
            versionName = digitOne + "." + digitTwo;
        } else {
            versionName = digitOne + "." + digitTwo + "." + digitThree;
        }
        holder.tvTitle.setText(versionName);

        ArrayList<String> descriptionList = mArrayDescriptions.get(position);
        String descriptionText = "";
        for (int i = 0; i < descriptionList.size(); i++) {
            descriptionText = descriptionText +
                    "\u2022 " + descriptionList.get(i);
            if (i != descriptionList.size()-1) { // If not last description
                descriptionText = descriptionText + "\n";
            }
        }
        holder.tvDescription.setText(descriptionText);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
