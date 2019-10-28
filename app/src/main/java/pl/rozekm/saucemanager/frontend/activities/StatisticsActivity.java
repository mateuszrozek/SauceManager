package pl.rozekm.saucemanager.frontend.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;

public class StatisticsActivity extends AppCompatActivity {

    @BindView(R.id.buttonBackStats)
    Button buttonBackStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

        buttonBackStats.setOnClickListener(v -> onBackPressed());
    }
}
