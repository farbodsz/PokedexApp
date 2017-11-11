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

package com.satsumasoftware.pokedex.ui.startup;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class InitializeDbActivity extends AppCompatActivity {

    private static final String LOG_TAG = "InitializeDBActivity";

    private Timer mTimer;
    private int mTipIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialise_db);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        final TextView tipText = (TextView) findViewById(R.id.tip_text);

        final String[] tips = getResources().getStringArray(R.array.usage_tips);

        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final String tip = tips[mTipIndex];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tipText.setText(tip);
                    }
                });

                mTipIndex = (mTipIndex == tips.length - 1) ?
                        0 : (mTipIndex + 1);
            }
        }, 0, 5000);  // every 5 seconds

        FavoriteUtils.doFavouritesTempConversion(this);

        Log.d(LOG_TAG, "Starting to initialise DBs");

        final SQLiteOpenHelper[] helpers = {
                AbilitiesDBHelper.getInstance(this),
                EncounterConditionsDBHelper.getInstance(this),
                LocationAreasDBHelper.getInstance(this),
                LocationsDBHelper.getInstance(this),
                MovesDBHelper.getInstance(this),
                NaturesDBHelper.getInstance(this),
                PokemonDBHelper.getInstance(this),
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

        new AsyncTask<Void, Boolean, Void>() {

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

                    Log.d(LOG_TAG, "background processes - running total has been published - loop #" + i + " complete");

                    if (isCancelled()) {
                        cancel(true);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                DatabaseUtils.markDatabaseUpgraded(getBaseContext());
                Log.d(LOG_TAG, "Completed initialising DBs");

                mTimer.cancel();

                startActivity(new Intent(InitializeDbActivity.this, WelcomeActivity.class));
                finish();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.initialise_wait_prompt, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefUtils.setTipsContinueFromIndex(this, mTipIndex);
    }
}
