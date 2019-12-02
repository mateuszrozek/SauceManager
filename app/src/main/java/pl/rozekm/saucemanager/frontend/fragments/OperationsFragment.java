package pl.rozekm.saucemanager.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModelFactory;
import pl.rozekm.saucemanager.databinding.OperationsFragmentBinding;
import pl.rozekm.saucemanager.frontend.utils.CategoriesConverter;
import pl.rozekm.saucemanager.frontend.utils.TransactionsSorter;
import pl.rozekm.saucemanager.frontend.utils.adapters.TransactionsAdapter;

public class OperationsFragment extends Fragment {

    @BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;

    @BindView(R.id.numberOfOperationsTextView)
    TextView numberOfOperationsTextView;

    private TransactionsViewModel transactionsViewModel;
    private RecyclerView allTransactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;

    private List<Transaction> allTransacations;

    CategoriesConverter categoriesConverter;
    ArrayAdapter<String> operationStringAdapter;
    TransactionsSorter transactionsSorter;


    public OperationsFragment() {
    }

    public static OperationsFragment newInstance() {

        return new OperationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        OperationsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.operations_fragment, container, false);
        View view = binding.getRoot();

        ButterKnife.bind(this, view);

        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getActivity().getApplication(), new Transaction())).get(TransactionsViewModel.class);
        transactionsAdapter = new TransactionsAdapter(getContext());
        transactionsViewModel.getAllTransactions().observe(this, transactions -> {
            transactionsAdapter.setTransactions(transactions);
            transactionsAdapter.notifyDataSetChanged();
            numberOfOperationsTextView.setText(getString(R.string.liczba_operacji, transactionsAdapter.getItemCount()));
            allTransacations = transactions;
        });

        allTransactionsRecyclerView = view.findViewById(R.id.allTransactionsRecyclerView);
        allTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allTransactionsRecyclerView.setHasFixedSize(true);
        allTransactionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allTransactionsRecyclerView.setAdapter(transactionsAdapter);

        transactionsSorter = new TransactionsSorter(allTransacations);
        categoriesConverter = new CategoriesConverter();
        operationStringAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriesConverter.getOperationsStrings());
        spinnerCategory.setAdapter(operationStringAdapter);


        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transactionsAdapter.setTransactions(
                        transactionsSorter.sortByCategory(
                                allTransacations,
                                categoriesConverter.stringToEnum((String) spinnerCategory.getSelectedItem())
                        )
                );
                numberOfOperationsTextView.setText(getString(R.string.liczba_operacji, transactionsAdapter.getItemCount()));
                transactionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
