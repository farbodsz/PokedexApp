package com.satsumasoftware.pokedex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.move.MiniMove;
import com.satsumasoftware.pokedex.entities.move.Move;
import com.satsumasoftware.pokedex.entities.move.MoveEffectProse;
import com.satsumasoftware.pokedex.ui.filter.FilterResultsActivity;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.DataUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.InfoUtils;


public class MovesDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVE = "MOVE";

    private Move mMove;

    private TextView tvId, tvMove, tvDescription, tvType, tvCategory, tvContest, tvPp, tvPower, tvAccuracy, tvGen;
    private Button btnFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_move);

        MiniMove miniMove = getIntent().getExtras().getParcelable(EXTRA_MOVE);
        if (miniMove == null) {
            throw new NullPointerException("Parcelable MiniMove object through Intent is null");
        }
        mMove = miniMove.toMove(this);

        tvId = (TextView) findViewById(R.id.moveDetail_tvID);
        tvMove = (TextView) findViewById(R.id.moveDetail_tvTitle);
        tvDescription = (TextView) findViewById(R.id.moveDetail_tvDescription);
        tvType = (TextView) findViewById(R.id.moveDetail_tvType_info);
        tvCategory = (TextView) findViewById(R.id.moveDetail_tvCategory_info);
        tvContest = (TextView) findViewById(R.id.moveDetail_tvContest_info);
        tvPp = (TextView) findViewById(R.id.moveDetail_tvPP_info);
        tvPower = (TextView) findViewById(R.id.moveDetail_tvPower_info);
        tvAccuracy = (TextView) findViewById(R.id.moveDetail_tvAccuracy_info);
        tvGen = (TextView) findViewById(R.id.moveDetail_tvGeneration_info);

        btnFilter = (Button) findViewById(R.id.moveDetail_btnFilter);

        setLayouts();
    }

    private void setLayouts() {
        tvId.setText("# " + mMove.getId());
        tvMove.setText(mMove.getName());

        tvDescription.setText(MoveEffectProse.getEffect(this, mMove.getEffectId(), true));

        tvType.setText(DataUtils.typeIdToString(mMove.getTypeId()));
        tvType.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(mMove.getTypeId()));

        setCategoryInfo();

        setContestInfo();

        tvPp.setText(String.valueOf(mMove.getPp()));
        int power = mMove.getPower();
        int accuracy = mMove.getAccuracy();
        /*
        if (power.equals("n/a")) {
            tvPower.setText("---");
        } else {
            tvPower.setText(power);
        }
        if (accuracy.equals("n/a")) {
            tvAccuracy.setText("---");
        } else {
            tvAccuracy.setText(accuracy + "%");
        } */
        tvPower.setText(String.valueOf(power));
        tvAccuracy.setText(accuracy + "%");

        tvGen.setText(InfoUtils.getRomanFromGen(mMove.getGenerationId()));

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Flavours.type == Flavours.Type.PAID) {
                    Intent intent = new Intent(getBaseContext(), FilterResultsActivity.class);
                    intent.putExtra(FilterResultsActivity.FILTER_MOVE, mMove.getId());
                    startActivity(intent);
                } else {
                    AlertUtils.requiresProToast(MovesDetailActivity.this);
                }
            }
        });
    }

    private void setCategoryInfo() { // TODO diff langs
        switch (mMove.getDamageClassId()) {
            case 1:
                tvCategory.setBackgroundResource(R.color.category_status);
                tvCategory.setTextColor(getResources().getColor(R.color.category_status_text));
                tvCategory.setText("Status");
                break;
            case 2:
                tvCategory.setBackgroundResource(R.color.category_physical);
                tvCategory.setTextColor(getResources().getColor(R.color.category_physical_text));
                tvCategory.setText("Physical");
                break;
            case 3:
                tvCategory.setBackgroundResource(R.color.category_special);
                tvCategory.setTextColor(getResources().getColor(R.color.category_special_text));
                tvCategory.setText("Special");
                break;
        }
    }

    private void setContestInfo() {  // TODO langs
        switch (mMove.getContestTypeId()) {
            case 1:
                tvContest.setBackgroundResource(R.color.contest_cool);
                tvContest.setText("Cool");
                break;
            case 2:
                tvContest.setBackgroundResource(R.color.contest_beautiful);
                tvContest.setText("Beautiful");
                break;
            case 3:
                tvContest.setBackgroundResource(R.color.contest_cute);
                tvContest.setText("Cute");
                break;
            case 4:
                tvContest.setBackgroundResource(R.color.contest_clever);
                tvContest.setText("Smart");
                break;
            case 5:
                tvContest.setBackgroundResource(R.color.contest_tough);
                tvContest.setText("Tough");
                break;
        }
    }
}