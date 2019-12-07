package pl.rozekm.saucemanager.frontend.activities;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModelFactory;
import pl.rozekm.saucemanager.frontend.utils.CategoriesConverter;

public class TransactionCrudActivity extends AppCompatActivity {

    @BindView(R.id.textInputLayoutTitle)
    TextInputLayout textInputLayoutTitle;

    @BindView(R.id.textViewOperationDate)
    TextView textViewOperationDate;

    @BindView(R.id.textViewOperationCrud)
    TextView textViewOperationCrud;

    @BindView(R.id.textInputLayoutAmount)
    TextInputLayout textInputLayoutAmount;

    @BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;

    @BindView(R.id.buttonUpdateAddReminder)
    Button buttonUpdate;

    @BindView(R.id.buttonDeleteReminder)
    Button buttonDelete;

    @BindView(R.id.switchOperationType)
    Switch switchOperationType;

    private CategoriesConverter categoriesConverter = new CategoriesConverter();
    private ArrayAdapter<String> outcomesArrayAdapter;
    private ArrayAdapter<String> incomesArrayAdapter;

    private Transaction transaction;
    private TransactionsViewModel transactionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_crud);
        ButterKnife.bind(this);

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getApplication(), new Transaction())).get(TransactionsViewModel.class);

        incomesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesConverter.getIncomesStrings());
        outcomesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesConverter.getOutcomesStrings());

        transaction = (Transaction) getIntent().getSerializableExtra("trans");
        if (transaction != null) {
            if (transaction.getAmount() == null) {
                prepareLayoutForAdditionWithInitialCategory();
            } else {
                prepareLayoutForUpdate();
            }
        } else {
            transaction = new Transaction();
            prepareLayoutForAddition();
        }

        switchOperationType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchOperationType.isChecked()) {
                transaction.setType(TransactionType.OUTCOME);
                spinnerCategory.setAdapter(outcomesArrayAdapter);
            } else {
                transaction.setType(TransactionType.INCOME);
                spinnerCategory.setAdapter(incomesArrayAdapter);
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (transaction.getType() == TransactionType.OUTCOME) {
                    transaction.setCategory(categoriesConverter.stringToEnum(outcomesArrayAdapter.getItem(position)));
                } else {
                    transaction.setCategory(categoriesConverter.stringToEnum(incomesArrayAdapter.getItem(position)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void prepareLayoutForUpdate() {
        buttonUpdate.setText(getString(R.string.update_button));
        textViewOperationCrud.setText(getString(R.string.operation_s_crud_screen_update));

        buttonUpdate.setOnClickListener(v -> {
            transaction.setTitle(textInputLayoutTitle.getEditText().getText().toString());
            transaction.setAmount(getDoubleFromString(textInputLayoutAmount.getEditText().getText().toString()));
            transactionsViewModel.update(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "Operacja zaktualizowana", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Usunięcie operacji")
                .setMessage("Czy na pewno chcesz usunąć operację?")
                .setPositiveButton("Tak", (dialog, which) -> {
                    transactionsViewModel.delete(transaction);
                    onBackPressed();
                    Toast.makeText(TransactionCrudActivity.this, "Operacja usunięta", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Nie", null)
                .show());

        TransactionType type = transaction.getType();
        String category = categoriesConverter.enumToString(transaction.getCategory());
        textInputLayoutTitle.getEditText().setText(transaction.getTitle());
        textViewOperationDate.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        textInputLayoutAmount.getEditText().setText(String.format(Locale.forLanguageTag("PL"), "%5.2f", transaction.getAmount()));

        switchOperationType.setChecked(transaction.getType() == TransactionType.OUTCOME);

        if (type == TransactionType.OUTCOME) {
            spinnerCategory.setAdapter(outcomesArrayAdapter);
            int spinnerCatPosition = outcomesArrayAdapter.getPosition(category);
            spinnerCategory.setSelection(spinnerCatPosition, true);
        } else {
            spinnerCategory.setAdapter(incomesArrayAdapter);
            int spinnerCatPosition = incomesArrayAdapter.getPosition(category);
            spinnerCategory.setSelection(spinnerCatPosition, true);
        }
    }

    private void prepareLayoutForAddition() {
        buttonUpdate.setText(getString(R.string.add_button));
        textViewOperationDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        textViewOperationCrud.setText(getString(R.string.operation_s_crud_screen_add));

        spinnerCategory.setAdapter(outcomesArrayAdapter);

        buttonUpdate.setOnClickListener(v -> {
            transaction.setTitle(textInputLayoutTitle.getEditText().getText().toString());
            transaction.setAmount(getDoubleFromString(textInputLayoutAmount.getEditText().getText().toString()));
            transactionsViewModel.insert(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "Operacja dodana", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setText(getString(R.string.back_to_main));
        buttonDelete.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_revert, 0);
        buttonDelete.setOnClickListener(v -> {
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "Powrót do menu głównego", Toast.LENGTH_SHORT).show();
        });
    }

    private void prepareLayoutForAdditionWithInitialCategory() {
        buttonUpdate.setText(getString(R.string.add_button));
        textViewOperationDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        textViewOperationCrud.setText(getString(R.string.operation_s_crud_screen_add));

        spinnerCategory.setAdapter(outcomesArrayAdapter);
        int spinnerCatPosition = outcomesArrayAdapter.getPosition(categoriesConverter.enumToString(transaction.getCategory()));
        spinnerCategory.setSelection(spinnerCatPosition, true);

        buttonUpdate.setOnClickListener(v -> {
            transaction.setTitle(textInputLayoutTitle.getEditText().getText().toString());
            transaction.setAmount(getDoubleFromString(textInputLayoutAmount.getEditText().getText().toString()));
            transactionsViewModel.insert(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "Operacja dodana", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setText(getString(R.string.back_to_main));
        buttonDelete.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_revert, 0);
        buttonDelete.setOnClickListener(v -> {
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "Powrót do menu głównego", Toast.LENGTH_SHORT).show();
        });
    }

    private double getDoubleFromString(String s) {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = null;
        try {
            number = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return number.doubleValue();
    }
}
