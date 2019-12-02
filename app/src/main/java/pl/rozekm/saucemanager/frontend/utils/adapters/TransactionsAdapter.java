package pl.rozekm.saucemanager.frontend.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pl.rozekm.saucemanager.R;
import pl.rozekm.saucemanager.backend.database.model.Transaction;
import pl.rozekm.saucemanager.backend.database.model.enums.TransactionType;
import pl.rozekm.saucemanager.frontend.activities.TransactionCrudActivity;
import pl.rozekm.saucemanager.frontend.utils.CategoriesConverter;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    private CategoriesConverter categoriesConverter = new CategoriesConverter();

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

    public TransactionsAdapter(Context context) {
        this.context = context;
    }

    public List<Transaction> getTransactions() {
        return transactions;
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

        Transaction trans = transactions.get(position);

        holder.category.setText(categoriesConverter.enumToString(trans.getCategory()));
        holder.category.setTextColor(chooseColor(trans));
        holder.amount.setText(String.format(Locale.forLanguageTag("PL"), "%7.2f", trans.getAmount()) + " zł");
        holder.date.setText(trans.getDate().format(DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss")));
        if (trans.getTitle() == null || trans.getTitle().isEmpty()) {
            holder.title.setText("No title provided");
        } else {
            holder.title.setText(trans.getTitle());
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransactionCrudActivity.class);
            intent.putExtra("trans", trans);
            context.startActivity(intent);
        });
    }

    private int chooseColor(Transaction transaction) {
        if (transaction.getType() == TransactionType.OUTCOME) {
            return Color.RED;
        } else return Color.GREEN;
    }


    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();
        else return 0;
    }
}
