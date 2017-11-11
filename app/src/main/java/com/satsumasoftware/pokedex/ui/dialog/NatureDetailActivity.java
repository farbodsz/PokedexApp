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
