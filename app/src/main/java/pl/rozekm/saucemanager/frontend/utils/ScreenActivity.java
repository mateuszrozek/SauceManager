package pl.rozekm.saucemanager.frontend.utils;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;

public class ScreenActivity extends AppCompatActivity {

    @BindView(R.id.textViewAlarmScreen)
    TextView textViewAlarmScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        ButterKnife.bind(this);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
        ringtone.play();

        String title = (String) getIntent().getExtras().get("title");
        textViewAlarmScreen.setText(title);

        SwipeButton swipeButton = findViewById(R.id.swipeButton);
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Toast.makeText(ScreenActivity.this, "State: " + active, Toast.LENGTH_SHORT).show();
                onBackPressed();
                ringtone.stop();
            }
        });
    }
}
