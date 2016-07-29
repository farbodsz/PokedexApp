package com.satsumasoftware.pokedex.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.ArrayList;

public class AbilityDexAdapter extends RecyclerView.Adapter<AbilityDexAdapter.AbilityViewHolder>
        implements INameableAdapter, ICustomAdapter{

    @Override
    public String getCustomStringForElement(int element) {
        int generationId = mArrayAbilities.get(element).toAbility(mContext).getGenerationId();
        return "Gen. " + DataUtilsKt.genIdToRoman(generationId);
    }

    @Override
    public Character getCharacterForElement(int element) {
        return mArrayAbilities.get(element).getName().charAt(0);
    }

    public class AbilityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        AbilityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.text1);
        }

        @Override
        public void onClick(View v) {
            if (mOnRowClickListener != null) {
                mOnRowClickListener.onRowClick(v, getLayoutPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<MiniAbility> mArrayAbilities;

    public AbilityDexAdapter(Context context, ArrayList<MiniAbility> arrayAbilities) {
        mContext = context;
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
        holder.textView.setText(anAbility.getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnRowClickListener {
        void onRowClick(View view, int position);
    }

    private OnRowClickListener mOnRowClickListener;

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        mOnRowClickListener = onRowClickListener;
    }
}
