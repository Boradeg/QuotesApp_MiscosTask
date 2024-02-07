package com.example.pagingtaskjava;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuoteAdapter adapter;
    private boolean isLoading = false;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuoteAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Add scroll listener to RecyclerView
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Load more items if reached the end and not already loading
                    loadMoreItems();
                }
            }
        });

        // Initial API call to fetch the first page
        fetchQuotes();
    }

    private void fetchQuotes() {
        isLoading = true;
        adapter.setLoading(true); // Show loading view
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.quotable.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuoteAPI quoteAPI = retrofit.create(QuoteAPI.class);
        Call<QuoteResponse> call = quoteAPI.getQuotes(currentPage);
        call.enqueue(new Callback<QuoteResponse>() {
            @Override
            public void onResponse(Call<QuoteResponse> call, Response<QuoteResponse> response) {
                if (response.isSuccessful()) {
                    QuoteResponse quoteResponse = response.body();
                    if (quoteResponse != null && !quoteResponse.getResults().isEmpty()) {
                        List<Quote> quotes = quoteResponse.getResults();
                        adapter.addQuotes(quotes);
                        currentPage++;
                    }
                }
                isLoading = false;
                adapter.setLoading(false); // Hide loading view
            }

            @Override
            public void onFailure(Call<QuoteResponse> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
                adapter.setLoading(false); // Hide loading view
            }
        });
    }

    private void loadMoreItems() {
        fetchQuotes();
    }
}
