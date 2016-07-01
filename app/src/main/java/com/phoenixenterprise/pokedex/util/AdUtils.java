package com.phoenixenterprise.pokedex.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public final class AdUtils {

    public static void setupAds(Activity activity, @IdRes int adViewId) {
        AdView adView = (AdView) activity.findViewById(adViewId);

        if (Flavours.type == Flavours.Type.FREE) {
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
    }

}
