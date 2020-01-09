package pl.rozekm.saucemanager.frontend.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.backend.database.model.enums.Frequency;
import pl.rozekm.saucemanager.backend.database.viewmodels.RemindersViewModel;
import pl.rozekm.saucemanager.frontend.utils.FrequenciesConverter;
import pl.rozekm.saucemanager.frontend.utils.adapters.FrequenciesAdapter;

public class ReminderCrudActivity extends AppCompatActivity {


    @BindView(R.id.buttonUpdateAddReminder)
    Button buttonUpdateAddReminder;

    @BindView(R.id.buttonDeleteReminder)
    Button buttonDeleteReminder;

    @BindView(R.id.textInputLayoutTitleReminder)
    TextInputLayout textInputLayoutTitleReminder;

    @BindView(R.id.alarmDatePicker)
    DatePicker alarmDatePicker;

    @BindView(R.id.alarmTimePicker)
    TimePicker alarmTimePicker;

    @BindView(R.id.spinnerFrequency)
    Spinner spinnerFrequency;

    private ArrayAdapter<String> frequenciesAdapter;

    RemindersViewModel remindersViewModel;

    private Reminder reminder;

    FrequenciesConverter frequenciesConverter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_crud);
        ButterKnife.bind(this);

        frequenciesConverter = new FrequenciesConverter();

        remindersViewModel = ViewModelProviders.of(this).get(RemindersViewModel.class);

        ArrayList<Frequency> frequencies = new ArrayList<>();
        frequencies.add(Frequency.DAILY);
        frequencies.add(Frequency.WEEKLY);
        frequencies.add(Frequency.MONTHLY);
        frequencies.add(Frequency.YEARLY);
        frequencies.add(Frequency.MINUTELY);
        frequenciesAdapter = new ArrayAdapter<>(ReminderCrudActivity.this, android.R.layout.simple_spinner_dropdown_item, frequenciesConverter.getFrequencies());

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reminder.setFrequency(frequenciesConverter.stringToEnum(frequenciesAdapter.getItem(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerFrequency.setAdapter(frequenciesAdapter);

        reminder = (Reminder) getIntent().getSerializableExtra("reminder");
        if (reminder != null) {
            prepareLayoutForUpdate();
        } else {
            reminder = new Reminder();
            prepareLayoutForAddition();
        }
    }


    private void prepareLayoutForUpdate() {
        textInputLayoutTitleReminder.getEditText().setText(reminder.getTitle());
        buttonUpdateAddReminder.setText(R.string.update_button);
        alarmDatePicker.updateDate(reminder.getDate().getYear(), reminder.getDate().getMonthValue(), reminder.getDate().getDayOfMonth());
        alarmTimePicker.setHour(reminder.getDate().getHour());
        alarmTimePicker.setMinute(reminder.getDate().getMinute());

        int spinnerFrequencyPosition = frequenciesAdapter.getPosition(frequenciesConverter.enumToString(reminder.getFrequency()));
        spinnerFrequency.setSelection(spinnerFrequencyPosition);

        buttonUpdateAddReminder.setOnClickListener(v -> {
            reminder.setTitle(textInputLayoutTitleReminder.getEditText().getText().toString());
            reminder.setDate(LocalDateTime.of(
                    alarmDatePicker.getYear(),
                    alarmDatePicker.getMonth(),
                    alarmDatePicker.getDayOfMonth(),
                    alarmTimePicker.getHour(),
                    alarmTimePicker.getMinute()
            ));

            remindersViewModel.update(reminder);
            onBackPressed();
            Toast.makeText(ReminderCrudActivity.this, "Przypomnienie zaktualizowane", Toast.LENGTH_SHORT).show();
        });

        buttonDeleteReminder.setOnClickListener(v -> {
            remindersViewModel.delete(reminder);
            onBackPressed();
            Toast.makeText(ReminderCrudActivity.this, "Przypomnienie usunięte", Toast.LENGTH_SHORT).show();
        });
    }

    private void prepareLayoutForAddition() {
        buttonUpdateAddReminder.setText(getString(R.string.add_button));

        buttonUpdateAddReminder.setOnClickListener(v -> {
            reminder.setTitle(textInputLayoutTitleReminder.getEditText().getText().toString());

            reminder.setDate(LocalDateTime.of(
                    alarmDatePicker.getYear(),
                    alarmDatePicker.getMonth() + 1,
                    alarmDatePicker.getDayOfMonth(),
                    alarmTimePicker.getHour(),
                    alarmTimePicker.getMinute()
            ));

            remindersViewModel.insert(reminder);
            onBackPressed();
            Toast.makeText(ReminderCrudActivity.this, "Przypomnienie dodane", Toast.LENGTH_SHORT).show();
        });

        buttonDeleteReminder.setOnClickListener(v -> {
            onBackPressed();
            Toast.makeText(ReminderCrudActivity.this, "Powrót", Toast.LENGTH_SHORT).show();
        });
    }
}
