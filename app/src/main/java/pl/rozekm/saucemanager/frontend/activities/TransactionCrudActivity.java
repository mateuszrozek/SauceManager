package pl.rozekm.saucemanager.frontend.activities;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionCategory;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModelFactory;
import pl.rozekm.saucemanager.frontend.utils.adapters.CategoriesAdapter;

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

    private CategoriesAdapter outcomesAdapter;
    private CategoriesAdapter incomesAdapter;
    private Transaction transaction;
    private TransactionsViewModel transactionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_crud);
        ButterKnife.bind(this);

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getApplication(), new Transaction())).get(TransactionsViewModel.class);

        ArrayList<TransactionCategory> outcomes = new ArrayList<>();
        outcomes.add(TransactionCategory.CLOTHES);
        outcomes.add(TransactionCategory.ENTERTAINMENT);
        outcomes.add(TransactionCategory.FOOD);
        outcomes.add(TransactionCategory.HEALTH);
        outcomes.add(TransactionCategory.HOUSE);
        outcomes.add(TransactionCategory.SPORT);
        outcomes.add(TransactionCategory.TRANSPORT);
        outcomes.add(TransactionCategory.OTHER);

        ArrayList<TransactionCategory> incomes = new ArrayList<>();
        incomes.add(TransactionCategory.INVESTMENT);
        incomes.add(TransactionCategory.SALARY);
        incomes.add(TransactionCategory.SAVINGS);

        outcomesAdapter = new CategoriesAdapter(TransactionCrudActivity.this, android.R.layout.simple_spinner_dropdown_item, outcomes);
        incomesAdapter = new CategoriesAdapter(TransactionCrudActivity.this, android.R.layout.simple_spinner_dropdown_item, incomes);

        transaction = (Transaction) getIntent().getSerializableExtra("trans");

        if (transaction != null) {
            prepareLayoutForUpdate();
        } else {
            transaction = new Transaction();
            prepareLayoutForAddition();
        }

        switchOperationType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchOperationType.isChecked()) {
                transaction.setType(TransactionType.OUTCOME);
                spinnerCategory.setAdapter(outcomesAdapter);
            } else {
                transaction.setType(TransactionType.INCOME);
                spinnerCategory.setAdapter(incomesAdapter);
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (transaction.getType() == TransactionType.OUTCOME) {
                    transaction.setCategory(outcomesAdapter.getItem(position));
                } else {
                    transaction.setCategory(incomesAdapter.getItem(position));
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

        buttonDelete.setOnClickListener(v -> {
            transactionsViewModel.delete(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "Operacja usunięta", Toast.LENGTH_SHORT).show();
        });

        TransactionType type = transaction.getType();
        TransactionCategory category = transaction.getCategory();
        textInputLayoutTitle.getEditText().setText(transaction.getTitle());
        textViewOperationDate.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        textInputLayoutAmount.getEditText().setText(String.format(Locale.forLanguageTag("PL"), "%5.2f", transaction.getAmount()));

        switchOperationType.setChecked(transaction.getType() == TransactionType.OUTCOME);

        if (type == TransactionType.OUTCOME) {
            spinnerCategory.setAdapter(outcomesAdapter);
            int spinnerCatPosition = outcomesAdapter.getPosition(category);
            spinnerCategory.setSelection(spinnerCatPosition, true);
        } else {
            spinnerCategory.setAdapter(incomesAdapter);
            int spinnerCatPosition = incomesAdapter.getPosition(category);
            spinnerCategory.setSelection(spinnerCatPosition, true);
        }
    }

    private void prepareLayoutForAddition() {
        buttonUpdate.setText(getString(R.string.add_button));
        textViewOperationDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        textViewOperationCrud.setText(getString(R.string.operation_s_crud_screen_add));

        spinnerCategory.setAdapter(outcomesAdapter);

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
