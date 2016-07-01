package com.satsumasoftware.pokedex.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.object.Pokemon;


public class PkmnImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_pkmn_image);

        setupLayouts();
    }

    private void setupLayouts() {
        TextView tvId = (TextView) findViewById(R.id.pkmnImageDetail_tvID);
        TextView tvTitle = (TextView) findViewById(R.id.pkmnImageDetail_tvTitle);
        TextView tvSubtitle = (TextView) findViewById(R.id.pkmnImageDetail_tvForm);
        ImageView imageView = (ImageView) findViewById(R.id.pkmnImageDetail_imgv);
        TextView tvCopyright = (TextView) findViewById(R.id.pkmnImageDetail_tvCopyright);

        MiniPokemon miniPokemon = getIntent().getExtras().getParcelable("POKEMON");
        if (miniPokemon == null) {
            throw new NullPointerException("Parcelable MiniPokemon object through Intent is null");
        }

        Pokemon pokemon = miniPokemon.toPokemon(this);

        tvId.setText(pokemon.getNationalIdFormatted());
        tvTitle.setText(pokemon.getPokemon());
        tvSubtitle.setText(pokemon.getFormFormatted());

        pokemon.setPokemonImage(imageView);
        tvCopyright.setText(getString(R.string.copyright_notice_images));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
