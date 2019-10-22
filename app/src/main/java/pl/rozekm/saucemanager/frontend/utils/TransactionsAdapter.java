package pl.rozekm.saucemanager.frontend.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder> {
    private ArrayList<Transaction> transactions;

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView category;
        public TextView amount;
        public TextView date;


        public TransactionsViewHolder(CardView v) {
            super(v);
            cardView = v;
            category = v.findViewById(R.id.transaction_item_category);
            amount = v.findViewById(R.id.transaction_item_amount);
            date = v.findViewById(R.id.transaction_item_date);
        }
    }

    public TransactionsAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public TransactionsAdapter() {

    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public TransactionsAdapter.TransactionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactions_item, parent, false);
        TransactionsViewHolder vh = new TransactionsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TransactionsViewHolder holder, int position) {

        holder.category.setText(transactions.get(position).getCategory().toString());
        holder.amount.setText(String.format(Locale.forLanguageTag("PL"),"%5.2f", transactions.get(position).getAmount()) + " zł");
        holder.date.setText(transactions.get(position).getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
    }

    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();
        else return 0;
    }
}
