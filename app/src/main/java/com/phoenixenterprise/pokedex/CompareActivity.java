package com.phoenixenterprise.pokedex;

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
import com.phoenixenterprise.pokedex.adapter.LearnsetVGAdapter;
import com.phoenixenterprise.pokedex.adapter.SimpleVGAdapter;
import com.phoenixenterprise.pokedex.dialog.AbilityDetailActivity;
import com.phoenixenterprise.pokedex.dialog.MovesDetailActivity;
import com.phoenixenterprise.pokedex.dialog.PkmnImageDetailActivity;
import com.phoenixenterprise.pokedex.dialog.PkmnTypeDetailActivity;
import com.phoenixenterprise.pokedex.dialog.PropertyDetailActivity;
import com.phoenixenterprise.pokedex.object.Learnset;
import com.phoenixenterprise.pokedex.object.MiniAbility;
import com.phoenixenterprise.pokedex.object.MiniMove;
import com.phoenixenterprise.pokedex.object.MiniPokemon;
import com.phoenixenterprise.pokedex.object.Pokemon;
import com.phoenixenterprise.pokedex.util.ActionUtils;
import com.phoenixenterprise.pokedex.util.AdUtils;
import com.phoenixenterprise.pokedex.util.AppConfig;
import com.phoenixenterprise.pokedex.util.InfoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class CompareActivity extends AppCompatActivity {

    private static Pokemon mPokemon1, mPokemon2;
    private static String mPkmnName1, mPkmnName2, mPkmnForm1, mPkmnForm2;
    private String mPkmnFullName1, mPkmnFullName2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AdUtils.setupAds(this, R.id.detail_adView);

        Bundle extras = getIntent().getExtras();
        MiniPokemon miniPokemon1 = extras.getParcelable("POKEMON_1");
        MiniPokemon miniPokemon2 = extras.getParcelable("POKEMON_2");

        if (miniPokemon1 == null || miniPokemon2 == null) {
            throw new NullPointerException("Parcelable MiniPokemon object(s) through Intent is null");
        }

        mPokemon1 = miniPokemon1.toPokemon(this);
        mPokemon2 = miniPokemon2.toPokemon(this);

        mPkmnName1 = mPokemon1.getPokemon();
        mPkmnName2 = mPokemon2.getPokemon();
        mPkmnForm1 = mPokemon1.getFormFormatted();
        mPkmnForm2 = mPokemon2.getFormFormatted();

        mPkmnFullName1 = InfoUtils.getNameAndForm(mPkmnName1, miniPokemon1.getForm());
        mPkmnFullName2 = InfoUtils.getNameAndForm(mPkmnName2, miniPokemon2.getForm());

        ViewPager viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.text_white_secondary),
                ContextCompat.getColor(this, R.color.text_white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        getSupportActionBar().setSubtitle(mPkmnFullName1 + " vs. " + mPkmnFullName2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compare, menu);

        menu.findItem(R.id.action_calculate_experience_pkmn1)
                .setTitle(getResources().getString(R.string.action_calculate_experience_of, mPkmnFullName1));
        menu.findItem(R.id.action_calculate_experience_pkmn2)
                .setTitle(getResources().getString(R.string.action_calculate_experience_of, mPkmnFullName2));

        menu.findItem(R.id.action_play_cry_pkmn1)
                .setTitle(getResources().getString(R.string.action_play_cry_of, mPkmnFullName1));
        menu.findItem(R.id.action_play_cry_pkmn2)
                .setTitle(getResources().getString(R.string.action_play_cry_of, mPkmnFullName2));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculate_experience_pkmn1:
                Intent expCalcIntent1 = new Intent(this, ExperienceCalculatorActivity.class);
                expCalcIntent1.putExtra("POKEMON_NAME", mPkmnName1);
                startActivity(expCalcIntent1);
                break;
            case R.id.action_calculate_experience_pkmn2:
                Intent expCalcIntent2 = new Intent(this, ExperienceCalculatorActivity.class);
                expCalcIntent2.putExtra("POKEMON_NAME", mPkmnName2);
                startActivity(expCalcIntent2);
                break;
            case R.id.action_play_cry_pkmn1:
                ActionUtils.playPokemonCry(this, mPokemon1);
                break;
            case R.id.action_play_cry_pkmn2:
                ActionUtils.playPokemonCry(this, mPokemon2);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
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
                    return getString(R.string.tab_pkmn_detail_moves).toUpperCase(l);
            }
            return null;
        }
    }

    public static class MainFragment extends Fragment implements View.OnClickListener {

        private View mRootView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_compare_main, container, false);

            setupGeneralInformation();
            setAbilityDetails();
            setAppearanceDetails();
            setGenderDetails();
            setupStatistics();
            setupTraining();
            setMoreInformation();

            return mRootView;
        }

        private void setupGeneralInformation() {
            ImageView imageView1 = (ImageView) mRootView.findViewById(R.id.compare_imgvPokemon_1);
            ImageView imageView2 = (ImageView) mRootView.findViewById(R.id.compare_imgvPokemon_2);
            mPokemon1.setPokemonImage(imageView1);
            mPokemon2.setPokemonImage(imageView2);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);

            TextView id1 = (TextView) mRootView.findViewById(R.id.compare_tvPokedexID_1);
            TextView id2 = (TextView) mRootView.findViewById(R.id.compare_tvPokedexID_2);
            TextView name1 = (TextView) mRootView.findViewById(R.id.compare_tvTitle_1);
            TextView name2 = (TextView) mRootView.findViewById(R.id.compare_tvTitle_2);
            id1.setText("# " + mPokemon1.getNationalIdFormatted());
            id2.setText("# " + mPokemon2.getNationalIdFormatted());
            name1.setText(mPkmnName1);
            name2.setText(mPkmnName2);

            TextView form1 = (TextView) mRootView.findViewById(R.id.compare_tvForm_1);
            TextView form2 = (TextView) mRootView.findViewById(R.id.compare_tvForm_2);
            if (mPkmnForm1.equals("") && mPkmnForm2.equals("")) {
                form1.setVisibility(View.GONE);
                form2.setVisibility(View.GONE);
            } else {
                form1.setVisibility(View.VISIBLE);
                form2.setVisibility(View.VISIBLE);
                if (!mPkmnForm1.equals("")) {
                    form1.setText("(" + mPkmnForm1 + ")");
                } else {
                    form1.setText("");
                }
                if (!mPkmnForm2.equals("")) {
                    form2.setText("(" + mPkmnForm2 + ")");
                } else {
                    form2.setText("");
                }
            }

        /* TYPES */
            TextView typePrimary1 = (TextView) mRootView.findViewById(R.id.compare_tvType1_1);
            TextView typeSecondary1 = (TextView) mRootView.findViewById(R.id.compare_tvType2_1);
            TextView typePrimary2 = (TextView) mRootView.findViewById(R.id.compare_tvType1_2);
            TextView typeSecondary2 = (TextView) mRootView.findViewById(R.id.compare_tvType2_2);

            setupPkmnTypeFormatting(mPokemon1.getType1(), mPokemon1.getType2(), typePrimary1, typeSecondary1);
            setupPkmnTypeFormatting(mPokemon2.getType1(), mPokemon2.getType2(), typePrimary2, typeSecondary2);
            typePrimary1.setOnClickListener(this);
            typeSecondary1.setOnClickListener(this);
            typePrimary2.setOnClickListener(this);
            typeSecondary2.setOnClickListener(this);

        /* CLASSIFICATIONS */
            TextView classification1 = (TextView) mRootView.findViewById(R.id.compare_tvClassification_1);
            TextView classification2 = (TextView) mRootView.findViewById(R.id.compare_tvClassification_2);
            classification1.setText(mPokemon1.getSpecies() + " Pok" + "\u00E9" + "mon");
            classification2.setText(mPokemon2.getSpecies() + " Pok" + "\u00E9" + "mon");
        }

        private void setupPkmnTypeFormatting(String type1, String type2, TextView tv_type1, TextView tv_type2) {
            if (type2.equals("")) {
                tv_type2.setVisibility(View.GONE);
                tv_type1.setText(type1);
                tv_type1.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type1));
                tv_type1.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_white));
            } else {
                tv_type2.setVisibility(View.VISIBLE);
                tv_type1.setText(type1);
                tv_type1.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type1));
                tv_type1.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_white));
                tv_type2.setText(type2);
                tv_type2.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(type2));
                tv_type2.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_white));
            }
        }

        private void setAbilityDetails() { // TODO: Optimise the code here
            TextView abilityPrimary1 = (TextView) mRootView.findViewById(R.id.compare_tvAbility1_1);
            TextView abilitySecondary1 = (TextView) mRootView.findViewById(R.id.compare_tvAbility2_1);
            TextView abilityHidden1 = (TextView) mRootView.findViewById(R.id.compare_tvAbilityH_1);
            TextView abilityPrimary2 = (TextView) mRootView.findViewById(R.id.compare_tvAbility1_2);
            TextView abilitySecondary2 = (TextView) mRootView.findViewById(R.id.compare_tvAbility2_2);
            TextView abilityHidden2 = (TextView) mRootView.findViewById(R.id.compare_tvAbilityH_2);

            if (mPokemon1.getAbility2().equals("") && mPokemon2.getAbility2().equals("")) {
                ((TextView) mRootView.findViewById(R.id.compare_tvHeaderAbility1)).setText(getString(R.string.header_ability));
                mRootView.findViewById(R.id.compare_tvHeaderAbility2).setVisibility(View.GONE);
                mRootView.findViewById(R.id.compare_llAbility2).setVisibility(View.GONE);

                abilityPrimary1.setText(mPokemon1.getAbility1());
                abilityPrimary2.setText(mPokemon2.getAbility1());
                abilityPrimary1.setOnClickListener(this);
                abilityPrimary2.setOnClickListener(this);
            } else {
                // There is at least one 'Ability 2'
                abilityPrimary1.setText(mPokemon1.getAbility1());
                abilityPrimary2.setText(mPokemon2.getAbility1());
                abilityPrimary1.setOnClickListener(this);
                abilityPrimary2.setOnClickListener(this);

                if (mPokemon1.getAbility2().equals("")) {
                    abilitySecondary1.setText("-");
                } else {
                    abilitySecondary1.setText(mPokemon1.getAbility2());
                    abilitySecondary1.setOnClickListener(this);
                }
                if (mPokemon2.getAbility2().equals("")) {
                    abilitySecondary2.setText("-");
                } else {
                    abilitySecondary2.setText(mPokemon2.getAbility2());
                    abilitySecondary2.setOnClickListener(this);
                }
            }

            if (mPokemon1.getAbilityH().equals("") && mPokemon2.getAbilityH().equals("")) {
                mRootView.findViewById(R.id.compare_tvHeaderAbilityH).setVisibility(View.GONE);
                mRootView.findViewById(R.id.compare_llAbilityH).setVisibility(View.GONE);
            } else {
                if (mPokemon1.getAbilityH().equals("")) {
                    abilityHidden1.setText("-");
                } else {
                    abilityHidden1.setText(mPokemon1.getAbilityH());
                    abilityHidden1.setOnClickListener(this);
                }
                if (mPokemon2.getAbilityH().equals("")) {
                    abilityHidden2.setText("-");
                } else {
                    abilityHidden2.setText(mPokemon2.getAbilityH());
                    abilityHidden2.setOnClickListener(this);
                }
            }
        }

        private void setAppearanceDetails() {
            TextView height1 = (TextView) mRootView.findViewById(R.id.compare_tvHeight_1);
            TextView height2 = (TextView) mRootView.findViewById(R.id.compare_tvHeight_2);
            TextView mass1 = (TextView) mRootView.findViewById(R.id.compare_tvMass_1);
            TextView mass2 = (TextView) mRootView.findViewById(R.id.compare_tvMass_2);
            TextView color1 = (TextView) mRootView.findViewById(R.id.compare_tvColor_1);
            TextView color2 = (TextView) mRootView.findViewById(R.id.compare_tvColor_2);
            TextView shape1 = (TextView) mRootView.findViewById(R.id.compare_tvShape_1);
            TextView shape2 = (TextView) mRootView.findViewById(R.id.compare_tvShape_2);

            height1.setText(mPokemon1.getHeight() + " m");
            height2.setText(mPokemon2.getHeight() + " m");
            mass1.setText(mPokemon1.getMass() + " kg");
            mass2.setText(mPokemon2.getMass() + " kg");
            color1.setText(mPokemon1.getColour());
            color2.setText(mPokemon2.getColour());
            shape1.setText(mPokemon1.getShape(true) + " (" + mPokemon1.getShape(false) + ")");
            shape2.setText(mPokemon2.getShape(true) + " (" + mPokemon2.getShape(false) + ")");

            height1.setOnClickListener(this);
            height2.setOnClickListener(this);
            mass1.setOnClickListener(this);
            mass2.setOnClickListener(this);
            color1.setOnClickListener(this);
            color2.setOnClickListener(this);
            shape1.setOnClickListener(this);
            shape2.setOnClickListener(this);
        }

        private void setGenderDetails() {
            ProgressBar genderBar1 = (ProgressBar) mRootView.findViewById(R.id.compare_barGender_1);
            ProgressBar genderBar2 = (ProgressBar) mRootView.findViewById(R.id.compare_barGender_2);
            TextView genderTxtM1 = (TextView) mRootView.findViewById(R.id.compare_tvGenderMale_1);
            TextView genderTxtF1 = (TextView) mRootView.findViewById(R.id.compare_tvGenderFemale_1);
            TextView genderTxtM2 = (TextView) mRootView.findViewById(R.id.compare_tvGenderMale_2);
            TextView genderTxtF2 = (TextView) mRootView.findViewById(R.id.compare_tvGenderFemale_2);
            genderBar1.setProgress((int) mPokemon1.getGenderMale());
            genderBar1.setMax(100);
            genderBar2.setProgress((int) mPokemon2.getGenderMale());
            genderBar2.setMax(100);
            setupGenderFormatting(mPokemon1.getGenderMale(), mPokemon1.getGenderFemale(), genderTxtM1, genderTxtF1, genderBar1, mPkmnName1);
            setupGenderFormatting(mPokemon2.getGenderMale(), mPokemon2.getGenderFemale(), genderTxtM2, genderTxtF2, genderBar2, mPkmnName2);
        }

        private void setupGenderFormatting(double genderMale, double genderFemale, TextView tv_genderMale, TextView tv_genderFemale, ProgressBar genderBar, String pokemon) {
            if (genderMale == 0 && genderFemale == 0) {
                tv_genderMale.setVisibility(View.VISIBLE);
                tv_genderFemale.setVisibility(View.GONE);
                tv_genderMale.setText(pokemon + " is genderless");
                tv_genderMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_black_secondary));
                genderBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_gender_neutral));
            } else {
                tv_genderMale.setVisibility(View.VISIBLE);
                tv_genderFemale.setVisibility(View.VISIBLE);
                tv_genderMale.setText(Html.fromHtml("<i><b>Male: </b>" + genderMale + "%</i>"));
                tv_genderFemale.setText(Html.fromHtml("<i><b>Female: </b>" + genderFemale + "%</i>"));
                tv_genderMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.progress_gender_male));
                tv_genderFemale.setTextColor(ContextCompat.getColor(getActivity(), R.color.progress_gender_female));
                genderBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_gender));
            }
        }

        private void setupStatistics() {
            int progressMax_HP = 255, progressMax_atks = 200, progressMax_defs = 230,
                    progressMax_speed = 200, progressMax_total = 800, progressMax_avg = 150;

            ProgressBar statBarHP1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatHP_1);
            ProgressBar statBarHP2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatHP_2);
            ProgressBar statBarAtk1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatAtk_1);
            ProgressBar statBarAtk2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatAtk_2);
            ProgressBar statBarDef1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatDef_1);
            ProgressBar statBarDef2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatDef_2);
            ProgressBar statBarSpA1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatSpA_1);
            ProgressBar statBarSpA2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatSpA_2);
            ProgressBar statBarSpD1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatSpD_1);
            ProgressBar statBarSpD2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatSpD_2);
            ProgressBar statBarSpe1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatSpe_1);
            ProgressBar statBarSpe2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatSpe_2);
            ProgressBar statBarTotal1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatTotal_1);
            ProgressBar statBarTotal2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatTotal_2);
            ProgressBar statBarAvg1 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatAvg_1);
            ProgressBar statBarAvg2 = (ProgressBar) mRootView.findViewById(R.id.compare_barStatAvg_2);

            TextView statHP1 = (TextView) mRootView.findViewById(R.id.compare_tvStatHP_1);
            TextView statHP2 = (TextView) mRootView.findViewById(R.id.compare_tvStatHP_2);
            TextView statAtk1 = (TextView) mRootView.findViewById(R.id.compare_tvStatAtk_1);
            TextView statAtk2 = (TextView) mRootView.findViewById(R.id.compare_tvStatAtk_2);
            TextView statDef1 = (TextView) mRootView.findViewById(R.id.compare_tvStatDef_1);
            TextView statDef2 = (TextView) mRootView.findViewById(R.id.compare_tvStatDef_2);
            TextView statSpA1 = (TextView) mRootView.findViewById(R.id.compare_tvStatSpA_1);
            TextView statSpA2 = (TextView) mRootView.findViewById(R.id.compare_tvStatSpA_2);
            TextView statSpD1 = (TextView) mRootView.findViewById(R.id.compare_tvStatSpD_1);
            TextView statSpD2 = (TextView) mRootView.findViewById(R.id.compare_tvStatSpD_2);
            TextView statSpe1 = (TextView) mRootView.findViewById(R.id.compare_tvStatSpe_1);
            TextView statSpe2 = (TextView) mRootView.findViewById(R.id.compare_tvStatSpe_2);
            TextView statTotal1 = (TextView) mRootView.findViewById(R.id.compare_tvStatTotal_1);
            TextView statTotal2 = (TextView) mRootView.findViewById(R.id.compare_tvStatTotal_2);
            TextView statAvg1 = (TextView) mRootView.findViewById(R.id.compare_tvStatAvg_1);
            TextView statAvg2 = (TextView) mRootView.findViewById(R.id.compare_tvStatAvg_2);

            statBarHP1.setMax(progressMax_HP);
            statBarHP1.setProgress(mPokemon1.getStatHP());
            statBarHP2.setMax(progressMax_HP);
            statBarHP2.setProgress(mPokemon2.getStatHP());
            statBarAtk1.setMax(progressMax_atks);
            statBarAtk1.setProgress(mPokemon1.getStatAtk());
            statBarAtk2.setMax(progressMax_atks);
            statBarAtk2.setProgress(mPokemon2.getStatAtk());
            statBarDef1.setMax(progressMax_defs);
            statBarDef1.setProgress(mPokemon1.getStatDef());
            statBarDef2.setMax(progressMax_defs);
            statBarDef2.setProgress(mPokemon2.getStatDef());
            statBarSpA1.setMax(progressMax_atks);
            statBarSpA1.setProgress(mPokemon1.getStatSpA());
            statBarSpA2.setMax(progressMax_atks);
            statBarSpA2.setProgress(mPokemon2.getStatSpD());
            statBarSpD1.setMax(progressMax_defs);
            statBarSpD1.setProgress(mPokemon1.getStatSpD());
            statBarSpD2.setMax(progressMax_defs);
            statBarSpD2.setProgress(mPokemon2.getStatSpD());
            statBarSpe1.setMax(progressMax_speed);
            statBarSpe1.setProgress(mPokemon1.getStatSpe());
            statBarSpe2.setMax(progressMax_speed);
            statBarSpe2.setProgress(mPokemon2.getStatSpe());
            statBarTotal1.setMax(progressMax_total);
            statBarTotal1.setProgress(mPokemon1.getStatTotal());
            statBarTotal2.setMax(progressMax_total);
            statBarTotal2.setProgress(mPokemon2.getStatTotal());
            statBarAvg1.setMax(progressMax_avg);
            statBarAvg1.setProgress(mPokemon1.getStatTotal() / 6);
            statBarAvg2.setMax(progressMax_avg);
            statBarAvg2.setProgress(mPokemon2.getStatTotal() / 6);

            statHP1.setText(String.valueOf(mPokemon1.getStatHP()));
            statHP2.setText(String.valueOf(mPokemon2.getStatHP()));
            statAtk1.setText(String.valueOf(mPokemon1.getStatAtk()));
            statAtk2.setText(String.valueOf(mPokemon2.getStatAtk()));
            statDef1.setText(String.valueOf(mPokemon1.getStatDef()));
            statDef2.setText(String.valueOf(mPokemon2.getStatDef()));
            statSpA1.setText(String.valueOf(mPokemon1.getStatSpA()));
            statSpA2.setText(String.valueOf(mPokemon2.getStatSpA()));
            statSpD1.setText(String.valueOf(mPokemon1.getStatSpD()));
            statSpD2.setText(String.valueOf(mPokemon2.getStatSpD()));
            statSpe1.setText(String.valueOf(mPokemon1.getStatSpe()));
            statSpe2.setText(String.valueOf(mPokemon2.getStatSpe()));
            statTotal1.setText(String.valueOf(mPokemon1.getStatTotal()));
            statTotal2.setText(String.valueOf(mPokemon2.getStatTotal()));
            statAvg1.setText(String.valueOf(mPokemon1.getStatTotal() / 6));
            statAvg2.setText(String.valueOf(mPokemon2.getStatTotal() / 6));
        }

        private void setupTraining() {
            TextView catchRate1 = (TextView) mRootView.findViewById(R.id.compare_tvCatchrate_1);
            TextView catchRate2 = (TextView) mRootView.findViewById(R.id.compare_tvCatchrate_2);
            TextView happiness1 = (TextView) mRootView.findViewById(R.id.compare_tvHappiness_1);
            TextView happiness2 = (TextView) mRootView.findViewById(R.id.compare_tvHappiness_2);
            TextView levellingRate1 = (TextView) mRootView.findViewById(R.id.compare_tvLevellingrate_1);
            TextView levellingRate2 = (TextView) mRootView.findViewById(R.id.compare_tvLevellingrate_2);
            TextView expGrowth1 = (TextView) mRootView.findViewById(R.id.compare_tvExpgrowth_1);
            TextView expGrowth2 = (TextView) mRootView.findViewById(R.id.compare_tvExpgrowth_2);

            catchRate1.setText(String.valueOf(mPokemon1.getCatchRate()));
            catchRate2.setText(String.valueOf(mPokemon2.getCatchRate()));
            happiness1.setText(String.valueOf(mPokemon1.getHappiness()));
            happiness2.setText(String.valueOf(mPokemon2.getHappiness()));
            levellingRate1.setText(InfoUtils.getGrowthFromAbbreviation(mPokemon1.getLevellingRate()));
            levellingRate2.setText(InfoUtils.getGrowthFromAbbreviation(mPokemon2.getLevellingRate()));
            expGrowth1.setText(String.valueOf(mPokemon1.getExpGrowth()));
            expGrowth2.setText(String.valueOf(mPokemon2.getExpGrowth()));

            catchRate1.setOnClickListener(this);
            catchRate2.setOnClickListener(this);
            happiness1.setOnClickListener(this);
            happiness2.setOnClickListener(this);
            levellingRate1.setOnClickListener(this);
            levellingRate2.setOnClickListener(this);
            expGrowth1.setOnClickListener(this);
            expGrowth2.setOnClickListener(this);
        }

        private void setMoreInformation() {
            TextView generation1 = (TextView) mRootView.findViewById(R.id.compare_tvGeneration_1);
            TextView generation2 = (TextView) mRootView.findViewById(R.id.compare_tvGeneration_2);
            TextView habitat1 = (TextView) mRootView.findViewById(R.id.compare_tvHabitat_1);
            TextView habitat2 = (TextView) mRootView.findViewById(R.id.compare_tvHabitat_2);
            TextView baseEggSteps1 = (TextView) mRootView.findViewById(R.id.compare_tvBaseeggsteps_1);
            TextView baseEggSteps2 = (TextView) mRootView.findViewById(R.id.compare_tvBaseeggsteps_2);
            TextView baseEggCycles1 = (TextView) mRootView.findViewById(R.id.compare_tvBaseeggcycles_1);
            TextView baseEggCycles2 = (TextView) mRootView.findViewById(R.id.compare_tvBaseeggcycles_2);

            generation1.setText(InfoUtils.getRomanFromGen(mPokemon1.getGeneration()));
            generation2.setText(InfoUtils.getRomanFromGen(mPokemon2.getGeneration()));

            generation1.setOnClickListener(this);
            generation2.setOnClickListener(this);

            int nullHabitatCounter = 0;
            String habitatName1 = mPokemon1.getHabitat();
            if (habitatName1 == null) {
                habitat1.setText("-");
                nullHabitatCounter++;
            } else {
                habitat1.setText(mPokemon1.getHabitat());
                habitat1.setOnClickListener(this);
            }
            String habitatName2 = mPokemon2.getHabitat();
            if (habitatName2 == null) {
                habitat2.setText("-");
                nullHabitatCounter++;
            } else {
                habitat2.setText(mPokemon2.getHabitat());
                habitat2.setOnClickListener(this);
            }
            if (nullHabitatCounter == 2) {
                mRootView.findViewById(R.id.compare_tvHeaderHabitat)
                        .setVisibility(View.GONE);
                ((View) habitat1.getParent()).setVisibility(View.GONE);
            }

            baseEggSteps1.setText(String.valueOf(mPokemon1.getBaseEggSteps()));
            baseEggSteps2.setText(String.valueOf(mPokemon2.getBaseEggSteps()));
            baseEggCycles1.setText(String.valueOf(mPokemon1.getBaseEggSteps() / AppConfig.EGG_CYCLE_STEPS));
            baseEggCycles2.setText(String.valueOf(mPokemon2.getBaseEggSteps() / AppConfig.EGG_CYCLE_STEPS));

            baseEggSteps1.setOnClickListener(this);
            baseEggSteps2.setOnClickListener(this);
            baseEggCycles1.setOnClickListener(this);
            baseEggCycles2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.compare_imgvPokemon_1:
                    Intent imageIntent1 = new Intent(getActivity(), PkmnImageDetailActivity.class);
                    imageIntent1.putExtra("POKEMON", mPokemon1.toMiniPokemon());
                    startActivity(imageIntent1);
                    break;
                case R.id.compare_imgvPokemon_2:
                    Intent imageIntent2 = new Intent(getActivity(), PkmnImageDetailActivity.class);
                    imageIntent2.putExtra("POKEMON", mPokemon2.toMiniPokemon());
                    startActivity(imageIntent2);
                    break;
                case R.id.compare_tvAbility1_1:
                case R.id.compare_tvAbility2_1:
                case R.id.compare_tvAbilityH_1:
                case R.id.compare_tvAbility1_2:
                case R.id.compare_tvAbility2_2:
                case R.id.compare_tvAbilityH_2:
                    TextView tvAbility = (TextView) v;
                    String abilityName = tvAbility.getText().toString();
                    Intent abilityIntent = new Intent(getActivity(), AbilityDetailActivity.class);
                    abilityIntent.putExtra("ABILITY", new MiniAbility(getContext(), abilityName));
                    startActivity(abilityIntent);
                    break;
                case R.id.compare_tvType1_1:
                case R.id.compare_tvType2_1:
                    Intent typeDetailIntent1 = new Intent(getActivity(), PkmnTypeDetailActivity.class);
                    typeDetailIntent1.putExtra("POKEMON", mPokemon1.toMiniPokemon());
                    startActivity(typeDetailIntent1);
                    break;
                case R.id.compare_tvType1_2:
                case R.id.compare_tvType2_2:
                    Intent typeDetailIntent2 = new Intent(getActivity(), PkmnTypeDetailActivity.class);
                    typeDetailIntent2.putExtra("POKEMON", mPokemon2.toMiniPokemon());
                    startActivity(typeDetailIntent2);
                    break;
                case R.id.compare_tvMass_1:
                case R.id.compare_tvMass_2:
                    TextView tvMass = (TextView) v;
                    String massWithKg = tvMass.getText().toString();
                    String mass = massWithKg.replace("kg", "").trim();
                    Intent massIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    massIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_MASS);
                    massIntent.putExtra("VALUE", mass);
                    startActivity(massIntent);
                    break;
                case R.id.compare_tvHeight_1:
                case R.id.compare_tvHeight_2:
                    TextView tvHeight = (TextView) v;
                    String heightWithM = tvHeight.getText().toString();
                    String height = heightWithM.replace("m", "").trim();
                    Intent heightIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    heightIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_HEIGHT);
                    heightIntent.putExtra("VALUE", height);
                    startActivity(heightIntent);
                    break;
                case R.id.compare_tvColor_1:
                case R.id.compare_tvColor_2:
                    TextView tvColour = (TextView) v;
                    String colour = tvColour.getText().toString();
                    Intent colourIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    colourIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_COLOUR);
                    colourIntent.putExtra("VALUE", colour);
                    startActivity(colourIntent);
                    break;
                case R.id.compare_tvShape_1:
                case R.id.compare_tvShape_2:
                    TextView tvShape = (TextView) v;
                    String shape = tvShape.getText().toString();
                    Intent shapeIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    shapeIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_SHAPE);
                    shapeIntent.putExtra("VALUE", shape);
                    startActivity(shapeIntent);
                    break;
                case R.id.compare_tvCatchrate_1:
                case R.id.compare_tvCatchrate_2:
                    TextView tvCatchRate = (TextView) v;
                    String catchRate = tvCatchRate.getText().toString();
                    Intent catchRateIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    catchRateIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_CATCH_RATE);
                    catchRateIntent.putExtra("VALUE", catchRate);
                    startActivity(catchRateIntent);
                    break;
                case R.id.compare_tvHappiness_1:
                case R.id.compare_tvHappiness_2:
                    TextView tvHappiness = (TextView) v;
                    String happiness = tvHappiness.getText().toString();
                    Intent happinessIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    happinessIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_HAPPINESS);
                    happinessIntent.putExtra("VALUE", happiness);
                    startActivity(happinessIntent);
                    break;
                case R.id.compare_tvLevellingrate_1:
                case R.id.compare_tvLevellingrate_2:
                    TextView tvLevelling = (TextView) v;
                    String levellingRate = tvLevelling.getText().toString();
                    Intent levellingRateIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    levellingRateIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_LEVELLING_RATE);
                    levellingRateIntent.putExtra("VALUE", levellingRate);
                    startActivity(levellingRateIntent);
                    break;
                case R.id.compare_tvExpgrowth_1:
                case R.id.compare_tvExpgrowth_2:
                    TextView tvExp = (TextView) v;
                    String exp = tvExp.getText().toString();
                    Intent expGrowthIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    expGrowthIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_EXP);
                    expGrowthIntent.putExtra("VALUE", exp);
                    startActivity(expGrowthIntent);
                    break;
                case R.id.compare_tvGeneration_1:
                case R.id.compare_tvGeneration_2:
                    TextView tvGen = (TextView) v;
                    String gen = tvGen.getText().toString();
                    Intent genIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    genIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_GENERATION);
                    genIntent.putExtra("VALUE", gen);
                    startActivity(genIntent);
                    break;
                case R.id.compare_tvHabitat_1:
                case R.id.compare_tvHabitat_2:
                    TextView tvHabitat = (TextView) v;
                    String habitat = tvHabitat.getText().toString();
                    Intent habitatIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    habitatIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_HABITAT);
                    habitatIntent.putExtra("VALUE", habitat);
                    startActivity(habitatIntent);
                    break;
                case R.id.compare_tvBaseeggsteps_1:
                case R.id.compare_tvBaseeggsteps_2:
                    TextView tvEggSteps = (TextView) v;
                    String eggSteps = tvEggSteps.getText().toString();
                    Intent eggStepsIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    eggStepsIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_EGG_STEPS);
                    eggStepsIntent.putExtra("VALUE", eggSteps);
                    startActivity(eggStepsIntent);
                    break;
                case R.id.compare_tvBaseeggcycles_1:
                case R.id.compare_tvBaseeggcycles_2:
                    TextView tvEggCycles = (TextView) v;
                    String eggCycles = tvEggCycles.getText().toString();
                    Intent eggCyclesIntent = new Intent(getActivity(), PropertyDetailActivity.class);
                    eggCyclesIntent.putExtra("PROPERTY", PropertyDetailActivity.PROPERTY_EGG_CYCLES);
                    eggCyclesIntent.putExtra("VALUE", eggCycles);
                    startActivity(eggCyclesIntent);
                    break;
            }
        }
    }

    public static class MovesFragment extends Fragment implements LabelledSpinner.OnItemChosenListener {

        private View mRootView;

        private boolean mSameLearnset;

        private String mLearnMethod, mGameVersion;

        private LinearLayout mContainer1, mContainer2;
        private Button mSubmitButton;
        private LabelledSpinner mSpinnerMethod, mSpinnerGame;

        private ArrayList<String> mArrayMethodTitles = new ArrayList<>();
        private ArrayList<Integer> mArrayMethodTypes = new ArrayList<>();
        private ArrayList<String> mArrayGameTitles;
        private ArrayList<Integer> mArrayGameTypes = new ArrayList<>();

        private AsyncTask<Void, Integer, Void> mAsyncTask;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_compare_learnsets, container, false);

            if (mPkmnName1.equals(mPkmnName2)) {
                if ((mPokemon1.isNormalForm() && mPokemon2.isMegaForm()) ||
                        mPokemon1.isMegaForm() && mPokemon2.isNormalForm()) {
                    mSameLearnset = true;
                }
            } else {
                mSameLearnset = false;
            }

            loadLearnsets();

            return mRootView;
        }

        private void loadLearnsets() {
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

            mSpinnerMethod = (LabelledSpinner) mRootView.findViewById(R.id.compareL_spinnerMethod);
            mSpinnerMethod.setItemsArray(mArrayMethodTitles);
            mSpinnerMethod.setSelection(0);
            mSpinnerMethod.setOnItemChosenListener(this);
            mSpinnerGame = (LabelledSpinner) mRootView.findViewById(R.id.compareL_spinnerGame);
            mSpinnerGame.setItemsArray(R.array.game_versions);
            mSpinnerGame.setSelection(mArrayGameTitles.size() - 1);
            mSpinnerGame.setOnItemChosenListener(this);

            mContainer1 = (LinearLayout) mRootView.findViewById(R.id.compareL_llContainer_1);
            mContainer2 = (LinearLayout) mRootView.findViewById(R.id.compareL_llContainer_2);

            mSubmitButton = (Button) mRootView.findViewById(R.id.compareL_btnGo);

            if (mSameLearnset) {
                mContainer2.setVisibility(View.GONE);
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAsyncTask != null) {
                            mAsyncTask.cancel(true);
                        }
                        loadCard(mContainer1, mPokemon1);
                    }
                });
            } else {
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAsyncTask != null) {
                            mAsyncTask.cancel(true);
                        }
                        loadCard(mContainer1, mPokemon1);
                        loadCard(mContainer2, mPokemon2);
                    }
                });
            }
        }

        private void loadCard(LinearLayout container, Pokemon pokemon) {
            container.removeAllViews();
            container.addView(makeCard(container, pokemon));
        }

        private View makeCard(LinearLayout container, final Pokemon pokemon) {
            View card = getActivity().getLayoutInflater().inflate(R.layout.card_detail_learnset, container, false);

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
                        learnset = new Learnset(getActivity(), pokemon.toMiniPokemon(), gameVersion, learnMethod);
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
                        learnset = new Learnset(getActivity(), pokemon.toMiniPokemon(), gameVersion, learnMethod);
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
                case R.id.compareL_spinnerMethod:
                    mLearnMethod = selected;
                    break;
                case R.id.compareL_spinnerGame:
                    mGameVersion = selected;
                    break;
            }
        }

        @Override
        public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {}
    }

}
