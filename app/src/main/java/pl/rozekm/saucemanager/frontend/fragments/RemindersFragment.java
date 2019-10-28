package pl.rozekm.saucemanager.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Reminder;
import pl.rozekm.saucemanager.databinding.RemindersFragmentBinding;
import pl.rozekm.saucemanager.frontend.utils.RemindersAdapter;
import pl.rozekm.saucemanager.frontend.viewmodels.RemindersViewModel;

public class RemindersFragment extends Fragment {

    private RemindersViewModel remindersViewModel;
    private RecyclerView remindersRecyclerView;

    private RemindersAdapter remindersAdapter;

    public static RemindersFragment newInstance() {
        return new RemindersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remindersViewModel = ViewModelProviders.of(this).get(RemindersViewModel.class);
        remindersAdapter = new RemindersAdapter();
        getReminders();

    }

    private void getReminders() {
        remindersViewModel.getAllReminders().observe(RemindersFragment.this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                remindersAdapter.setReminders(reminders);
                remindersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RemindersFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.reminders_fragment, container, false);
        View view = binding.getRoot();

        ButterKnife.bind(this, view);

        remindersRecyclerView = view.findViewById(R.id.remindersRecyclerView);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        remindersRecyclerView.setHasFixedSize(true);
        remindersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        remindersRecyclerView.setAdapter(remindersAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
