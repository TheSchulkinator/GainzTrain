package theschulk.com.gainztrain.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import theschulk.com.gainztrain.R;

public class AddMuscleGroupActivity extends AppCompatActivity {

    @BindView(R.id.text_view_select_back) TextView mSelectBack;
    @BindView(R.id.text_view_select_bicep) TextView mSelectBicep;
    @BindView(R.id.text_view_select_chest) TextView mSelectChest;
    @BindView(R.id.text_view_select_legs) TextView mSelectLegs;
    @BindView(R.id.text_view_select_shoulder) TextView mSelectShoulders;
    @BindView(R.id.text_view_select_tricep) TextView mSelectTricep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_muscle_group);
        ButterKnife.bind(this);

        //Setup onClickHandlers
        mSelectShoulders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}
