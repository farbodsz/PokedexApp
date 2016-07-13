package com.satsumasoftware.pokedex.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;
import com.satsumasoftware.pokedex.framework.pokemon.Pokemon;
import com.satsumasoftware.pokedex.ui.ExperienceCalculatorActivity;

import java.util.ArrayList;

public final class ActionUtils {

    private static String LOG_TAG = "ActionUtils";

    public static void handleListLongClick(final Activity activity, final MiniPokemon miniPokemon, final View rootLayout) {
        ArrayList<String> quickActionsList = new ArrayList<>();
        if (FavoriteUtils.isAFavouritePkmn(activity.getBaseContext(), miniPokemon)) {
            quickActionsList.add(activity.getResources().getString(R.string.action_favourite_remove));
        } else {
            quickActionsList.add(activity.getResources().getString(R.string.action_favourite_add));
        }
        quickActionsList.add(activity.getResources().getString(R.string.action_calculate_experience));

        String[] quickActions = new String[quickActionsList.size()];
        quickActions = quickActionsList.toArray(quickActions);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(miniPokemon.getFormAndPokemonName());
        builder.setItems(quickActions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch (which) {
                    case 0:
                        // Add to favourites
                        if (Flavours.type == Flavours.Type.PAID) {
                            FavoriteUtils.markAsFavouritePkmn(activity.getBaseContext(),
                                    miniPokemon,
                                    rootLayout);
                        } else {
                            AlertUtils.requiresProSnackbar(activity, rootLayout);
                        }
                        break;
                    case 1:
                        // Experience Calculator
                        Intent intent = new Intent(activity, ExperienceCalculatorActivity.class);
                        intent.putExtra(ExperienceCalculatorActivity.EXTRA_POKEMON, miniPokemon);
                        activity.startActivity(intent);
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void playPokemonCry(Context context, Pokemon pokemon) {
        String normalFilename = "cry_" + pokemon.getSpeciesId();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cry_")
                .append(pokemon.getSpeciesId());

        switch (pokemon.getId()) {
            // Shaymin
            case 492: stringBuilder.append("_land"); break;
            case 10006: stringBuilder.append("_sky"); break;

            // Tornadus, etc
            case 641:
            case 642:
            case 645:
                stringBuilder.append("_incarnate");
                break;
            case 10019:
            case 10020:
            case 10021:
                stringBuilder.append("_therian");
                break;

            // Kyurem (Black and white)
            case 10022: stringBuilder.append("_black"); break;
            case 10023: stringBuilder.append("_white"); break;

            // Floette (Eternal form)
            case 10061: stringBuilder.append("_eternal"); break;

            // Pumpkaboo, etc.
            case 710:
            case 711:
                stringBuilder.append("_average");
                break;
            case 10027:
            case 10030:
                stringBuilder.append("_small");
                break;
            case 10028:
            case 10031:
                stringBuilder.append("_large");
                break;
            case 10029:
            case 10032:
                stringBuilder.append("_super");
                break;

            default:
                // Mega
                if (Pokemon.isFormMega(pokemon.getFormSpecificValues())) {
                    stringBuilder.append("_mega");
                    switch (pokemon.getId()) {
                        case 10034:
                        case 10043:
                            stringBuilder.append("_x");
                            break;
                        case 10035:
                        case 10044:
                            stringBuilder.append("_y");
                            break;
                    }
                }
        }

        String filename = stringBuilder.toString();
        int resId = 0;
        MediaPlayer mediaPlayer = null;

        Log.d(LOG_TAG, "Note filename: " + filename + " | and normalFilename: " + normalFilename);

        try {
            resId = context.getResources().getIdentifier(filename, "raw", context.getPackageName());
            mediaPlayer = MediaPlayer.create(context, resId);
        } catch (Resources.NotFoundException e1) {
            Log.d(LOG_TAG, "Can't find resId from filename: " + filename);
            e1.printStackTrace();
            try {
                resId = context.getResources().getIdentifier(normalFilename, "raw", context.getPackageName());
                mediaPlayer = MediaPlayer.create(context, resId);
            } catch (Resources.NotFoundException e2) {
                Log.d(LOG_TAG, "Can't find resId from filename: " + normalFilename);
                e2.printStackTrace();
            }
        }
        assert mediaPlayer != null;

        Log.d(LOG_TAG, "Note resource identifier: " + resId);
        //if (resId == 0) throw new NullPointerException("resId is null (given value of 0)");

        mediaPlayer.start();
    }


    public static void setPokemonImage(MiniPokemon pokemon, ImageView imageView) {
        setPokemonImage(pokemon.getId(),
                InfoUtils.formatPokemonId(pokemon.getNationalDexNumber()),
                pokemon.getName(),
                false,
                imageView);
    }

    public static void setPokemonImage(Pokemon pokemon, ImageView imageView) {
        setPokemonImage(pokemon.getId(),
                InfoUtils.formatPokemonId(pokemon.getNationalDexNumber()),
                pokemon.getName(),
                Pokemon.isFormMega(pokemon.getFormSpecificValues()),
                imageView);
    }

    public static void setPokemonImage(int pkmnId, String nationalIdFormatted, String name, boolean isFormMega, ImageView imageView) {
        int resId;
        switch (pkmnId) { // TODO: base this of form id not pokemon id
            case 29: resId = R.drawable.img_pkmn_029_nidoran; break;
            case 32: resId = R.drawable.img_pkmn_032_nidoran; break;
            case 83: resId = R.drawable.img_pkmn_083_farfetchd; break;
            case 122: resId = R.drawable.img_pkmn_122_mrmime; break;
            case 250: resId = R.drawable.img_pkmn_250_hooh; break;
            case 10077: resId = R.drawable.img_pkmn_382_kyogre_primal; break;
            case 10078: resId = R.drawable.img_pkmn_383_groudon_primal; break;
            case 10001: resId = R.drawable.img_pkmn_386_deoxys_attack; break;
            case 10002: resId = R.drawable.img_pkmn_386_deoxys_defense; break;
            case 10003: resId = R.drawable.img_pkmn_386_deoxys_speed; break;
            case 412: resId = R.drawable.img_pkmn_412_burmy_plant; break;
            case 413: resId = R.drawable.img_pkmn_413_wormadam_plant; break;
            case 10004: resId = R.drawable.img_pkmn_413_wormadam_sandy; break;
            case 10005: resId = R.drawable.img_pkmn_413_wormadam_trash; break;
            case 439: resId = R.drawable.img_pkmn_439_mimejr; break;
            case 474: resId = R.drawable.img_pkmn_474_porygonz; break;
            case 10008: resId = R.drawable.img_pkmn_479_rotom_heat; break;
            case 10009: resId = R.drawable.img_pkmn_479_rotom_wash; break;
            case 10010: resId = R.drawable.img_pkmn_479_rotom_frost; break;
            case 10011: resId = R.drawable.img_pkmn_479_rotom_fan; break;
            case 10012: resId = R.drawable.img_pkmn_479_rotom_mow; break;
            case 487: resId = R.drawable.img_pkmn_487_giratina_altered; break;
            case 10007: resId = R.drawable.img_pkmn_487_giratina_origin; break;
            case 492: resId = R.drawable.img_pkmn_492_shaymin_land; break;
            case 10006: resId = R.drawable.img_pkmn_492_shaymin_sky; break;
            case 10017: resId = R.drawable.img_pkmn_555_darmanitan_zen; break;
            case 10019: resId = R.drawable.img_pkmn_641_tornadus_therian; break;
            case 10020: resId = R.drawable.img_pkmn_642_thundurus_therian; break;
            case 10021: resId = R.drawable.img_pkmn_645_landorus_therian; break;
            case 10022: resId = R.drawable.img_pkmn_646_kyurem_black; break;
            case 10023: resId = R.drawable.img_pkmn_646_kyurem_white; break;
            case 10024: resId = R.drawable.img_pkmn_647_keldeo_resolute; break;
            case 10018: resId = R.drawable.img_pkmn_648_meloetta_pirouette; break;
            case 10026: resId = R.drawable.img_pkmn_681_aegislash; break; // The one image is of both forms
            case 10027:
            case 10028:
            case 10029:
                resId = R.drawable.img_pkmn_710_pumpkaboo; // The one image is of all forms
                break;
            case 10030:
            case 10031:
            case 10032:
                resId = R.drawable.img_pkmn_710_pumpkaboo; // The one image is of all forms
                break;
            case 10086: resId = R.drawable.img_unknown; break; // No image for Unbound Hoopa yet
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("img_pkmn_")
                        .append(nationalIdFormatted)
                        .append("_")
                        .append(name.toLowerCase());
                if (isFormMega) {
                    stringBuilder.append("_mega");
                    switch (pkmnId) {
                        case 10034:
                        case 10043:
                            stringBuilder.append("_x");
                            break;
                        case 10035:
                        case 10044:
                            stringBuilder.append("_y");
                            break;
                    }
                }
                Context mContext = imageView.getContext();
                resId = mContext.getResources().getIdentifier(stringBuilder.toString(), "drawable", mContext.getPackageName());
                break;
        }
        imageView.setImageResource(resId);
    }
}
