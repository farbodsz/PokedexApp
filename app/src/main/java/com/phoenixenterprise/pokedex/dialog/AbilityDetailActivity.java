package com.phoenixenterprise.pokedex.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.phoenixenterprise.pokedex.FilterResultsActivity;
import com.phoenixenterprise.pokedex.R;
import com.phoenixenterprise.pokedex.object.Ability;
import com.phoenixenterprise.pokedex.object.MiniAbility;
import com.phoenixenterprise.pokedex.util.AlertUtils;
import com.phoenixenterprise.pokedex.util.Flavours;
import com.phoenixenterprise.pokedex.util.InfoUtils;


public class AbilityDetailActivity extends AppCompatActivity {

    private Ability mAbility;

    private TextView tvId, tvAbility, tvDescription, tvGen;
    private Button btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_ability);

        receiveIntents();
        castLayouts();
        setLayouts();
    }

    private void receiveIntents() {
        MiniAbility miniAbility = getIntent().getExtras().getParcelable("ABILITY");
        if (miniAbility == null) {
            throw new NullPointerException("Parcelable MiniAbility object through Intent is null");
        }

        mAbility = miniAbility.toAbility(this);
    }

    private void castLayouts() {
        tvId = (TextView) findViewById(R.id.abilityDetail_tvID);
        tvAbility = (TextView) findViewById(R.id.abilityDetail_tvTitle);
        tvDescription = (TextView) findViewById(R.id.abilityDetail_tvDescription);
        tvGen = (TextView) findViewById(R.id.abilityDetail_tvGen_info);
        btnFilter = (Button) findViewById(R.id.abilityDetail_btnFilter);
    }

    private void setLayouts() {
        tvId.setText("# " + mAbility.getAbilityId());
        tvAbility.setText(mAbility.getAbilityName());

        tvDescription.setText(mAbility.getEffect());

        tvGen.setText(InfoUtils.getRomanFromGen(mAbility.getGeneration()));

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Flavours.type == Flavours.Type.PAID) {
                    Intent intent = new Intent(getBaseContext(), FilterResultsActivity.class);
                    intent.putExtra("ABILITY", mAbility.getAbilityName());
                    startActivity(intent);
                } else {
                    AlertUtils.requiresProToast(AbilityDetailActivity.this);
                }
            }
        });
    }
}
