package com.satsumasoftware.pokedex.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.satsumasoftware.pokedex.FilterResultsActivity;
import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniMove;
import com.satsumasoftware.pokedex.object.Move;
import com.satsumasoftware.pokedex.util.AlertUtils;
import com.satsumasoftware.pokedex.util.Flavours;
import com.satsumasoftware.pokedex.util.InfoUtils;


public class MovesDetailActivity extends AppCompatActivity {

    private Move mMove;

    private TextView tvId, tvMove, tvDescription, tvType, tvCategory, tvContest, tvPp, tvPower, tvAccuracy, tvGen;
    private Button btnFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_move);

        loadDetail();
    }

    private void loadDetail() {
        receiveIntents();
        castLayouts();
        setLayouts();
    }

    private void receiveIntents() {
        MiniMove miniMove = getIntent().getExtras().getParcelable("MOVE");
        if (miniMove == null) {
            throw new NullPointerException("Parcelable MiniMove object through Intent is null");
        }

        mMove = miniMove.toMove(this);
    }

    private void castLayouts() {
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
    }

    private void setLayouts() {
        tvId.setText("# " + mMove.getMoveId());
        tvMove.setText(mMove.getMove());

        tvDescription.setText(mMove.getDescription());

        tvType.setText(mMove.getType());
        tvType.setBackgroundResource(InfoUtils.getTypeBkgdColorRes(mMove.getType()));

        setCategoryInfo();

        setContestInfo();

        tvPp.setText(String.valueOf(mMove.getPP()));
        String power = mMove.getPower();
        String accuracy = mMove.getAccuracy();
        if (power.equals("n/a")) {
            tvPower.setText("---");
        } else {
            tvPower.setText(power);
        }
        if (accuracy.equals("n/a")) {
            tvAccuracy.setText("---");
        } else {
            tvAccuracy.setText(accuracy + "%");
        }

        tvGen.setText(InfoUtils.getRomanFromGen(mMove.getGeneration()));

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Flavours.type == Flavours.Type.PAID) {
                    Intent intent = new Intent(getBaseContext(), FilterResultsActivity.class);
                    intent.putExtra("MOVE_ID", mMove.getMoveId());
                    startActivity(intent);
                } else {
                    AlertUtils.requiresProToast(MovesDetailActivity.this);
                }
            }
        });
    }

    private void setCategoryInfo() {
        String category = mMove.getCategory();
        if (category.equalsIgnoreCase("P")) {
            tvCategory.setBackgroundResource(R.color.category_physical);
            tvCategory.setTextColor(getResources().getColor(R.color.category_physical_text));
            tvCategory.setText("Physical");

        } else if (category.equalsIgnoreCase("Sp")) {
            tvCategory.setBackgroundResource(R.color.category_special);
            tvCategory.setTextColor(getResources().getColor(R.color.category_special_text));
            tvCategory.setText("Special");

        } else if (category.equalsIgnoreCase("St")) {
            tvCategory.setBackgroundResource(R.color.category_status);
            tvCategory.setTextColor(getResources().getColor(R.color.category_status_text));
            tvCategory.setText("Status");
        }
    }

    private void setContestInfo() {
        String contest = mMove.getContest();
        if (contest.equalsIgnoreCase("C")) {
            tvContest.setBackgroundResource(R.color.contest_cool);
            tvContest.setText("Cool");

        } else if (contest.equalsIgnoreCase("B")) {
            tvContest.setBackgroundResource(R.color.contest_beautiful);
            tvContest.setText("Beautiful");

        } else if (contest.equalsIgnoreCase("Cu")) {
            tvContest.setBackgroundResource(R.color.contest_cute);
            tvContest.setText("Cute");

        } else if (contest.equalsIgnoreCase("Cl")) {
            tvContest.setBackgroundResource(R.color.contest_clever);
            tvContest.setText("Clever");

        } else if (contest.equalsIgnoreCase("T")) {
            tvContest.setBackgroundResource(R.color.contest_tough);
            tvContest.setText("Tough");
        }
    }
}
