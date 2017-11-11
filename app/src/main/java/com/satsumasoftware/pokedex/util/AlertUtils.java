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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.satsumasoftware.pokedex.R;

import static android.support.v4.app.ActivityCompat.startActivity;

public final class AlertUtils {

    public static void buyPro(Activity activity) {
        final String appPackageName = "com.satsumasoftware.pokedex.pro";
        //final String appPackageName = getPackageName(); // Gets name from Context or Activity object
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            startActivity(activity, intent, null);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
            startActivity(activity, intent, null);
        }
    }

    public static void requiresProSnackbar(final Activity activity, View parentLayout) {
        String message = activity.getResources().getString(R.string.misc_requires_pro);

        // Gets color attribute programmatically: http://stackoverflow.com/questions/27611173/how-to-get-accent-color-programmatically
        final TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        int color = value.data;

        Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_SHORT)
                .setAction("Buy", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyPro(activity);
                    }
                })
                .setActionTextColor(color)
                .show();
    }

    public static void requiresProToast(final Activity activity) {
        String message = activity.getResources().getString(R.string.misc_requires_pro);
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
