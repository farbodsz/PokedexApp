package com.phoenixenterprise.pokedex.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class ChangelogUtils {

    public static ArrayList<Integer> getListOfVersions(Context context) {
        ArrayList<Integer> arrayVersions = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(CSVUtils.CHANGELOG)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String line[] = data.split(CSVUtils.CHANGELOG_SEP);
                if (line.length > 1) {
                    arrayVersions.add(Integer.parseInt(line[0]));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayVersions;
    }

    public static ArrayList<String> getVersionChanges(Context context, int versionCode) {
        ArrayList<String> arrayFeatures = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(CSVUtils.CHANGELOG)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String line[] = data.split(CSVUtils.CHANGELOG_SEP);
                if (line.length > 1) {
                    if (Integer.parseInt(line[0]) == versionCode) {

                        for (int i = 0; i < line.length; i++) {
                            if (i != 0) {
                                if (line[i].contains("okemon")) {
                                    line[i] = line[i].replace("okemon", "ok"+"\u00E9"+"mon");
                                }
                                arrayFeatures.add(line[i]);
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayFeatures;
    }
}
