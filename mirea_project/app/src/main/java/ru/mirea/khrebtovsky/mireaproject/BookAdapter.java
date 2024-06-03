package ru.mirea.khrebtovsky.mireaproject;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.isbnTextView.setText("ISBN: " + book.getIsbn());
        holder.pageCountTextView.setText("Pages: " + book.getPageCount());
        holder.authorsTextView.setText("Authors: " + TextUtils.join(", ", book.getAuthors()));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView isbnTextView;
        TextView pageCountTextView;
        TextView authorsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            isbnTextView = itemView.findViewById(R.id.isbnTextView);
            pageCountTextView = itemView.findViewById(R.id.pageCountTextView);
            authorsTextView = itemView.findViewById(R.id.authorsTextView);
        }
    }
}
