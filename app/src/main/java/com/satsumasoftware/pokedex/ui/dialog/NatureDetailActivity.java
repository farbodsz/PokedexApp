package com.satsumasoftware.pokedex.ui.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.nature.MiniNature;
import com.satsumasoftware.pokedex.framework.nature.Nature;
import com.satsumasoftware.pokedex.util.DataUtilsKt;

public class NatureDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NATURE = "NATURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_nature);

        MiniNature miniNature = getIntent().getExtras().getParcelable(EXTRA_NATURE);
        if (miniNature == null) {
            throw new NullPointerException("Parcelable Nature object through Intent is null");
        }
        Nature nature = miniNature.toNature(this);

        TextView tvNature = (TextView) findViewById(R.id.natureName);
        TextView tvStatIncreased = (TextView) findViewById(R.id.natureIncreasedStat);
        TextView tvStatDecreased = (TextView) findViewById(R.id.natureDecreasedStat);

        tvNature.setText(nature.getName());
        tvStatIncreased.setText(DataUtilsKt.pokemonStatIdToName(nature.getIncreasedStatId()));
        tvStatDecreased.setText(DataUtilsKt.pokemonStatIdToName(nature.getDecreasedStatId()));
    }
}
