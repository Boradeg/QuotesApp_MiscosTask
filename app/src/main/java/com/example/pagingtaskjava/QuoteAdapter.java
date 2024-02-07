package com.example.pagingtaskjava;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_QUOTE = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private List<Quote> quotes;
    private boolean isLoading = false;

    public QuoteAdapter(List<Quote> quotes) {
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_QUOTE) {
            View itemView = inflater.inflate(R.layout.item, parent, false);
            return new QuoteViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(itemView);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuoteViewHolder) {
            Quote quote = quotes.get(position);
            ((QuoteViewHolder) holder).quoteTextView.setText("Author -"+ quote.getAuthorName());
            ((QuoteViewHolder) holder).quoteTextView2.setText(quote.getContent());
        } else if (holder instanceof LoadingViewHolder) {
            // Do nothing for loading view
        }
    }

    @Override
    public int getItemCount() {
        return quotes.size() + (isLoading ? 1 : 0); // Add 1 for the loading view if it's visible
    }

    @Override
    public int getItemViewType(int position) {
        if (position < quotes.size()) {
            return VIEW_TYPE_QUOTE;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    public void setLoading(boolean loading) {
        if (isLoading != loading) {
            isLoading = loading;
            if (loading) {
                notifyItemInserted(quotes.size()); // Show loading view
            } else {
                notifyItemRemoved(quotes.size()); // Hide loading view
            }
        }
    }

    public void addQuotes(List<Quote> newQuotes) {
        int startPosition = quotes.size();
        quotes.addAll(newQuotes);
        notifyItemRangeInserted(startPosition, newQuotes.size());
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView quoteTextView;
        TextView quoteTextView2;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteTextView = itemView.findViewById(R.id.name);
            quoteTextView2 = itemView.findViewById(R.id.content);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
