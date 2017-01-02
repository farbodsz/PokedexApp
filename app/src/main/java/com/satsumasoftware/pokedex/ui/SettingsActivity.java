package com.satsumasoftware.pokedex.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_frame);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_fragment);

            setupLanguagePref();
        }

        private void setupLanguagePref() {
            ListPreference localePref = (ListPreference) findPreference(PrefUtils.PREF_APP_LOCALE);

            localePref.setSummary(localePref.getEntry());

            localePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String language = (String) newValue;
                    Locale newLocale = new Locale(language);

                    Locale.setDefault(newLocale);
                    Configuration configuration = new Configuration();
                    configuration.locale = newLocale;

                    getResources().updateConfiguration(configuration,
                            getResources().getDisplayMetrics());

                    getActivity().recreate();

                    return true;
                }
            });
        }
    }

}
