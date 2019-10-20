package pl.rozekm.saucemanager.frontend.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.rozekm.saucemanager.backend.database.model.Transaction;


public class TransactionsViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private Transaction transaction;


    public TransactionsViewModelFactory(Application application, Transaction transaction){
        this.application = application;
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TransactionsViewModel(application, transaction);
    }
}
