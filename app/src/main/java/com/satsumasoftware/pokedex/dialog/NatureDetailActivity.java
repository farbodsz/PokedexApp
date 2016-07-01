package com.satsumasoftware.pokedex.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.Nature;
import com.satsumasoftware.pokedex.util.InfoUtils;


public class NatureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_nature);

        Nature nature = getIntent().getExtras().getParcelable("NATURE");
        if (nature == null) {
            throw new NullPointerException("Parcelable Nature object through Intent is null");
        }

        TextView tvNature = (TextView) findViewById(R.id.natureDetail_tvTitle);
        TextView tvStatIncreased = (TextView) findViewById(R.id.natureDetail_tvStatIncr_info);
        TextView tvStatDecreased = (TextView) findViewById(R.id.natureDetail_tvStatDecr_info);

        tvNature.setText(nature.getNatureName());
        tvStatIncreased.setText(InfoUtils.getStatFromAbbreviation(nature.getStatIncreased()));
        tvStatDecreased.setText(InfoUtils.getStatFromAbbreviation(nature.getStatDecreased()));
    }
}
