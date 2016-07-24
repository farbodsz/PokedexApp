package com.satsumasoftware.pokedex.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokeDB;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.framework.pokemon.PokemonEvolution;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;

public class EvolutionsAdapter extends RecyclerView.Adapter<EvolutionsAdapter.EvolutionsViewHolder> {

    public static class EvolutionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView text1, text2;

        EvolutionsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private Context mContext;
    private ArrayList<MiniPokemon> mEvolutions;
    private MiniPokemon mCurrentPokemon;

    private SQLiteDatabase mDatabase;

    public EvolutionsAdapter(Context context, ArrayList<MiniPokemon> evolutions,
                             MiniPokemon currentPokemon) {
        mContext = context;
        mEvolutions = evolutions;
        mCurrentPokemon = currentPokemon;
        mDatabase = new PokeDB(context).getReadableDatabase();
    }

    @Override
    public int getItemCount() {
        return mEvolutions.size();
    }

    @Override
    public EvolutionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pokemon_with_image, parent, false);
        return new EvolutionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EvolutionsViewHolder holder, int position) {
        Pokemon pokemon = mEvolutions.get(position).toPokemon(mContext);

        if (PrefUtils.showPokemonImages(mContext)) {
            pokemon.setPokemonImage(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        boolean sameAsCurrent = pokemon.getSpeciesId() == mCurrentPokemon.getSpeciesId();

        String name = pokemon.getFormAndPokemonName();
        holder.text1.setText(sameAsCurrent ? Html.fromHtml("<b>" + name + "</b>") : name);

        String evolutionMethod = "";

        PokemonEvolution evolutionData = pokemon.getEvolutionDataObject(mContext);
        if (evolutionData == null) {
            evolutionMethod = "Base form";
        } else {
            // TODO get values via the database, for different languages (chosen in Settings)
            switch (evolutionData.getEvolutionTriggerId()) {
                case 1:
                    evolutionMethod = "Lv. " + evolutionData.getMinimumLevel();
                    break;
                case 2:
                    int tradeSpeciesId = evolutionData.getTradeSpeciesId();
                    evolutionMethod = (tradeSpeciesId == DataUtilsKt.NULL_INT) ?
                            "Trade" :
                            "Trade with " + new MiniPokemon(mContext, tradeSpeciesId, false).getName();
                    break;
                case 3:
                    evolutionMethod = "By item";
                    break;
                case 4:
                    evolutionMethod = "By shed";
                    break;
            }
        }

        holder.text2.setText(evolutionMethod);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    private static OnEntryClickListener mOnEntryClickListener;

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}
