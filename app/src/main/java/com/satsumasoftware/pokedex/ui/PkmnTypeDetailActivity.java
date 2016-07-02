package com.satsumasoftware.pokedex.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.entities.pokemon.Pokemon;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.util.ArrayList;


public class PkmnTypeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POKEMON = "POKEMON";

    private Pokemon mPokemon;
    private SparseIntArray mTypeIds;

    private ArrayList<String> mArrayWeak = new ArrayList<>();
    private ArrayList<String> mArrayStrong = new ArrayList<>();
    private ArrayList<String> mArrayImmune = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_pkmn_type);

        MiniPokemon miniPokemon = getIntent().getExtras().getParcelable(EXTRA_POKEMON);
        if (miniPokemon == null) {
            throw new NullPointerException("Parcelable MiniPokemon object through Intent is null");
        }

        mPokemon = miniPokemon.toPokemon(this);
        mTypeIds = mPokemon.getTypeIds();


        calculate();
    }

    private void calculate() {
        double normal1 = 1, fighting1 = 1, flying1 = 1, poison1 = 1, ground1 = 1, rock1 = 1, bug1 = 1, ghost1 = 1, steel1 = 1,
                fire1 = 1, water1 = 1, grass1 = 1, electr1 = 1, psychic1 = 1, ice1 = 1, dragon1 = 1, dark1 = 1, fairy1 = 1;
        double normal2 = 1, fighting2 = 1, flying2 = 1, poison2 = 1, ground2 = 1, rock2 = 1, bug2 = 1, ghost2 = 1, steel2 = 1,
                fire2 = 1, water2 = 1, grass2 = 1, electr2 = 1, psychic2 = 1, ice2 = 1, dragon2 = 1, dark2 = 1, fairy2 = 1;
        double normalT = 1, fightingT = 1, flyingT = 1, poisonT = 1, groundT = 1, rockT = 1, bugT = 1, ghostT = 1, steelT = 1,
                fireT = 1, waterT = 1, grassT = 1, electrT = 1, psychicT = 1, iceT = 1, dragonT = 1, darkT = 1, fairyT = 1;

        switch (DataUtils.typeIdToString(mTypeIds.get(1)).toLowerCase()) {
            case "normal":
                fighting1 = 2; // Fighting against normal is super effective (i.e. it is mArrayWeak to it)
                ghost1 = 0;
                break;
            case "fighting":
                flying1 = 2;
                rock1 = 0.5;
                bug1 = 0.5;
                psychic1 = 2;
                dark1 = 0.5;
                fairy1 = 2;
                break;
            case "flying":
                fighting1 = 0.5;
                ground1 = 0;
                rock1 = 2;
                bug1 = 0.5;
                grass1 = 0.5;
                electr1 = 2;
                ice1 = 2;
                break;
            case "poison":
                fighting1 = 0.5;
                poison1 = 0.5;
                ground1 = 2;
                bug1 = 0.5;
                grass1 = 0.5;
                psychic1 = 2;
                fairy1 = 0.5;
                break;
            case "ground":
                poison1 = 0.5;
                rock1 = 0.5;
                water1 = 2;
                grass1 = 2;
                electr1 = 0;
                ice1 = 2;
                break;
            case "rock":
                normal1 = 0.5;
                fighting1 = 2;
                flying1 = 0.5;
                poison1 = 0.5;
                ground1 = 2;
                steel1 = 2;
                fire1 = 0.5;
                water1 = 2;
                grass1 = 2;
                break;
            case "bug":
                fighting1 = 0.5;
                flying1 = 2;
                ground1 = 0.5;
                rock1 = 2;
                fire1 = 2;
                grass1 = 0.5;
                break;
            case "ghost":
                normal1 = 0;
                fighting1 = 0;
                poison1 = 0.5;
                bug1 = 0.5;
                ghost1 = 2;
                dark1 = 2;
                break;
            case "steel":
                normal1 = 0.5;
                fighting1 = 2;
                flying1 = 0.5;
                poison1 = 0;
                ground1 = 2;
                rock1 = 0.5;
                bug1 = 0.5;
                steel1 = 0.5;
                fire1 = 2;
                grass1 = 0.5;
                psychic1 = 0.5;
                ice1 = 0.5;
                dragon1 = 0.5;
                fairy1 = 0.5;
                break;
            case "fire":
                ground1 = 2;
                rock1 = 2;
                bug1 = 0.5;
                steel1 = 0.5;
                fire1 = 0.5;
                water1 = 2;
                grass1 = 0.5;
                ice1 = 0.5;
                fairy1 = 0.5;
                break;
            case "water":
                steel1 = 0.5;
                fire1 = 0.5;
                water1 = 0.5;
                grass1 = 2;
                electr1 = 2;
                ice1 = 0.5;
                break;
            case "grass":
                flying1 = 2;
                poison1 = 2;
                ground1 = 0.5;
                bug1 = 2;
                fire1 = 2;
                water1 = 0.5;
                grass1 = 0.5;
                electr1 = 0.5;
                ice1 = 2;
                break;
            case "electric":
                flying1 = 0.5;
                ground1 = 2;
                steel1 = 0.5;
                electr1 = 0.5;
                break;
            case "psychic":
                fighting1 = 0.5;
                bug1 = 2;
                ghost1 = 2;
                psychic1 = 0.5;
                dark1 = 2;
                break;
            case "ice":
                fighting1 = 2;
                rock1 = 2;
                steel1 = 2;
                fire1 = 2;
                ice1 = 0.5;
                break;
            case "dragon":
                fire1 = 0.5;
                water1 = 0.5;
                grass1 = 0.5;
                electr1 = 0.5;
                ice1 = 2;
                dragon1 = 2;
                fairy1 = 2;
                break;
            case "fairy":
                fighting1 = 0.5;
                poison1 = 2;
                bug1 = 0.5;
                steel1 = 2;
                dragon1 = 0;
                dark1 = 0.5;
                break;
        }

        int type2Id = mTypeIds.get(2);
        if (Pokemon.hasSecondaryType(type2Id)) {
            switch (DataUtils.typeIdToString(type2Id).toLowerCase()) {
                case "normal":
                    fighting2 = 2; // Fighting against normal is super effective (i.e. it is mArrayWeak to it)
                    ghost2 = 0;
                    break;
                case "fighting":
                    flying2 = 2;
                    rock2 = 0.5;
                    bug2 = 0.5;
                    psychic2 = 2;
                    dark2 = 0.5;
                    fairy2 = 2;
                    break;
                case "flying":
                    fighting2 = 0.5;
                    ground2 = 0;
                    rock2 = 2;
                    bug2 = 0.5;
                    grass2 = 0.5;
                    electr2 = 2;
                    ice2 = 2;
                    break;
                case "poison":
                    fighting2 = 0.5;
                    poison2 = 0.5;
                    ground2 = 2;
                    bug2 = 0.5;
                    grass2 = 0.5;
                    psychic2 = 2;
                    fairy2 = 0.5;
                    break;
                case "ground":
                    poison2 = 0.5;
                    rock2 = 0.5;
                    water2 = 2;
                    grass2 = 2;
                    electr2 = 0;
                    ice2 = 2;
                    break;
                case "rock":
                    normal2 = 0.5;
                    fighting2 = 2;
                    flying2 = 0.5;
                    poison2 = 0.5;
                    ground2 = 2;
                    steel2 = 2;
                    fire2 = 0.5;
                    water2 = 2;
                    grass2 = 2;
                    break;
                case "bug":
                    fighting2 = 0.5;
                    flying2 = 2;
                    ground2 = 0.5;
                    rock2 = 2;
                    fire2 = 2;
                    grass2 = 0.5;
                    break;
                case "ghost":
                    normal2 = 0;
                    fighting2 = 0;
                    poison2 = 0.5;
                    bug2 = 0.5;
                    ghost2 = 2;
                    dark2 = 2;
                    break;
                case "steel":
                    normal2 = 0.5;
                    fighting2 = 2;
                    flying2 = 0.5;
                    poison2 = 0;
                    ground2 = 2;
                    rock2 = 0.5;
                    bug2 = 0.5;
                    steel2 = 0.5;
                    fire2 = 2;
                    grass2 = 0.5;
                    psychic2 = 0.5;
                    ice2 = 0.5;
                    dragon2 = 0.5;
                    fairy2 = 0.5;
                    break;
                case "fire":
                    ground2 = 2;
                    rock2 = 2;
                    bug2 = 0.5;
                    steel2 = 0.5;
                    fire2 = 0.5;
                    water2 = 2;
                    grass2 = 0.5;
                    ice2 = 0.5;
                    fairy2 = 0.5;
                    break;
                case "water":
                    steel2 = 0.5;
                    fire2 = 0.5;
                    water2 = 0.5;
                    grass2 = 2;
                    electr2 = 2;
                    ice2 = 0.5;
                    break;
                case "grass":
                    flying2 = 2;
                    poison2 = 2;
                    ground2 = 0.5;
                    bug2 = 2;
                    fire2 = 2;
                    water2 = 0.5;
                    grass2 = 0.5;
                    electr2 = 0.5;
                    ice2 = 2;
                    break;
                case "electric":
                    flying2 = 0.5;
                    ground2 = 2;
                    steel2 = 0.5;
                    electr2 = 0.5;
                    break;
                case "psychic":
                    fighting2 = 0.5;
                    bug2 = 2;
                    ghost2 = 2;
                    psychic2 = 0.5;
                    dark2 = 2;
                    break;
                case "ice":
                    fighting2 = 2;
                    rock2 = 2;
                    steel2 = 2;
                    fire2 = 2;
                    ice2 = 0.5;
                    break;
                case "dragon":
                    fire2 = 0.5;
                    water2 = 0.5;
                    grass2 = 0.5;
                    electr2 = 0.5;
                    ice2 = 2;
                    dragon2 = 2;
                    fairy2 = 2;
                    break;
                case "fairy":
                    fighting2 = 0.5;
                    poison2 = 2;
                    bug2 = 0.5;
                    steel2 = 2;
                    dragon2 = 0;
                    dark2 = 0.5;
                    break;
            }
        }

        normalT = normal1 * normal2;
        fightingT = fighting1 * fighting2;
        flyingT = flying1 * flying2;
        poisonT = poison1 * poison2;
        groundT = ground1 * ground2;
        rockT = rock1 * rock2;
        bugT = bug1 * bug2;
        ghostT = ghost1 * ghost2;
        steelT = steel1 * steel2;
        fireT = fire1 * fire2;
        waterT = water1 * water2;
        grassT = grass1 * grass2;
        electrT = electr1 * electr2;
        psychicT = psychic1 * psychic2;
        iceT = ice1 * ice2;
        dragonT = dragon1 * dragon2;
        darkT = dark1 * dark2;
        fairyT = fairy1 * fairy2;


        // 2 = mArrayWeak, 0.5 = mArrayStrong, 0 = mArrayImmune
        if (normalT == 0) mArrayImmune.add("Normal");
        else if (normalT == 0.5 || normalT == 0.25) mArrayStrong.add("Normal");
        else if (normalT == 2 || normalT == 4) mArrayWeak.add("Normal");

        if (fightingT == 0) mArrayImmune.add("Fighting");
        else if (fightingT == 0.5 || fightingT == 0.25) mArrayStrong.add("Fighting");
        else if (fightingT == 2 || fightingT == 4) mArrayWeak.add("Fighting");

        if (flyingT == 0) mArrayImmune.add("Flying");
        else if (flyingT == 0.5 || flyingT == 0.25) mArrayStrong.add("Flying");
        else if (flyingT == 2 || flyingT == 4) mArrayWeak.add("Flying");

        if (poisonT == 0) mArrayImmune.add("Poison");
        else if (poisonT == 0.5 || poisonT == 0.25) mArrayStrong.add("Poison");
        else if (poisonT == 2 || poisonT == 4) mArrayWeak.add("Poison");

        if (groundT == 0) mArrayImmune.add("Ground");
        else if (groundT == 0.5 || groundT == 0.25) mArrayStrong.add("Ground");
        else if (groundT == 2 || groundT == 4) mArrayWeak.add("Ground");

        if (rockT == 0) mArrayImmune.add("Rock");
        else if (rockT == 0.5 || rockT == 0.25) mArrayStrong.add("Rock");
        else if (rockT == 2 || rockT == 4) mArrayWeak.add("Rock");

        if (bugT == 0) mArrayImmune.add("Bug");
        else if (bugT == 0.5 || bugT == 0.25) mArrayStrong.add("Bug");
        else if (bugT == 2 || bugT == 4) mArrayWeak.add("Bug");

        if (ghostT == 0) mArrayImmune.add("Ghost");
        else if (ghostT == 0.5 || ghostT == 0.25) mArrayStrong.add("Ghost");
        else if (ghostT == 2 || ghostT == 4) mArrayWeak.add("Ghost");

        if (steelT == 0) mArrayImmune.add("Steel");
        else if (steelT == 0.5 || steelT == 0.25) mArrayStrong.add("Steel");
        else if (steelT == 2 || steelT == 4) mArrayWeak.add("Steel");

        if (fireT == 0) mArrayImmune.add("Fire");
        else if (fireT == 0.5 || fireT == 0.25) mArrayStrong.add("Fire");
        else if (fireT == 2 || fireT == 4) mArrayWeak.add("Fire");

        if (waterT == 0) mArrayImmune.add("Water");
        else if (waterT == 0.5 || waterT == 0.25) mArrayStrong.add("Water");
        else if (waterT == 2 || waterT == 4) mArrayWeak.add("Water");

        if (grassT == 0) mArrayImmune.add("Grass");
        else if (grassT == 0.5 || grassT == 0.25) mArrayStrong.add("Grass");
        else if (grassT == 2 || grassT == 4) mArrayWeak.add("Grass");

        if (electrT == 0) mArrayImmune.add("Electric");
        else if (electrT == 0.5 || electrT == 0.25) mArrayStrong.add("Electric");
        else if (electrT == 2 || electrT == 4) mArrayWeak.add("Electric");

        if (psychicT == 0) mArrayImmune.add("Psychic");
        else if (psychicT == 0.5 || psychicT == 0.25) mArrayStrong.add("Psychic");
        else if (psychicT == 2 || psychicT == 4) mArrayWeak.add("Psychic");

        if (iceT == 0) mArrayImmune.add("Ice");
        else if (iceT == 0.5 || iceT == 0.25) mArrayStrong.add("Ice");
        else if (iceT == 2 || iceT == 4) mArrayWeak.add("Ice");

        if (dragonT == 0) mArrayImmune.add("Dragon");
        else if (dragonT == 0.5 || dragonT == 0.25) mArrayStrong.add("Dragon");
        else if (dragonT == 2 || dragonT == 4) mArrayWeak.add("Dragon");

        if (darkT == 0) mArrayImmune.add("Dark");
        else if (darkT == 0.5 || darkT == 0.25) mArrayStrong.add("Dark");
        else if (darkT == 2 || darkT == 4) mArrayWeak.add("Dark");

        if (fairyT == 0) mArrayImmune.add("Fairy");
        else if (fairyT == 0.5 || fairyT == 0.25) mArrayStrong.add("Fairy");
        else if (fairyT == 2 || fairyT == 4) mArrayWeak.add("Fairy");


        setupLayout();
    }

    private void setupLayout() {
        TextView tvTitle = (TextView) findViewById(R.id.pokemonName);
        ListView lvImmune = (ListView) findViewById(R.id.listView_immuneTypes);
        ListView lvStrong = (ListView) findViewById(R.id.listView_strongTypes);
        ListView lvWeak = (ListView) findViewById(R.id.listView_weakTypes);

        if (mArrayImmune.isEmpty()) mArrayImmune.add("None");
        if (mArrayStrong.isEmpty()) mArrayStrong.add("None");
        if (mArrayWeak.isEmpty()) mArrayWeak.add("None");
        ArrayAdapter<String> adapterImmune = new ArrayAdapter<String>(this, R.layout.list_item_pkmn_types, mArrayImmune) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // See: http://stackoverflow.com/questions/20809272/android-change-listview-item-text-color
                View view = super.getView(position, convertView, parent);
                TextView thisItem = (TextView) view.findViewById(R.id.lv_compressed_text1);
                String thisType = thisItem.getText().toString();
                thisItem.setBackgroundColor(getResources().getColor(InfoUtils.getTypeBkgdColorRes(thisType)));
                return view;
            }
        };
        ArrayAdapter<String> adapterStrong = new ArrayAdapter<String>(this, R.layout.list_item_pkmn_types, mArrayStrong) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView thisItem = (TextView) view.findViewById(R.id.lv_compressed_text1);
                String thisType = thisItem.getText().toString();
                thisItem.setBackgroundColor(getResources().getColor(InfoUtils.getTypeBkgdColorRes(thisType)));
                return view;
            }
        };
        ArrayAdapter<String> adapterWeak = new ArrayAdapter<String>(this, R.layout.list_item_pkmn_types, mArrayWeak) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView thisItem = (TextView) view.findViewById(R.id.lv_compressed_text1);
                String thisType = thisItem.getText().toString();
                thisItem.setBackgroundColor(getResources().getColor(InfoUtils.getTypeBkgdColorRes(thisType)));
                return view;
            }
        };

        tvTitle.setText(mPokemon.getFormAndPokemonName());
        lvImmune.setAdapter(adapterImmune);
        lvStrong.setAdapter(adapterStrong);
        lvWeak.setAdapter(adapterWeak);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
