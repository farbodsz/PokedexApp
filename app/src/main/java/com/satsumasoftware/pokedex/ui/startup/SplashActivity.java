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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.ui.PokedexActivity;
import com.satsumasoftware.pokedex.util.DatabaseUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private static final int SLEEP_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tipText = (TextView) findViewById(R.id.tip_text);
        String[] tipsArray = getResources().getStringArray(R.array.usage_tips);

        int index = PrefUtils.getTipsContinueFromIndex(this);
        tipText.setText(tipsArray[index]);
        PrefUtils.setTipsContinueFromIndex(this,
                index == tipsArray.length - 1 ? 0 : index + 1);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent;
                    if (DatabaseUtils.hasDatabaseUpgraded(getBaseContext())) {
                        intent = new Intent(getBaseContext(), InitializeDbActivity.class);
                    } else if (!PrefUtils.isWelcomeDone(getBaseContext())) {
                        intent = new Intent(getBaseContext(), WelcomeActivity.class);
                    } else {
                        intent = new Intent(getBaseContext(), PokedexActivity.class);
                    }
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
