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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.pokedex.BuildConfig;
import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.ui.ChangelogActivity;
import com.satsumasoftware.pokedex.ui.PokedexActivity;
import com.satsumasoftware.pokedex.util.ChangelogUtils;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        TextView tvTitle = (TextView) findViewById(R.id.title);
        String appName = getResources().getString(R.string.app_name);
        tvTitle.setText(getResources().getString(R.string.welcome_message, appName));

        TextView tvSubtitle = (TextView) findViewById(R.id.subtitle);
        String versionName = "v" + BuildConfig.VERSION_NAME;
        tvSubtitle.setText(versionName);


        TextView tvBody = (TextView) findViewById(R.id.body_text);
        StringBuilder welcomeText = new StringBuilder();
        welcomeText.append("Here is what's new:\n\n");


        ArrayList<String> versionChanges = ChangelogUtils.getVersionChanges(this, BuildConfig.VERSION_CODE);
        for (int i = 0; i < versionChanges.size(); i++) {
            String bulletAndPoint = "\u2022 " + versionChanges.get(i); // u2022 is a bullet point
            welcomeText.append(bulletAndPoint);
            if (i != versionChanges.size()-1) { // If not last point
                welcomeText.append("\n");
            }
        }

        tvBody.setText(welcomeText);


        Button btnDone = (Button) findViewById(R.id.button_continue);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, PokedexActivity.class));
                finish();
                // Welcome will be marked done in Pokedex activity after showing the nav drawer
            }
        });

        Button btnChangelog = (Button) findViewById(R.id.button_changelog);
        btnChangelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, ChangelogActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
