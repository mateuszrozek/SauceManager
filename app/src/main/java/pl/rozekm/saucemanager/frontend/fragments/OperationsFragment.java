package pl.rozekm.saucemanager.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModel;
import pl.rozekm.saucemanager.backend.database.viewmodels.TransactionsViewModelFactory;
import pl.rozekm.saucemanager.databinding.OperationsFragmentBinding;
import pl.rozekm.saucemanager.frontend.utils.adapters.TransactionsAdapter;

public class OperationsFragment extends Fragment {

    private TransactionsViewModel transactionsViewModel;
    private RecyclerView allTransactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;


    public OperationsFragment() {
    }

    public static OperationsFragment newInstance() {

        return new OperationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        OperationsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.operations_fragment, container, false);
        View view = binding.getRoot();

        ButterKnife.bind(this, view);


        transactionsViewModel = ViewModelProviders.of(this, new TransactionsViewModelFactory(getActivity().getApplication(), new Transaction())).get(TransactionsViewModel.class);
        transactionsAdapter = new TransactionsAdapter(getContext());
        getAllTransactions();

        allTransactionsRecyclerView = view.findViewById(R.id.allTransactionsRecyclerView);
        allTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allTransactionsRecyclerView.setHasFixedSize(true);
        allTransactionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        allTransactionsRecyclerView.setAdapter(transactionsAdapter);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
