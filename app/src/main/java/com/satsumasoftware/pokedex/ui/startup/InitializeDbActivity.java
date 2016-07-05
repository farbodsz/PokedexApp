package com.satsumasoftware.pokedex.ui.startup;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;
import com.satsumasoftware.pokedex.db.EncounterConditionsDBHelper;
import com.satsumasoftware.pokedex.db.LocationAreasDBHelper;
import com.satsumasoftware.pokedex.db.LocationsDBHelper;
import com.satsumasoftware.pokedex.db.MovesDBHelper;
import com.satsumasoftware.pokedex.db.NaturesDBHelper;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.util.DatabaseUtils;
import com.satsumasoftware.pokedex.util.FavoriteUtils;

import java.util.ArrayList;

public class InitializeDbActivity extends AppCompatActivity {

    private static final String LOG_TAG = "InitializeDBActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialise_db);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");

        final ProgressBar progressMain = (ProgressBar) findViewById(R.id.progress_main);
        final ProgressBar progressBarDetail = (ProgressBar) findViewById(R.id.progress_detail);

        final TextView currentDbName = (TextView) findViewById(R.id.current_db_name);
        final TextView currentDbStatus = (TextView) findViewById(R.id.current_db_status);

        final TextView updatedDbList = (TextView) findViewById(R.id.updated_databases_list);
        final TextView completedDbNum = (TextView) findViewById(R.id.completed_databases_num);
        final TextView completedDbLines = (TextView) findViewById(R.id.completed_lines);

        FavoriteUtils.doFavouritesTempConversion(this);

        Log.d(LOG_TAG, "Starting to initialise DBs");

        final SQLiteOpenHelper[] helpers = {
                new AbilitiesDBHelper(this),
                new EncounterConditionsDBHelper(this),
                new LocationAreasDBHelper(this),
                new LocationsDBHelper(this),
                new MovesDBHelper(this),
                new NaturesDBHelper(this),
                new PokemonDBHelper(this),
        };
        final String[] tableNames = {
                AbilitiesDBHelper.TABLE_NAME,
                EncounterConditionsDBHelper.TABLE_NAME,
                LocationAreasDBHelper.TABLE_NAME,
                LocationsDBHelper.TABLE_NAME,
                MovesDBHelper.TABLE_NAME,
                NaturesDBHelper.TABLE_NAME,
                PokemonDBHelper.TABLE_NAME,
        };
        final int[] lineCounts = {
                252, // Abilities
                20, // Encounter Conditions
                717, // Location Areas
                690, // Locations
                640, // Moves
                26, // Natures
                914, // Pokemon
        };
        // i.e. ___ database

        //progressBar.setMax(100);

        new AsyncTask<Void, Boolean, Void>() {

            private int mLoop;
            private int mCompletedDbs = 0;
            private int mCompletedLines = 0;
            private int mSumOfLines;

            @Override
            protected void onPreExecute() {
                mSumOfLines = 0;
                for (int lineCount : lineCounts) {
                    mSumOfLines += lineCount;
                }
                progressMain.setMax(mSumOfLines);
                progressMain.setProgress(0);
                progressBarDetail.setIndeterminate(true);

                updatedDbList.setText("");
                completedDbNum.setText("0 databases complete");
                completedDbLines.setText("(approx. 0 lines)");
            }

            @Override
            protected Void doInBackground(Void... params) {
                String[] currentVersions = DatabaseUtils.getDbVersionArray();
                String[] savedDbVersions;
                if (DatabaseUtils.getSavedDbVersionString(InitializeDbActivity.this) != null) {
                    savedDbVersions = DatabaseUtils.getSavedDbVersionArray(InitializeDbActivity.this);
                } else {
                    ArrayList<String> tempSavedDbArrayList = new ArrayList<>();
                    for (String ignored : currentVersions) {
                        tempSavedDbArrayList.add("0");
                    }
                    savedDbVersions = tempSavedDbArrayList.toArray(new String[tempSavedDbArrayList.size()]);
                }
                for (int i = 0; i < helpers.length; i++) {
                    mLoop = i;
                    publishProgress(false);
                    Log.d(LOG_TAG, "background processes - starting loop #" + i);

                    if (!savedDbVersions[i].equals(currentVersions[i])) {
                        // this database has been updated
                        SQLiteOpenHelper helper = helpers[i];
                        SQLiteDatabase db = helper.getReadableDatabase();
                        Cursor cursor = db.query(true, tableNames[i], null, null, null, null, null, null, null);
                        cursor.moveToFirst();
                        cursor.close();
                        db.close();
                        System.gc();
                    }

                    publishProgress(true);
                    Log.d(LOG_TAG, "background processes - running total has been published - loop #" + i + " complete");

                    if (isCancelled()) {
                        cancel(true);
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Boolean... values) {
                boolean hasCompleted = values[0];
                if (!hasCompleted) {
                    currentDbName.setText(tableNames[mLoop]);
                    currentDbStatus.setText("Creating db from " + lineCounts[mLoop] + "+ lines");
                } else {
                    mCompletedDbs++;
                    mCompletedLines += lineCounts[mLoop];
                    updatedDbList.setText(updatedDbList.getText().toString() + tableNames[mLoop-1] + "\n");
                    completedDbNum.setText(mCompletedDbs + " of " + tableNames.length + " databases complete");
                    completedDbLines.setText("(approx. " + mCompletedLines + " lines)");
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                DatabaseUtils.markDatabaseUpgraded(getBaseContext());
                Log.d(LOG_TAG, "Completed initialising DBs");
                startActivity(new Intent(InitializeDbActivity.this, WelcomeActivity.class));
                finish();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.initialise_wait_prompt, Toast.LENGTH_SHORT).show();
    }
}
