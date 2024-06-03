package ru.mirea.khrebtovsky.mireaproject;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("books")
    Call<List<Book>> getBooks();
}
