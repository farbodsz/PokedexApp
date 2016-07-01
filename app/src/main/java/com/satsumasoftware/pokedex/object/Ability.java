package com.satsumasoftware.pokedex.object;

import android.content.Context;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ability {

    private Context mContext;
    private String mAbility;

    private int mAbilityId, mGeneration;
    private String mEffect;

    public Ability(Context context, String ability) {
        mContext = context;
        mAbility = ability;

        loadInformation();
    }

    public Ability(int id, String ability, String effect, int generation) {
        mAbilityId = id;
        mAbility = ability;
        mEffect = effect;
        mGeneration = generation;
    }

    private void loadInformation() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.ABILITYDEX)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String[] line = data.split(",,"); // Splits the line up into a string array

                if (line.length > 1) {
                    if (line[1].equals(mAbility)) {
                        mAbilityId = Integer.parseInt(line[0]);
                        mEffect = InfoUtils.formatDBDescription(line[2]);
                        mGeneration = Integer.parseInt(line[3]);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getAbilityId() {
        return mAbilityId;
    }

    public String getAbilityName() {
        return mAbility;
    }

    public String getEffect() {
        return mEffect;
    }

    public int getGeneration() {
        return mGeneration;
    }

}
