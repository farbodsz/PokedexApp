package com.phoenixenterprise.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phoenixenterprise.pokedex.util.PrefUtils;


public class SplashActivity extends AppCompatActivity {

    private static final int SLEEP_TIME = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent;
                    if (!PrefUtils.hasDatabaseInitialised(getBaseContext())) {
                        intent = new Intent(getBaseContext(), InitialiseDBActivity.class);
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
