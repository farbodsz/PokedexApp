package com.satsumasoftware.pokedex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farbod.labelledspinner.LabelledSpinner;
import com.satsumasoftware.pokedex.adapter.FormsVGAdapter;
import com.satsumasoftware.pokedex.adapter.LearnsetVGAdapter;
import com.satsumasoftware.pokedex.adapter.PokedexAdapter;
import com.satsumasoftware.pokedex.adapter.SimpleVGAdapter;
import com.satsumasoftware.pokedex.db.PokedexDBHelper;
import com.satsumasoftware.pokedex.dialog.AbilityDetailActivity;
import com.satsumasoftware.pokedex.dialog.MovesDetailActivity;
import com.satsumasoftware.pokedex.dialog.PkmnImageDetailActivity;
import com.satsumasoftware.pokedex.dialog.PkmnTypeDetailActivity;
import com.satsumasoftware.pokedex.dialog.PropertyDetailActivity;
import com.satsumasoftware.pokedex.misc.DividerItemDecoration;
import com.satsumasoftware.pokedex.object.Learnset;
import com.satsumasoftware.pokedex.object.MiniAbility;
import com.satsumasoftware.pokedex.object.MiniMove;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.object.Pokemon;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.AppConfig;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.InfoUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;
import com.satsumasoftware.pokedex.util.ThemeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;



public class DetailActivity extends AppCompatActivity {

    private View mRootLayout;

    private static Pokemon mPokemon;

    private static int mPkmnId;
    private static String mPkmnName, mPkmnForm, mPkmnFormFormatted;

    private String mPkmnNameFull;

    private MenuItem mMenuItemFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MiniPokemon miniPokemon = getIntent().getExtras().getParcelable("POKEMON");
        if (miniPokemon == null) {
            throw new NullPointerException("Parcelable MiniPokemon object through Intent is null");
        }
        mPokemon = miniPokemon.toPokemon(this);

