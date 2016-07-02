package com.satsumasoftware.pokedex.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.pokemon.BasePokemon;
import com.satsumasoftware.pokedex.entities.pokemon.Pokemon;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.satsumasoftware.pokedex.util.ThemeUtils;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PkmnImageActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON = "POKEMON";

    private Pokemon mPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BasePokemon basePokemon = getIntent().getExtras().getParcelable(EXTRA_POKEMON);
        if (basePokemon == null) {
            throw new NullPointerException("Parcelable BasePokemon object through Intent is null");
        }

        mPokemon = basePokemon.toPokemon(this);

        if (mPokemon == null) {
            throw new NullPointerException("Pokemon is null");
        }

        switch (PrefUtils.detailColorType(this)) {
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_DEFAULT:
                // Do nothing
                break;
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_TYPE:
                ThemeUtils.colourDetailByType(this, mPokemon.getTypeIds().get(1));
                break;
            case PrefUtils.PREF_DETAIL_COLORING_VALUE_COLOUR:
                ThemeUtils.colourDetailByColour(this, Pokemon.getColorId(mPokemon.getPhysicalAttrs()));
                break;
        }
        setContentView(R.layout.activity_pkmn_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        assert toolbar != null;
        getSupportActionBar().setTitle(mPokemon.getFormAndPokemonName());
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        assert imageView != null;
        mPokemon.setPokemonImage(imageView);
        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        attacher.update();
    }
}
