package com.phoenixenterprise.pokedex;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.phoenixenterprise.pokedex.db.AbilityDBHelper;
import com.phoenixenterprise.pokedex.db.FormIdDBHelper;
import com.phoenixenterprise.pokedex.db.LearnsetDBHelper;
import com.phoenixenterprise.pokedex.db.LocationDBHelper;
import com.phoenixenterprise.pokedex.db.MoveDBHelper;
import com.phoenixenterprise.pokedex.db.NatureDBHelper;
import com.phoenixenterprise.pokedex.db.PokedexDBHelper;
import com.phoenixenterprise.pokedex.db.internal.EncounterDBHelper;
import com.phoenixenterprise.pokedex.db.internal.EncounterSlotsDBHelper;
import com.phoenixenterprise.pokedex.db.internal.LocationAreaDBHelper;
import com.phoenixenterprise.pokedex.db.internal.LocationAreaEncounterRatesDBHelper;
import com.phoenixenterprise.pokedex.db.internal.LocationGameIndicesDBHelper;
import com.phoenixenterprise.pokedex.util.PrefUtils;

public class InitialiseDBActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private TextView mTextView;

    private TextView mLoadDescription;

    private int mLoop;

    private static final String LOG_TAG = "InitialiseDBActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialise_db);

        Log.d(LOG_TAG, "Starting to initialise DBs");

        mProgress = (ProgressBar) findViewById(R.id.initDb_progress);
        mTextView = (TextView) findViewById(R.id.initDb_info);
        mLoadDescription = (TextView) findViewById(R.id.initDb_setup_description);

        final SQLiteOpenHelper[] helpers = {
                new PokedexDBHelper(this),
                new FormIdDBHelper(this),
                new AbilityDBHelper(this),
                new MoveDBHelper(this),
                new NatureDBHelper(this),
                new LearnsetDBHelper(this),
                new LocationDBHelper(this), // TODO: Remove these before publishing
                new LocationGameIndicesDBHelper(this),
                new EncounterDBHelper(this),
                new EncounterSlotsDBHelper(this),
                new LocationAreaDBHelper(this),
                new LocationAreaEncounterRatesDBHelper(this)
        };
        final String[] tableNames = {
                PokedexDBHelper.TABLE_NAME,
                FormIdDBHelper.TABLE_NAME,
                AbilityDBHelper.TABLE_NAME,
                MoveDBHelper.TABLE_NAME,
                NatureDBHelper.TABLE_NAME,
                LearnsetDBHelper.TABLE_NAME,
                LocationDBHelper.TABLE_NAME,
                LocationGameIndicesDBHelper.TABLE_NAME,
                EncounterDBHelper.TABLE_NAME,
                EncounterSlotsDBHelper.TABLE_NAME,
                LocationAreaDBHelper.TABLE_NAME,
                LocationAreaEncounterRatesDBHelper.TABLE_NAME
        };
        final int[] progressTotals = {
                5, // Pokedex
                7, // Form Ids
                8, // Ability
                11, // Move
                12, // Nature
                65, // Learnset
                70, // Location
                72, // Location Game Indices
                84, // Encounter
                87, // Encounter Slots
                92, // Location Area
                100 // Location Area Encounter Rates
        };
        final String[] dbDescriptions = {
                "Pokedex",
                "Pokedex", // Form Ids
                "Ability",
                "Move",
                "Nature",
                "Learnset",
                "Location",
                "Location", // Location Game Indices
                "Pokémon Encounters",
                "Pokémon Encounters", // Encounter Slots
                "Location Areas",
                "Location Areas" // Location Area Encounter Rates
        };
        // i.e. ___ database

        mProgress.setMax(100);

        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                mProgress.setProgress(0);
                mTextView.setText(getResources().getString(R.string.initialise_loading, "0%"));
            }

            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < helpers.length; i++) {
                    mLoop = i;
                    Log.d(LOG_TAG, "background processes - starting loop #" + i);
                    SQLiteOpenHelper helper = helpers[i];
                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.query(tableNames[i], null, null, null, null, null, null);
                    cursor.close();

                    publishProgress(progressTotals[i]);
                    Log.d(LOG_TAG, "background processes - running total has been published - loop #" + i + " complete");

                    if (isCancelled()) cancel(true);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                int percentage = values[0];

                ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", percentage);
                animation.setDuration(500);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();

                mTextView.setText(getResources().getString(R.string.initialise_loading, percentage + "%"));
                if (mLoop < dbDescriptions.length-1) {
                    mLoadDescription.setText(getResources().getString(R.string.initialise_setup_desc, dbDescriptions[mLoop+1]));
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mTextView.setText(getResources().getString(R.string.initialise_finished));
                PrefUtils.markDatabaseInitialised(getBaseContext());
                Log.d(LOG_TAG, "Completed initialising DBs");
                startActivity(new Intent(InitialiseDBActivity.this, WelcomeActivity.class));
                finish();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.initialise_wait_prompt, Toast.LENGTH_SHORT).show();
    }
}
