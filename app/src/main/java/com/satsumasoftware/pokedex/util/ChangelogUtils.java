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

package com.satsumasoftware.pokedex.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class ChangelogUtils {

    public static final String FILENAME = "changelog.csv";
    public static final String SEPARATOR = ",\",\",";


    public static ArrayList<Integer> getListOfVersions(Context context) {
        ArrayList<Integer> arrayVersions = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(FILENAME)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String line[] = data.split(SEPARATOR);
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(FILENAME)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String line[] = data.split(SEPARATOR);
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
