package com.satsumasoftware.pokedex.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.dialog.MovesDetailActivity;
import com.satsumasoftware.pokedex.object.MiniMove;

import java.util.ArrayList;

public class LearnsetVGAdapter {

    private ViewGroup mViewGroup;
    private ArrayList<String> mArrayMoveNames, mArrayLevels;
    private Activity mActivity;
    private View[] mItemViews; // Null if not yet created

    public LearnsetVGAdapter(Activity activity, ViewGroup viewGroup, ArrayList<String> arrayLevels, ArrayList<String> arrayMoves) {
        mViewGroup = viewGroup;
        mActivity = activity;
        mArrayMoveNames = arrayMoves;
        mArrayLevels = arrayLevels;

        if (mArrayMoveNames.size() != mArrayLevels.size()) {
            throw new Error("The size of the moves list and levels list must be identical");
        }
    }

    public void createListItems() {
        mItemViews = new View[mArrayMoveNames.size()];
        mViewGroup.removeAllViews();

        for (int i = 0; i < mArrayMoveNames.size(); i++) {
            mItemViews[i] = makeListItem(i, mViewGroup);
            mViewGroup.addView(mItemViews[i]);
            if (i != mArrayMoveNames.size()-1) {
                mViewGroup.addView(mActivity.getLayoutInflater().inflate(R.layout.divider, mViewGroup, false));
            }
        }
    }

    private View makeListItem(int itemNo, ViewGroup container) {
        String level = mArrayLevels.get(itemNo);
        final String move = mArrayMoveNames.get(itemNo);

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_learnset, container, false);

        TextView tvLevels = (TextView) view.findViewById(R.id.item_learnset_text1);
        TextView tvMoves = (TextView) view.findViewById(R.id.item_learnset_text2);
        tvLevels.setText(level);
        tvMoves.setText(move);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MovesDetailActivity.class);
                intent.putExtra("MOVE", new MiniMove(mActivity, move));
                mActivity.startActivity(intent);
            }
        });

        return view;
    }
}