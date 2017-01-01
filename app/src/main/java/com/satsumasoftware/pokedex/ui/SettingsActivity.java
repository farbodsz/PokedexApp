package com.satsumasoftware.pokedex.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.util.PrefUtils;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // http://stackoverflow.com/questions/26564400/creating-a-preference-screen-with-support-v21-toolbar
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        AppBarLayout appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(appBar, 0); // insert at top
        setSupportActionBar((Toolbar) root.findViewById(R.id.settings_toolbar));

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_fragment);

            setupLanguagePref();
        }

        private void setupLanguagePref() {
            findPreference(PrefUtils.PREF_APP_LOCALE).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
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
