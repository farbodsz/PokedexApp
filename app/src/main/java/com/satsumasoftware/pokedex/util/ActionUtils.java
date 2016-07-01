package com.satsumasoftware.pokedex.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.satsumasoftware.pokedex.ExperienceCalculatorActivity;
import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.object.Pokemon;

import java.util.ArrayList;

public final class ActionUtils { // TODO: Add more methods here

    public static void handleListLongClick(final Activity activity, final MiniPokemon miniPokemon, final View rootLayout) {
        ArrayList<String> quickActionsList = new ArrayList<>();
        if (PrefUtils.isAFavouritePkmn(activity.getBaseContext(), miniPokemon.getNationalIdFormatted(), miniPokemon.getPokemon(), miniPokemon.getForm())) {
            quickActionsList.add(activity.getResources().getString(R.string.action_favourite_remove));
        } else {
            quickActionsList.add(activity.getResources().getString(R.string.action_favourite));
        }
        quickActionsList.add(activity.getResources().getString(R.string.action_calculate_experience));

        String[] quickActions = new String[quickActionsList.size()];
        quickActions = quickActionsList.toArray(quickActions);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(InfoUtils.getNameAndForm(miniPokemon.getPokemon(), miniPokemon.getForm()));
        builder.setItems(quickActions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch (which) {
                    case 0:
                        // Add to favourites
                        if (Flavours.type == Flavours.Type.PAID) {
                            PrefUtils.markAsFavouritePkmn(activity.getBaseContext(),
                                    miniPokemon.getNationalIdFormatted(),
                                    miniPokemon.getPokemon(),
                                    miniPokemon.getForm(),
                                    rootLayout);
                        } else {
                            AlertUtils.requiresProSnackbar(activity, rootLayout);
                        }
                        break;
                    case 1:
                        // Experience Calculator
                        Intent intent = new Intent(activity, ExperienceCalculatorActivity.class);
                        intent.putExtra("POKEMON_NAME", miniPokemon.getPokemon());
                        activity.startActivity(intent);
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void playPokemonCry(Context context, Pokemon pokemon) {
        String normalFilename = "cry_" + pokemon.getNationalId();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cry_")
                .append(pokemon.getNationalId());

        switch (pokemon.getNationalId()) {
            case 492:
                if (pokemon.getForm().equals("Land Forme")) {
                    stringBuilder.append("_land");
                } else {
                    stringBuilder.append("_sky");
                }
                break;
            case 641:
            case 642:
            case 645:
                if (pokemon.getForm().equals("Incarnate Forme")) {
                    stringBuilder.append("_incarnate");
                } else {
                    stringBuilder.append("_therian");
                }
                break;
            case 646:
                if (pokemon.getForm().equals("Black Kyurem")) {
                    stringBuilder.append("_black");
                } else if (pokemon.getForm().equals("White Kyurem")) {
                    stringBuilder.append("_white");
                }
                break;
            case 670:
                // TODO: Add Eternal Floette form to the Pokedex
                if (pokemon.getForm().equals("Eternal Floette")) {
                    stringBuilder.append("_eternal");
                }
                break;
            case 710:
            case 711:
                switch (pokemon.getForm()) {
                    case "Average Size": stringBuilder.append("_average"); break;
                    case "Small Size": stringBuilder.append("_small"); break;
                    case "Large Size": stringBuilder.append("_large"); break;
                    case "Super Size": stringBuilder.append("_super"); break;
                }
        }

        switch (pokemon.getForm()) {
            case "Mega":
                stringBuilder.append("_mega");
                break;
            case "Mega X":
                stringBuilder.append("_mega_x");
                break;
            case "Mega Y":
                stringBuilder.append("_mega_y");
                break;
        }

        String filename = stringBuilder.toString();
        int resId = 0;

        try {
            resId = context.getResources().getIdentifier(filename, "raw", context.getPackageName());
        } catch (Resources.NotFoundException e1) {
            try {
                resId = context.getResources().getIdentifier(normalFilename, "raw", context.getPackageName());
            } catch (Resources.NotFoundException e2) {
                e2.printStackTrace();
            }
        }

        if (resId == 0) throw new NullPointerException("resId is null (given value of 0)");

        MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.start();
    }

}
