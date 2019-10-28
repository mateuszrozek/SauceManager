package pl.rozekm.saucemanager.frontend.utils;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder> {
    private List<Transaction> transactions;

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView category;
        public TextView amount;
        public TextView date;
        public TextView title;


        public TransactionsViewHolder(CardView v) {
            super(v);
            cardView = v;
            category = v.findViewById(R.id.transaction_item_category);
            amount = v.findViewById(R.id.transaction_item_amount);
            date = v.findViewById(R.id.transaction_item_date);
            title = v.findViewById(R.id.transaction_item_title);
        }
    }

    public TransactionsAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public TransactionsAdapter() {

    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public TransactionsAdapter.TransactionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_item, parent, false);
        TransactionsViewHolder vh = new TransactionsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TransactionsViewHolder holder, int position) {

        holder.category.setText(transactions.get(position).getCategory().toString());
        holder.category.setTextColor(chooseColor(transactions.get(position)));
        holder.amount.setText(String.format(Locale.forLanguageTag("PL"),"%5.2f", transactions.get(position).getAmount()) + " zł");
        holder.date.setText(transactions.get(position).getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        holder.title.setText(givenUsingPlainJava_whenGeneratingRandomStringBounded_thenCorrect());
    }

    private int chooseColor(Transaction transaction) {
        if (transaction.getType()== TransactionType.OUTCOME){
            return Color.RED;
        }
        else return Color.GREEN;
    }

    public String givenUsingPlainJava_whenGeneratingRandomStringBounded_thenCorrect() {


        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        System.out.println(generatedString);

        return generatedString;
    }

    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();
        else return 0;
    }
}
