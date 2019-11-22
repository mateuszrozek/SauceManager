package pl.rozekm.saucemanager.frontend.activities;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.frontend.utils.adapters.TransactionsAdapter;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModelFactory;

public class AllTransactionsActivity extends AppCompatActivity {

    private TransactionsViewModel transactionsViewModel;
    private RecyclerView allTransactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;


    @BindView(R.id.buttonBackAllTrans)
    Button buttonBackAllTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);
        ButterKnife.bind(this);

        buttonBackAllTrans.setOnClickListener(v -> onBackPressed());

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getApplication(), new Transaction())).get(TransactionsViewModel.class);
        transactionsAdapter = new TransactionsAdapter(AllTransactionsActivity.this);
        getAllTransactions();

        allTransactionsRecyclerView = findViewById(R.id.allTransactionsRecyclerView);
        allTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTransactionsRecyclerView.setHasFixedSize(true);
        allTransactionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allTransactionsRecyclerView.setAdapter(transactionsAdapter);
    }

    private void getAllTransactions() {

        transactionsViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                transactionsAdapter.setTransactions(transactions);
                transactionsAdapter.notifyDataSetChanged();
            }
        });
    }
}