        switch (PrefUtils.detailColourType(this)) {
            case PrefUtils.PREF_DETAIL_COLOURING_VALUE_DEFAULT:
                // Do nothing
                break;
            case PrefUtils.PREF_DETAIL_COLOURING_VALUE_TYPE:
                ThemeUtils.colourDetailByType(this, mPokemon.getType1());
                break;
            case PrefUtils.PREF_DETAIL_COLOURING_VALUE_COLOUR:
                ThemeUtils.colourDetailByColour(this, mPokemon.getColour());
                break;
        }
        setContentView(R.layout.activity_detail);
        mRootLayout = findViewById(R.id.detail_rootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AdUtils.setupAds(this, R.id.detail_adView);

        mPkmnId = mPokemon.getNationalId();
        mPkmnName = mPokemon.getPokemon();
        mPkmnForm = mPokemon.getForm();
        mPkmnFormFormatted = mPokemon.getFormFormatted();
        mPkmnNameFull = InfoUtils.getNameAndForm(mPkmnName, mPkmnFormFormatted);

        ViewPager viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.text_white_secondary),
                ContextCompat.getColor(this, R.color.text_white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem previous = menu.findItem(R.id.action_previous);
        MenuItem next = menu.findItem(R.id.action_next);
        MenuItem compare = menu.findItem(R.id.action_compare);
        mMenuItemFavourite = menu.findItem(R.id.action_favourite);

        if (Flavours.type == Flavours.Type.FREE) {
            previous.setIcon(R.drawable.ic_chevron_left_grey600_48dp);
            next.setIcon(R.drawable.ic_chevron_right_grey600_48dp);
            compare.setIcon(R.drawable.ic_compare_grey600_48dp);
            mMenuItemFavourite.setIcon(R.drawable.ic_star_outline_grey600_48dp);
            return true;
        }

        if (mPkmnId == 1) {
            previous.setIcon(R.drawable.ic_chevron_left_grey600_48dp);
        } else if (mPkmnId == AppConfig.MAX_NATIONAL_ID) {
            next.setIcon(R.drawable.ic_chevron_right_grey600_48dp);
        }

        if (PrefUtils.isAFavouritePkmn(this, InfoUtils.formatPokemonId(mPkmnId), mPkmnName, mPkmnForm)) {
            mMenuItemFavourite.setIcon(R.drawable.ic_star_white_48dp);
            mMenuItemFavourite.setTitle(R.string.action_favourite_remove);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                if (Flavours.type == Flavours.Type.PAID) {
                    if (!(mPkmnId == 1)) {
                        action_navigation(0);
                    }
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_next:
                if (Flavours.type == Flavours.Type.PAID) {
                    if (!(mPkmnId == AppConfig.MAX_NATIONAL_ID)) {
                        action_navigation(1);
                    }
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_compare:
                if (Flavours.type == Flavours.Type.PAID) {
                    action_compare_chooser();
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_favourite:
                if (Flavours.type == Flavours.Type.PAID) {
                    PrefUtils.markAsFavouritePkmn(this, InfoUtils.formatPokemonId(mPkmnId), mPkmnName, mPkmnForm, mRootLayout);
                    if (PrefUtils.isAFavouritePkmn(this, InfoUtils.formatPokemonId(mPkmnId), mPkmnName, mPkmnForm)) {
                        mMenuItemFavourite.setIcon(R.drawable.ic_star_white_48dp);
                        mMenuItemFavourite.setTitle(R.string.action_favourite_remove);
                    } else {
                        mMenuItemFavourite.setIcon(R.drawable.ic_star_outline_white_48dp);
                    }
                } else {
                    AlertUtils.requiresProSnackbar(this, mRootLayout);
                }
                break;
            case R.id.action_calculate_experience:
                goToExperienceCalculator(this);
                break;
            case R.id.action_play_cry:
                ActionUtils.playPokemonCry(this, mPokemon);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void action_navigation(int navigationType) {
        PokedexDBHelper dbHelper = new PokedexDBHelper(this);
        ArrayList<MiniPokemon> list = dbHelper.getAllPokemon();
        ArrayList<String> stringList = new ArrayList<>();
        for (MiniPokemon item : list) {
            stringList.add(item.toString());
        }
        int pos = stringList.indexOf(mPokemon.toString());
        int newPos;
        if (navigationType == 0) {
            // Previous
            newPos = pos - 1;
        } else {
            // Next
            newPos = pos + 1;
        }
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("POKEMON", MiniPokemon.stringToPokemon(stringList.get(newPos)));
        startActivity(intent);
    }

    private void action_compare_chooser() {
        if (mPokemon.hasAlternateForms()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title_compare_pokemon_forms)
                    .setMessage(R.string.dialog_msg_compare_pokemon_forms)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            action_compare(true);
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            action_compare(false);
                        }
                    })
                    .setNeutralButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .show();
        } else {
            action_compare(false);
        }
    }

    private void action_compare(boolean compareToForm) {
        String title = getResources().getString(R.string.dialog_title_compare_pokemon);
        LayoutInflater inflater = getLayoutInflater();
        View listPicker = inflater.inflate(R.layout.dialog_list, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(listPicker)
                .setTitle(title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final ArrayList<MiniPokemon> arrayPokemon;
        if (compareToForm) {
            arrayPokemon = mPokemon.getAlternateForms(false);
        } else {
            PokedexDBHelper dbHelper = new PokedexDBHelper(this);
            arrayPokemon = dbHelper.getAllPokemon();
        }

        RecyclerView pokemonList = (RecyclerView) listPicker.findViewById(R.id.dialog_rv);
        pokemonList.setHasFixedSize(true);
        pokemonList.setLayoutManager(new LinearLayoutManager(this));
        pokemonList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        PokedexAdapter adapter = new PokedexAdapter(this, arrayPokemon);
        adapter.setOnEntryClickListener(new PokedexAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Intent intent = new Intent(getBaseContext(), CompareActivity.class);
                intent.putExtra("POKEMON_1", mPokemon.toMiniPokemon());
                intent.putExtra("POKEMON_2", arrayPokemon.get(position));
                startActivity(intent);

                alertDialog.dismiss();
            }
        });
        pokemonList.setAdapter(adapter);
        alertDialog.show();
    }

    private static void goToExperienceCalculator(Context context) {
        Intent intent = new Intent(context, ExperienceCalculatorActivity.class);
        intent.putExtra("POKEMON_NAME", mPkmnName);
        context.startActivity(intent);
    }


    /*
     * VIEW PAGER ADAPTER
     */

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // Returns the number of tabs
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // Returns a new instance of the fragment
                case 0:
                    return new MainFragment();
                case 1:
                    return new EvolutionsFragment();
                case 2:
                    return new MovesFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.tab_pkmn_detail_general).toUpperCase(l);
                case 1:
                    return getString(R.string.tab_pkmn_detail_evolutions).toUpperCase(l);
                case 2:
                    return getString(R.string.tab_pkmn_detail_moves).toUpperCase(l);
            }
            return null;
        }
    }


    /*
     * FRAGMENTS
     */

    public static class MainFragment extends Fragment implements View.OnClickListener {

        private View mRootView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_main, container, false);

            loadDetail();

            return mRootView;
        }

        private void loadDetail() {
            setMainGeneralInfo();
            setTypeInfo();
            setAbilityInfo();
            setGenderInfo();
            setStatInfo();
            setTrainingInfo();
            setAppearanceInfo();
            setMoreInfo();
        }

        private void setMainGeneralInfo() {
            if (PrefUtils.playPokemonCryAtStart(getContext())) {
                ActionUtils.playPokemonCry(getContext(), mPokemon);
            }

            TextView tvId = (TextView) mRootView.findViewById(R.id.detailM_tvPokedexId);
            TextView tvPkmnName = (TextView) mRootView.findViewById(R.id.detailM_tvTitle);
            TextView tvForm = (TextView) mRootView.findViewById(R.id.detailM_tvForm);
            TextView tvSpecies = (TextView) mRootView.findViewById(R.id.detailM_tvSpecies);ImageView imageView = (ImageView) mRootView.findViewById(R.id.detailM_imgvPokemon);

            tvId.setText("# " + mPokemon.getNationalIdFormatted());
            tvPkmnName.setText(mPkmnName);

            String form = mPokemon.getFormFormatted();
            if (form.equals("")) {
                tvForm.setVisibility(View.GONE);
            } else {
                tvForm.setVisibility(View.VISIBLE);
                tvForm.setText(form);
            }

            tvSpecies.setText(mPokemon.getSpecies() + " Pok" + "\u00E9" + "mon");

            mPokemon.setPokemonImage(imageView);
            imageView.setOnClickListener(this);
        }

        private void setTypeInfo() {
            TextView tvType1 = (TextView) mRootView.findViewById(R.id.detailM_tvType1);
            TextView tvType2 = (TextView) mRootView.findViewById(R.id.detailM_tvType2);

            String type1 = mPokemon.getType1();
            String type2 = mPokemon.getType2();
            if (mPokemon.hasSecondaryType()) {
                tvType1.setVisibility(View.VISIBLE);
                tvType2.setVisibility(View.GONE);
                tvType1.setText(type1);
                tvType1.setOnClickListener(this);
                tvType1.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type1));
                //tvType1.setTextColor(getResources().getColor(R.color.text_white));
                tvType1.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_white));
            } else {
                tvType1.setVisibility(View.VISIBLE);
                tvType2.setVisibility(View.VISIBLE);
                tvType1.setText(type1);
                tvType1.setOnClickListener(this);
                tvType2.setText(type2);
                tvType2.setOnClickListener(this);
                tvType1.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type1));
                tvType2.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type2));
                tvType1.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_white));
                tvType2.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_white));
            }
        }

        private void setAbilityInfo() {
            TextView tvAbility1 = (TextView) mRootView.findViewById(R.id.detailM_tvAbility1_info);
            TextView tvAbility2 = (TextView) mRootView.findViewById(R.id.detailM_tvAbility2_info);
            TextView tvAbilityH = (TextView) mRootView.findViewById(R.id.detailM_tvAbilityH_info);

            tvAbility1.setText(mPokemon.getAbility1());
            tvAbility2.setText(mPokemon.getAbility2());
            tvAbilityH.setText(mPokemon.getAbilityH());

            LinearLayout llAbility2 = (LinearLayout) mRootView.findViewById(R.id.detailM_llAbility2);
            LinearLayout llAbilityH = (LinearLayout) mRootView.findViewById(R.id.detailM_llAbilityH);
            TextView tvAbility1Title = (TextView) mRootView.findViewById(R.id.detailM_tvAbility1_title);

            if (mPokemon.hasSecondaryAbility()) {
                llAbility2.setVisibility(View.GONE);
                tvAbility1Title.setText(R.string.header_ability);
                tvAbility1.setOnClickListener(this);
            } else {
                llAbility2.setVisibility(View.VISIBLE);
                tvAbility1Title.setText(R.string.header_ability_1);
                tvAbility1.setOnClickListener(this);
                tvAbility2.setOnClickListener(this);
            }
            if (mPokemon.hasHiddenAbility()) {
                llAbilityH.setVisibility(View.GONE);
            } else {
                llAbilityH.setVisibility(View.VISIBLE);
                tvAbilityH.setOnClickListener(this);
            }
        }

        private void setGenderInfo() {
            ProgressBar genderBar = (ProgressBar) mRootView.findViewById(R.id.detailM_barGender);
            TextView tvGenderMale = (TextView) mRootView.findViewById(R.id.detailM_tvGenderMale);
            TextView tvGenderFemale = (TextView) mRootView.findViewById(R.id.detailM_tvGenderFemale);

            double genderMale = mPokemon.getGenderMale();
            double genderFemale = mPokemon.getGenderFemale();

            genderBar.setProgress((int) genderMale);
            genderBar.setMax(100);

            if (genderMale == 0 && genderFemale == 0) {
                tvGenderMale.setVisibility(View.VISIBLE);
                tvGenderFemale.setVisibility(View.GONE);
                tvGenderMale.setText(mPkmnName + " is genderless");
                tvGenderMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_black_secondary));
                genderBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_gender_neutral));
            } else {
                tvGenderMale.setVisibility(View.VISIBLE);
                tvGenderFemale.setVisibility(View.VISIBLE);
                tvGenderMale.setText(Html.fromHtml("<i><b>Male: </b>" + genderMale + "%</i>"));
                tvGenderFemale.setText(Html.fromHtml("<i><b>Female: </b>" + genderFemale + "%</i>"));
                tvGenderMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.progress_gender_male));
                tvGenderFemale.setTextColor(ContextCompat.getColor(getActivity(), R.color.progress_gender_female));
                genderBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_gender));
            }
        }

        private void setStatInfo() {
            ProgressBar statBarHp = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatHP);
            ProgressBar statBarAtk = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatAtk);
            ProgressBar statBarDef = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatDef);
            ProgressBar statBarSpA = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatSpA);
            ProgressBar statBarSpD = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatSpD);
            ProgressBar statBarSpe = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatSpe);
            ProgressBar statBarTotal = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatTotal);
            ProgressBar statBarAvg = (ProgressBar) mRootView.findViewById(R.id.detailM_barStatAvg);

            TextView tvStatHp = (TextView) mRootView.findViewById(R.id.detailM_tvStatHP_number);
            TextView tvStatAtk = (TextView) mRootView.findViewById(R.id.detailM_tvStatAtk_number);
            TextView tvStatDef = (TextView) mRootView.findViewById(R.id.detailM_tvStatDef_number);
            TextView tvStatSpA = (TextView) mRootView.findViewById(R.id.detailM_tvStatSpA_number);
            TextView tvStatSpd = (TextView) mRootView.findViewById(R.id.detailM_tvStatSpD_number);
            TextView tvStatSpe = (TextView) mRootView.findViewById(R.id.detailM_tvStatSpe_number);
            TextView tvStatTotal = (TextView) mRootView.findViewById(R.id.detailM_tvStatTotal_number);
            TextView tvStatAvg = (TextView) mRootView.findViewById(R.id.detailM_tvStatAvg_number);

            int maxHp = 255;
            int maxAtks = 200;
            int maxDefs = 230;
            int maxSpeed = 200;
            int maxTotal = 800;
            int maxAvg = 150;

            int statHp = mPokemon.getStatHP();
            int statAtk = mPokemon.getStatAtk();
            int statDef = mPokemon.getStatDef();
            int statSpA = mPokemon.getStatSpA();
            int statSpD = mPokemon.getStatSpD();
            int statSpe = mPokemon.getStatSpe();
            int statTotal = mPokemon.getStatTotal();
            int statAvg = mPokemon.getStatAvg();

            statBarHp.setMax(maxHp);
            statBarHp.setProgress(statHp);
            statBarAtk.setMax(maxAtks);
            statBarAtk.setProgress(statAtk);
            statBarDef.setMax(maxDefs);
            statBarDef.setProgress(statDef);
            statBarSpA.setMax(maxAtks);
            statBarSpA.setProgress(statSpA);
            statBarSpD.setMax(maxDefs);
            statBarSpD.setProgress(statSpD);
            statBarSpe.setMax(maxSpeed);
            statBarSpe.setProgress(statSpe);
            statBarTotal.setMax(maxTotal);
            statBarTotal.setProgress(statTotal);
            statBarAvg.setMax(maxAvg);
            statBarAvg.setProgress(statAvg);

            tvStatHp.setText(String.valueOf(statHp));
            tvStatAtk.setText(String.valueOf(statAtk));
            tvStatDef.setText(String.valueOf(statDef));
            tvStatSpA.setText(String.valueOf(statSpA));
            tvStatSpd.setText(String.valueOf(statSpD));
            tvStatSpe.setText(String.valueOf(statSpe));
            tvStatTotal.setText(String.valueOf(statTotal));
            tvStatAvg.setText(String.valueOf(statAvg));
        }

        private void setTrainingInfo() {
            TextView tvCatchRate = (TextView) mRootView.findViewById(R.id.detailM_tvCatchrate_info);
            TextView tvHappiness = (TextView) mRootView.findViewById(R.id.detailM_tvHappiness_info);
            TextView tvLevellingRate = (TextView) mRootView.findViewById(R.id.detailM_tvLevellingrate_info);
            TextView tvExpGrowth = (TextView) mRootView.findViewById(R.id.detailM_tvExpgrowth_info);

            tvCatchRate.setText(String.valueOf(mPokemon.getCatchRate()));
            tvCatchRate.setOnClickListener(this);
            tvHappiness.setText(String.valueOf(mPokemon.getHappiness()));
            tvHappiness.setOnClickListener(this);
            tvLevellingRate.setText(InfoUtils.getGrowthFromAbbreviation(mPokemon.getLevellingRate()));
            tvLevellingRate.setOnClickListener(this);
            tvExpGrowth.setText(String.valueOf(mPokemon.getExpGrowth()));
            tvExpGrowth.setOnClickListener(this);

            Button btnCalcExp = (Button) mRootView.findViewById(R.id.detailM_btnCalcExp);
            btnCalcExp.setOnClickListener(this);
        }

        private void setAppearanceInfo() {
            TextView tvHeight = (TextView) mRootView.findViewById(R.id.detailM_tvHeight);
            TextView tvMass = (TextView) mRootView.findViewById(R.id.detailM_tvMass);
            TextView tvColor = (TextView) mRootView.findViewById(R.id.detailM_tvColor_info);
            TextView tvShape = (TextView) mRootView.findViewById(R.id.detailM_tvShape_info);

            tvColor.setText(mPokemon.getColour());
            tvColor.setOnClickListener(this);

            tvShape.setText(mPokemon.getShape(true) + " (" + mPokemon.getShape(false) + ")");
            tvShape.setOnClickListener(this);

            tvHeight.setText(mPokemon.getHeight() + " m");
            tvHeight.setOnClickListener(this);

            tvMass.setText(mPokemon.getMass() + " kg");
            tvMass.setOnClickListener(this);
        }

        private void setMoreInfo() {
            TextView tvGeneration = (TextView) mRootView.findViewById(R.id.detailM_tvGeneration_info);
            TextView tvHabitat = (TextView) mRootView.findViewById(R.id.detailM_tvHabitat_info);
            TextView tvEggSteps = (TextView) mRootView.findViewById(R.id.detailM_tvBaseeggsteps_info);
            TextView tvEggCycles = (TextView) mRootView.findViewById(R.id.detailM_tvBaseeggcycles_info);

            tvGeneration.setText(InfoUtils.getRomanFromGen(mPokemon.getGeneration()));
            tvGeneration.setOnClickListener(this);
            tvEggSteps.setText(String.valueOf(mPokemon.getBaseEggSteps()));
            tvEggSteps.setOnClickListener(this);
            tvEggCycles.setText(String.valueOf(mPokemon.getBaseEggCycles()));
            tvEggCycles.setOnClickListener(this);

            String habitat = mPokemon.getHabitat();
            if (habitat == null) {
                mRootView.findViewById(R.id.detailM_llHabitat)
                        .setVisibility(View.GONE);
            } else {
                tvHabitat.setText(mPokemon.getHabitat());
                tvHabitat.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.detailM_tvAbility1_info:
                case R.id.detailM_tvAbility2_info:
                case R.id.detailM_tvAbilityH_info:
                    TextView textView = (TextView) v;
                    String abilityName = textView.getText().toString();
                    Intent abilityIntent = new Intent(getActivity(), AbilityDetailActivity.class);
                    abilityIntent.putExtra("ABILITY", new MiniAbility(getContext(), abilityName));
                    startActivity(abilityIntent);
                    break;
                case R.id.detailM_imgvPokemon:
                    Intent imgDetailIntent = new Intent(getActivity(), PkmnImageDetailActivity.class);
                    imgDetailIntent.putExtra("POKEMON", mPokemon.toMiniPokemon());
                    startActivity(imgDetailIntent);
                    break;
                case R.id.detailM_tvType1:
                case R.id.detailM_tvType2:
                    Intent typeDetailIntent = new Intent(getActivity(), PkmnTypeDetailActivity.class);
                    typeDetailIntent.putExtra("POKEMON", mPokemon.toMiniPokemon());
                    startActivity(typeDetailIntent);
                    break;
                case R.id.detailM_tvCatchrate_info:
                    Intent catchRateIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    catchRateIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_CATCH_RATE);
                    catchRateIntent.putExtra("VALUE", String.valueOf(mPokemon.getCatchRate()));
                    startActivity(catchRateIntent);
                    break;
                case R.id.detailM_tvHappiness_info:
                    Intent happinessIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    happinessIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_HAPPINESS);
                    happinessIntent.putExtra("VALUE", String.valueOf(mPokemon.getHappiness()));
                    startActivity(happinessIntent);
                    break;
                case R.id.detailM_tvLevellingrate_info:
                    Intent levellingIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    levellingIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_LEVELLING_RATE);
                    levellingIntent.putExtra("VALUE", mPokemon.getLevellingRate());
                    startActivity(levellingIntent);
                    break;
                case R.id.detailM_tvExpgrowth_info:
                    Intent expIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    expIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_EXP);
                    expIntent.putExtra("VALUE", String.valueOf(mPokemon.getExpGrowth()));
                    startActivity(expIntent);
                    break;
                case R.id.detailM_btnCalcExp:
                    goToExperienceCalculator(getActivity());
                    break;
                case R.id.detailM_tvMass:
                    Intent massIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    massIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_MASS);
                    massIntent.putExtra("VALUE", String.valueOf(mPokemon.getMass()));
                    startActivity(massIntent);
                    break;
                case R.id.detailM_tvHeight:
                    Intent heightIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    heightIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_HEIGHT);
                    heightIntent.putExtra("VALUE", String.valueOf(mPokemon.getHeight()));
                    startActivity(heightIntent);
                    break;
                case R.id.detailM_tvGeneration_info:
                    Intent generationIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    generationIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_GENERATION);
                    generationIntent.putExtra("VALUE", InfoUtils.getRomanFromGen(mPokemon.getGeneration()));
                    startActivity(generationIntent);
                    break;
                case R.id.detailM_tvColor_info:
                    Intent colorIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    colorIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_COLOUR);
                    colorIntent.putExtra("VALUE", mPokemon.getColour());
                    startActivity(colorIntent);
                    break;
                case R.id.detailM_tvShape_info:
                    Intent shapeIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    shapeIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_SHAPE);
                    shapeIntent.putExtra("VALUE", mPokemon.getShape(false));
                    startActivity(shapeIntent);
                    break;
                case R.id.detailM_tvHabitat_info:
                    Intent habitatIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    habitatIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_HABITAT);
                    habitatIntent.putExtra("VALUE", mPokemon.getHabitat());
                    startActivity(habitatIntent);
                    break;
                case R.id.detailM_tvBaseeggsteps_info:
                    Intent eggStepsIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    eggStepsIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_EGG_STEPS);
                    eggStepsIntent.putExtra("VALUE", String.valueOf(mPokemon.getBaseEggSteps()));
                    startActivity(eggStepsIntent);
                    break;
                case R.id.detailM_tvBaseeggcycles_info:
                    Intent eggCyclesIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    eggCyclesIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_EGG_CYCLES);
                    eggCyclesIntent.putExtra("VALUE", String.valueOf(mPokemon.getBaseEggCycles()));
                    startActivity(eggCyclesIntent);
                    break;
            }
        }
    }

    public static class EvolutionsFragment extends Fragment {

        private View mRootView;

        // Todo: Evolutions list

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_evolutions, container, false);

            setupFormsList();

            return mRootView;
        }

        private void setupFormsList() {
            LinearLayout container = (LinearLayout) mRootView.findViewById(R.id.detailF_llFormsContainer);

            ArrayList<MiniPokemon> miniPokemonAltForms = mPokemon.getAlternateForms(true);
            final ArrayList<String> alternateForms = getFormList(miniPokemonAltForms);

            if (alternateForms.isEmpty()) {
                container.removeAllViews();
                getActivity().getLayoutInflater().inflate(R.layout.list_item_null, container, true);
                String listMessage = getResources().getString(R.string.null_alternate_forms, mPkmnName);
                TextView tvListTxt = (TextView) mRootView.findViewById(R.id.item_null_text1);
                tvListTxt.setText(listMessage);
            } else {
                FormsVGAdapter adapter = new FormsVGAdapter(getActivity(), container, mPokemon.toMiniPokemon(), alternateForms);
                adapter.createListItems();
                adapter.setOnEntryClickListener(new FormsVGAdapter.OnEntryClickListener() {
                    @Override
                    public void onEntryClick(View view, int position) {
                        String selectedForm = alternateForms.get(position);
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("POKEMON", new MiniPokemon(mPkmnId, mPkmnName, selectedForm));
                        startActivity(intent);
                    }
                });
            }
        }

        private ArrayList<String> getFormList(ArrayList<MiniPokemon> alternateForms) {
            ArrayList<String> altFormTexts = new ArrayList<>();
            for (int i = 0; i < alternateForms.size(); i++) {
                altFormTexts.add(alternateForms.get(i).getForm());
            }
            return altFormTexts;
        }
    }

    public static class MovesFragment extends Fragment implements LabelledSpinner.OnItemChosenListener {

        private View mRootView;

        private String mLearnMethod, mGameVersion;

        private LinearLayout mContainer;
        private Button mSubmitButton;
        private LabelledSpinner mSpinnerMethod, mSpinnerGame;

        private ArrayList<String> mArrayMethodTitles = new ArrayList<>();
        private ArrayList<Integer> mArrayMethodTypes = new ArrayList<>();
        private ArrayList<String> mArrayGameTitles;
        private ArrayList<Integer> mArrayGameTypes = new ArrayList<>();

        private AsyncTask<Void, Integer, Void> mAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_detail_learnsets, container, false);

            Resources res = getActivity().getResources();
            mArrayMethodTitles.add(res.getString(R.string.header_moves_levelup));
            mArrayMethodTitles.add(res.getString(R.string.header_moves_machine));
            mArrayMethodTitles.add(res.getString(R.string.header_moves_tutor));
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_LEVEL_UP);
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_MACHINE);
            mArrayMethodTypes.add(AppConfig.LEARN_METHOD_TUTOR);

            mArrayGameTitles = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.game_versions)));
            mArrayGameTypes.add(AppConfig.GAME_VERSION_RED_BLUE);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_YELLOW);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_GOLD_SILVER);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_CRYSTAL);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_RUBY_SAPPHIRE);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_EMERALD);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_FIRERED_LEAFGREEN);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_DIAMOND_PEARL);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_PLATINUM);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_HEARTGOLD_SOULSILVER);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_BLACK_WHITE);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_BLACK2_WHITE2);
            mArrayGameTypes.add(AppConfig.GAME_VERSION_X_Y);

            mSpinnerMethod = (LabelledSpinner) mRootView.findViewById(R.id.detailL_spinnerMethod);
            mSpinnerMethod.setItemsArray(mArrayMethodTitles);
            mSpinnerMethod.setSelection(0);
            mSpinnerMethod.setOnItemChosenListener(this);
            mSpinnerGame = (LabelledSpinner) mRootView.findViewById(R.id.detailL_spinnerGame);
            mSpinnerGame.setItemsArray(R.array.game_versions);
            mSpinnerGame.setSelection(mArrayGameTitles.size() - 1);
            mSpinnerGame.setOnItemChosenListener(this);

            mContainer = (LinearLayout) mRootView.findViewById(R.id.detailL_llContainer);

            mSubmitButton = (Button) mRootView.findViewById(R.id.detailL_btnGo);
            mSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAsyncTask != null) {
                        mAsyncTask.cancel(true);
                    }
                    loadCard();
                }
            });

            return mRootView;
        }

        private void loadCard() {
            mContainer.removeAllViews();
            mContainer.addView(makeCard());
        }

        private View makeCard() {
            View card = getActivity().getLayoutInflater().inflate(R.layout.card_detail_learnset, mContainer, false);

            final TextView title = (TextView) card.findViewById(R.id.card_learnset_titleText);
            final TextView subtitle = (TextView) card.findViewById(R.id.card_learnset_subtitleText);
            final ProgressBar progressBar = (ProgressBar) card.findViewById(R.id.card_learnset_progressBar);
            final LinearLayout itemsContainer = (LinearLayout) card.findViewById(R.id.card_learnset_linearLayout);

            title.setText(mLearnMethod);
            subtitle.setText("Pok"+"\u00E9"+"mon " + mGameVersion);

            int methodNoInList = mArrayMethodTitles.indexOf(mLearnMethod);
            final int learnMethod = mArrayMethodTypes.get(methodNoInList);
            int gameNoInList = mArrayGameTitles.indexOf(mGameVersion);
            final int gameVersion = mArrayGameTypes.get(gameNoInList);

            if (learnMethod == AppConfig.LEARN_METHOD_LEVEL_UP) {
                mAsyncTask = new AsyncTask<Void, Integer, Void>() {
                    Learnset learnset;
                    LearnsetVGAdapter adapter;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (progressBar.getVisibility() != View.VISIBLE) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Note .getForm() for raw form rather than mPkmnForm
                        learnset = new Learnset(getActivity(), mPokemon.toMiniPokemon(), gameVersion, learnMethod);
                        adapter = new LearnsetVGAdapter(getActivity(), itemsContainer, learnset.getLevels(), learnset.getMoveNames());
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.createListItems();
                        progressBar.setVisibility(View.GONE);
                    }
                }.execute();
            } else {
                mAsyncTask = new AsyncTask<Void, Integer, Void>() {
                    Learnset learnset;
                    SimpleVGAdapter adapter;
                    ArrayList<String> arrayMoves;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (progressBar.getVisibility() != View.VISIBLE) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    protected Void doInBackground(Void... params) {
                        learnset = new Learnset(getActivity(), mPokemon.toMiniPokemon(), gameVersion, learnMethod);
                        arrayMoves = learnset.getMoveNames();
                        adapter = new SimpleVGAdapter(getActivity(), itemsContainer, arrayMoves);
                        adapter.setOnEntryClickListener(new SimpleVGAdapter.OnEntryClickListener() {
                            @Override
                            public void onEntryClick(View view, int position) {
                                String move = arrayMoves.get(position);
                                Intent intent = new Intent(getActivity(), MovesDetailActivity.class);
                                intent.putExtra("MOVE", new MiniMove(getContext(), move));
                                startActivity(intent);
                            }
                        });
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.createListItems();
                        progressBar.setVisibility(View.GONE);
                    }
                }.execute();
            }

            return card;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
        }

        @Override
        public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
            String selected = adapterView.getItemAtPosition(position).toString();
            switch (labelledSpinner.getId()) {
                case R.id.detailL_spinnerMethod:
                    mLearnMethod = selected;
                    break;
                case R.id.detailL_spinnerGame:
                    mGameVersion = selected;
                    break;
            }
        }

        @Override
        public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
    }
}
