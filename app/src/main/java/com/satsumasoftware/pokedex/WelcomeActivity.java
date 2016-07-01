package com.satsumasoftware.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.pokedex.util.ChangelogUtils;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        TextView tvTitle = (TextView) findViewById(R.id.welcome_title);
        String appName = getResources().getString(R.string.app_name);
        tvTitle.setText(getResources().getString(R.string.welcome_message, appName));

        TextView tvSubtitle = (TextView) findViewById(R.id.welcome_subtitle);
        String versionName = "v" + BuildConfig.VERSION_NAME;
        tvSubtitle.setText(versionName);


        TextView tvBody = (TextView) findViewById(R.id.welcome_body);
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


        Button btnDone = (Button) findViewById(R.id.welcome_btnContinue);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, PokedexActivity.class));
                finish();
                // Welcome will be marked done in Pokedex activity after showing the nav drawer
            }
        });

        Button btnChangelog = (Button) findViewById(R.id.welcome_btnChangelog);
        btnChangelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, ChangelogActivity.class));
            }
        });


        PrefUtils.doTempBugFix(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
