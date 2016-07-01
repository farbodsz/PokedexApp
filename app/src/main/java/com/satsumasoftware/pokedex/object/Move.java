package com.satsumasoftware.pokedex.object;

import android.content.Context;

import com.satsumasoftware.pokedex.util.CSVUtils;
import com.satsumasoftware.pokedex.util.InfoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Move {

    private Context mContext;
    private String mMove;
    private int mMoveId;

    private int mPp, mGeneration;
    private String mType, mCategory, mContest, mPower, mAccuracy, mDescription;
    // Note that power and accuracy are strings because their values could be "n/a"

    public Move(Context context, String move) {
        mContext = context;
        mMove = move;

        loadInformation(1);
    }

    public Move(Context context, int moveID) {
        mContext = context;
        mMoveId = moveID;

        loadInformation(0);
    }

    public Move(int moveId, String move, String type, String category, String contest,
                int pp, String power, String accuracy, int generation, String description) {
        mMoveId = moveId;
        mMove = move;
        mType = type;
        mCategory = category;
        mContest = contest;
        mPp = pp;
        mPower = power;
        mAccuracy = accuracy;
        mGeneration = generation;
        mDescription = description;
    }

    private void loadInformation(int searchType) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.MOVEDEX)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String[] line = data.split(",,"); // Splits the line up into a string array

                if (line.length > 1) {

                    boolean condition = false;
                    if (searchType == 1) {
                        condition = line[1].equals(mMove);
                    } else if (searchType == 0) {
                        condition = line[0].equals(String.valueOf(mMoveId));
                    }

                    if (condition) {
                        mMoveId = Integer.parseInt(line[0]);
                        mMove = line[1];
                        mType = line[2];
                        mCategory = line[3];
                        mContest = line[4];
                        mPp = Integer.parseInt(line[5]);
                        mPower = line[6];
                        mAccuracy = line[7];
                        mGeneration = Integer.parseInt(line[8]);
                        mDescription = InfoUtils.formatDBDescription(line[9]);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMoveId() {
        return mMoveId;
    }

    public String getMove() {
        return mMove;
    }

    public String getType() {
        return mType;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getContest() {
        return mContest;
    }

    public int getPP() {
        return mPp;
    }

    public String getPower() {
        return mPower;
    }

    public String getAccuracy() {
        return mAccuracy;
    }

    public int getGeneration() {
        return mGeneration;
    }

    public String getDescription() {
        return mDescription;
    }
}
