package com.example.pagingtaskjava;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuoteAPI {

    @GET("quotes")
    Call<QuoteResponse> getQuotes(@Query("page") int page);
}
