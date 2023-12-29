package com.example.mobile_apis_framework;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String _id;
    private String author;

    private String ISBN;
    private String booksName;


    public Book(String title, String author, double rating, String ISBN, String booksName) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.ISBN = ISBN;
        this.booksName = booksName;
    }
 public Book(String title, String author, double rating) {
        this.title = title;
        this.author = author;
        this.rating = rating;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    private double rating;


    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getIsbn() {
        return ISBN;
    }

    public void setIsbn(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getBooksName() {
        return booksName;
    }

    public void setBooksName(String booksName) {
        this.booksName = booksName;
    }
}
