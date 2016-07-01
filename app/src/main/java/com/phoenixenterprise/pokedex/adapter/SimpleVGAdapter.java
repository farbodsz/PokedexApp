package com.phoenixenterprise.pokedex.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.R;

import java.util.ArrayList;

public class SimpleVGAdapter {

    private ViewGroup mViewGroup;
    private ArrayList<String> mArrayEntries;
    private Activity mActivity;
    private View[] mItemViews; // Null if not yet created

    public SimpleVGAdapter(Activity activity, ViewGroup viewGroup, ArrayList<String> arrayEntries) {
        mViewGroup = viewGroup;
        mActivity = activity;
        mArrayEntries = arrayEntries;
    }

    public void createListItems() {
        mItemViews = new View[mArrayEntries.size()];
        mViewGroup.removeAllViews();

        for (int i = 0; i < mArrayEntries.size(); i++) {
            mItemViews[i] = makeListItem(i, mViewGroup);
            mViewGroup.addView(mItemViews[i]);
            if (i != mArrayEntries.size()-1) {
                mViewGroup.addView(mActivity.getLayoutInflater().inflate(R.layout.divider, mViewGroup, false));
            }
        }
    }

    private View makeListItem(final int itemNo, ViewGroup container) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_simple, container, false);

        String anEntry = mArrayEntries.get(itemNo);
        TextView tvEntry = (TextView) view.findViewById(R.id.item_simple_text1);
        tvEntry.setText(anEntry);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEntryClickListener != null) {
                    mOnEntryClickListener.onEntryClick(v, itemNo);
                }
            }
        });

        return view;
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    private static OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}