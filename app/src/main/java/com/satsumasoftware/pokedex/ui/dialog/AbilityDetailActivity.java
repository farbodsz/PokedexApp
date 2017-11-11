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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.ability.Ability;
import com.satsumasoftware.pokedex.framework.ability.MiniAbility;
import com.satsumasoftware.pokedex.ui.filter.FilterResultsActivity;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.DataUtilsKt;
import com.satsumasoftware.pokedex.util.Flavours;

public class AbilityDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ABILITY = "ABILITY";

    private Ability mAbility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_ability);

        MiniAbility miniAbility = getIntent().getExtras().getParcelable(EXTRA_ABILITY);
        if (miniAbility == null) {
            throw new NullPointerException("Parcelable MiniAbility object through Intent is null");
        }
        mAbility = miniAbility.toAbility(this);

        TextView idText = ((TextView) findViewById(R.id.abilityId));
        TextView nameText = (TextView) findViewById(R.id.abilityName);
        TextView descriptionText = (TextView) findViewById(R.id.abilityDescription);
        TextView genText = (TextView) findViewById(R.id.abilityGeneration);
        Button btnFilter = (Button) findViewById(R.id.abilityDetail_btnFilter);

        idText.setText("# " + mAbility.getId());
        nameText.setText(mAbility.getName());

        descriptionText.setText(mAbility.getFlavorText(this));

        genText.setText(DataUtilsKt.genIdToRoman(mAbility.getGenerationId()));

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Flavours.type == Flavours.Type.PAID) {
                    Intent intent = new Intent(getBaseContext(), FilterResultsActivity.class);
                    intent.putExtra(FilterResultsActivity.FILTER_ABILITY, mAbility.getId());
                    startActivity(intent);
                } else {
                    AlertUtils.requiresProToast(AbilityDetailActivity.this);
                }
            }
        });
    }
}
