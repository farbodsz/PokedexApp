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

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterListItemVGAdapter {

    private ViewGroup mViewGroup;
    private List<String> mArrayItems;
    private Activity mActivity;
    private View[] mItemViews; // Null if not yet created

    public FilterListItemVGAdapter(Activity activity, ViewGroup viewGroup, ArrayList<String> arrayItems) {
        mViewGroup = viewGroup;
        mActivity = activity;
        mArrayItems = arrayItems;
    }

    public FilterListItemVGAdapter(Activity activity, ViewGroup viewGroup, int arrayResId) {
        mViewGroup = viewGroup;
        mActivity = activity;
        mArrayItems = Arrays.asList(mActivity.getResources().getStringArray(arrayResId));
    }

    public void createListItems() {
        mItemViews = new View[mArrayItems.size()];
        mViewGroup.removeAllViews();

        for (int i = 0; i < mArrayItems.size(); i++) {
            mItemViews[i] = makeListItem(i, mViewGroup);
            mViewGroup.addView(mItemViews[i]);
        }
    }

    private View makeListItem(final int itemNo, ViewGroup container) {
        final String itemText = mArrayItems.get(itemNo);

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_filter, container, false);
        final TextView textView = (TextView) view.findViewById(R.id.filter_text);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.filter_checkbox);

        textView.setText(itemText);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.toggle();
                View parent = (View) v.getParent();
                mOnFilterItemClickListener.onFilterItemClick(v, itemNo, itemText, checkBox.isChecked(), parent);
                Log.d("FILTER", "View clicked");
            }
        });
        // Sometimes, the 'view' is not exactly tapped and the checkbox is clicked instead:
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent().getParent();
                mOnFilterItemClickListener.onFilterItemClick(v, itemNo, itemText, checkBox.isChecked(), parent);
                Log.d("FILTER", "Checkbox clicked");
            }
        });

        return view;
    }

    public interface OnFilterItemClickListener {
        void onFilterItemClick(View view, int position, String text, boolean isChecked, View itemView);
    }

    private static OnFilterItemClickListener mOnFilterItemClickListener;

    public void setOnFilterItemClickListener(OnFilterItemClickListener onFilterItemClickListener) {
        mOnFilterItemClickListener = onFilterItemClickListener;
    }
}