package pl.rozekm.saucemanager.frontend.activities;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
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
import pl.rozekm.saucemanager.frontend.utils.adapters.TypesAdapter;

public class TransactionCrudActivity extends AppCompatActivity {

    @BindView(R.id.textInputLayoutTitle)
    TextInputLayout textInputLayoutTitle;

    @BindView(R.id.textViewTitleReminder)
    TextView textViewDate;

    @BindView(R.id.textInputLayoutAmount)
    TextInputLayout textInputLayoutAmount;

    @BindView(R.id.spinnerType)
    Spinner spinnerType;

    @BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;

    @BindView(R.id.buttonUpdateAddReminder)
    Button buttonUpdate;

    @BindView(R.id.buttonDeleteReminder)
    Button buttonDelete;

    private TypesAdapter typesAdapter;
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

        ArrayList<TransactionType> types = new ArrayList<>();
        types.add(TransactionType.INCOME);
        types.add(TransactionType.OUTCOME);

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

        typesAdapter = new TypesAdapter(TransactionCrudActivity.this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(typesAdapter);

        outcomesAdapter = new CategoriesAdapter(TransactionCrudActivity.this, android.R.layout.simple_spinner_dropdown_item, outcomes);
        incomesAdapter = new CategoriesAdapter(TransactionCrudActivity.this, android.R.layout.simple_spinner_dropdown_item, incomes);

        transaction = (Transaction) getIntent().getSerializableExtra("trans");

        if (transaction != null) {
            prepareLayoutForUpdate();
            TransactionType type = transaction.getType();
            TransactionCategory category = transaction.getCategory();
            textInputLayoutTitle.getEditText().setText(transaction.getTitle());
            textViewDate.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
            textInputLayoutAmount.getEditText().setText(String.format(Locale.forLanguageTag("PL"), "%5.2f", transaction.getAmount()));

            int spinnerTypePosition = typesAdapter.getPosition(type);
            spinnerType.setSelection(spinnerTypePosition);

            if (type == TransactionType.OUTCOME) {
                spinnerCategory.setAdapter(outcomesAdapter);
                int spinnerCatPosition = outcomesAdapter.getPosition(category);
                spinnerCategory.setSelection(spinnerCatPosition, true);
            } else {
                spinnerCategory.setAdapter(incomesAdapter);
                int spinnerCatPosition = incomesAdapter.getPosition(category);
                spinnerCategory.setSelection(spinnerCatPosition, true);
            }
        } else {
            transaction = new Transaction();
            prepareLayoutForAddition();
        }

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transaction.setType(typesAdapter.getItem(position));

                if (typesAdapter.getItem(position) == TransactionType.OUTCOME) {
                    spinnerCategory.setAdapter(outcomesAdapter);
                } else {
                    spinnerCategory.setAdapter(incomesAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        textInputLayoutAmount.getEditText().setText(transaction.getTitle());
        textInputLayoutTitle.getEditText().setText(transaction.getAmount().toString());

        buttonUpdate.setOnClickListener(v -> {
            transaction.setTitle(textInputLayoutTitle.getEditText().getText().toString());
            transaction.setAmount(getDoubleFromString(textInputLayoutAmount.getEditText().getText().toString()));
            transactionsViewModel.update(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "updated", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v -> {
            transactionsViewModel.delete(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "deleted", Toast.LENGTH_SHORT).show();
        });
    }

    private void prepareLayoutForAddition() {
        buttonUpdate.setText(getString(R.string.add_button));
        textInputLayoutAmount.getEditText().setHint(getString(R.string.type_in_amount_to_update));
        textInputLayoutTitle.getEditText().setHint(getString(R.string.type_in_title_to_update));

        buttonUpdate.setOnClickListener(v -> {
            transaction.setTitle(textInputLayoutTitle.getEditText().getText().toString());
            transaction.setAmount(getDoubleFromString(textInputLayoutAmount.getEditText().getText().toString()));
            transactionsViewModel.insert(transaction);
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "updated", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v -> {
            onBackPressed();
            Toast.makeText(TransactionCrudActivity.this, "deleted", Toast.LENGTH_SHORT).show();
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
