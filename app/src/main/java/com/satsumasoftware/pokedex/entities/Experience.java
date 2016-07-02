package com.satsumasoftware.pokedex.entities;

import android.content.Context;

import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Experience {

    // TODO SQL

    public static final int GROWTH_FLUCTUATING = 1;
    public static final int GROWTH_SLOW = 2;
    public static final int GROWTH_MEDIUM_SLOW = 3;
    public static final int GROWTH_MEDIUM_FAST = 4;
    public static final int GROWTH_FAST = 5;
    public static final int GROWTH_ERRATIC = 6;

    public static String getTotalExperience(Context context, int growthId, String level) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(CSVUtils.EXP_INFO_DB)));
            reader.readLine();
            String data;
            while ((data=reader.readLine()) != null) {
                String[] line = data.split(",");

                if (line.length > 1) {
                    if (line[0].equals(String.valueOf(growthId))
                            && line[1].equals(level)) {
                        return line[2];
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getGrowthIdFromString(String string) {
        switch (string.toLowerCase()) {
            case "fluctuating": return GROWTH_FLUCTUATING;
            case "slow": return GROWTH_SLOW;
            case "medium slow": return GROWTH_MEDIUM_SLOW;
            case "medium fast": return GROWTH_MEDIUM_FAST;
            case "fast": return GROWTH_FAST;
            case "erratic": return GROWTH_ERRATIC;
            default: throw new IllegalArgumentException("The string in the parameter is not a recognised growth rate");
        }
    }

}
