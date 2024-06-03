package ru.mirea.khrebtovsky.mireaproject;

import java.util.List;

public class Book {
    private int id;
    private String title;
    private String isbn;
    private int pageCount;
    private List<String> authors;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPageCount() {
        return pageCount;
    }



    public List<String> getAuthors() {
        return authors;
    }


}
