package pl.rozekm.saucemanager.frontend.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

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
import pl.rozekm.saucemanager.frontend.utils.adapters.CategoriesAdapter;
import pl.rozekm.saucemanager.frontend.utils.adapters.TypesAdapter;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.frontend.viewmodels.TransactionsViewModelFactory;

public class TransactionCRUDActivity extends AppCompatActivity {

    @BindView(R.id.textInputLayoutTitle)
    TextInputLayout textInputLayoutTitle;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textInputLayoutAmount)
    TextInputLayout textInputLayoutAmount;

    @BindView(R.id.spinnerType)
    Spinner spinnerType;

    @BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;

    @BindView(R.id.buttonUpdate)
    Button buttonUpdate;

    @BindView(R.id.buttonDelete)
    Button buttonDelete;

    TypesAdapter typesAdapter;
    CategoriesAdapter outcomesAdapter;
    CategoriesAdapter incomesAdapter;

    Transaction transaction;

    TransactionsViewModel transactionsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_crud);
        ButterKnife.bind(this);

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getApplication(), new Transaction())).get(TransactionsViewModel.class);

        transaction = (Transaction) getIntent().getExtras().get("trans");
        TransactionType type = transaction.getType();
        TransactionCategory category = transaction.getCategory();

        textInputLayoutTitle.getEditText().setText(transaction.getTitle());
        textViewDate.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        textInputLayoutAmount.getEditText().setText(String.format(Locale.forLanguageTag("PL"), "%5.2f", transaction.getAmount()));

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

        typesAdapter = new TypesAdapter(TransactionCRUDActivity.this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(typesAdapter);

        outcomesAdapter = new CategoriesAdapter(TransactionCRUDActivity.this, android.R.layout.simple_spinner_dropdown_item, outcomes);
        incomesAdapter = new CategoriesAdapter(TransactionCRUDActivity.this, android.R.layout.simple_spinner_dropdown_item, incomes);

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

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transaction.setType(typesAdapter.getItem(position));

                Toast.makeText(TransactionCRUDActivity.this, "type: " + transaction.getType().toString(),
                        Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(TransactionCRUDActivity.this, "cat: " + transaction.getCategory().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    transaction.setCategory(incomesAdapter.getItem(position));
                    Toast.makeText(TransactionCRUDActivity.this, "cat: " + transaction.getCategory().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonUpdate.setOnClickListener(v -> {
            transaction.setTitle(textInputLayoutTitle.getEditText().getText().toString());
            transaction.setAmount(Double.parseDouble(textInputLayoutAmount.getEditText().getText().toString()));
            transactionsViewModel.update(transaction);
            Toast.makeText(TransactionCRUDActivity.this, "updated", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v -> {
            transactionsViewModel.delete(transaction);
            onBackPressed();
            Toast.makeText(TransactionCRUDActivity.this, "deleted", Toast.LENGTH_SHORT).show();
        });
    }
}
