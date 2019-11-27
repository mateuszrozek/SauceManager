package pl.rozekm.saucemanager.frontend.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import pl.rozekm.saucemanager.R;

public class OperationsFragment extends Fragment {


    public OperationsFragment() {
    }

    public static OperationsFragment newInstance() {

        return new OperationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.operations_fragment, container, false);
    }

}
