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

package com.satsumasoftware.pokedex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.pokedex.BuildConfig;
import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.util.AdUtils;
import com.satsumasoftware.pokedex.util.Flavours;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AdUtils.setupAds(this, R.id.adView);

        setAppInformation();

        Button btnChangelog = (Button) findViewById(R.id.button_changelog);
        btnChangelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, ChangelogActivity.class));
            }
        });

        Button btnSettings = (Button) findViewById(R.id.button_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, SettingsActivity.class));
            }
        });
    }

    private void setAppInformation() {
        TextView appInfo = (TextView) findViewById(R.id.text_app_info);
        String appName = getResources().getString(R.string.app_name);
        String versionSuffix = "";
        if (Flavours.type == Flavours.Type.FREE) {
            versionSuffix = "-free";
        }
        String versionName = BuildConfig.VERSION_NAME + versionSuffix;
        appInfo.setText(getResources().getString(R.string.about_app_info, appName, versionName));
    }
}
