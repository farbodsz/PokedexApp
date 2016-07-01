package com.satsumasoftware.pokedex.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Nature implements Parcelable {

    private Context mContext;
    private String mNature;

    private String mStatIncreased, mStatDecreased;

    public Nature(Context context, String nature) {
        mContext = context;
        mNature = nature;

        loadInformation();
    }

    public Nature(String nature, String statIncreased, String statDecreased) {
        mNature = nature;
        mStatIncreased = statIncreased;
        mStatDecreased = statDecreased;
    }

    private void loadInformation() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.NATUREDEX)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null) {
                String[] line = data.split(","); // Splits the line up into a string array

                if (line.length > 1) {
                    if (line[0].equals(mNature)) {
                        mStatIncreased = line[1];
                        mStatDecreased = line[2];
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNatureName() {
        return mNature;
    }

    public String getStatIncreased() {
        return mStatIncreased;
    }

    public String getStatDecreased() {
        return mStatDecreased;
    }

    protected Nature(Parcel in) {
        mNature = in.readString();
        mStatIncreased = in.readString();
        mStatDecreased = in.readString();
    }

    public static final Creator<Nature> CREATOR = new Creator<Nature>() {
        @Override
        public Nature createFromParcel(Parcel in) {
            return new Nature(in);
        }

        @Override
        public Nature[] newArray(int size) {
            return new Nature[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNature);
        dest.writeString(mStatIncreased);
        dest.writeString(mStatDecreased);
    }
}
