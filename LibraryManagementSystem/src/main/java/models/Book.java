package models;
import exceptions.InvalidYearException;
import validators.YearValidator;
import validators.IsbnValidator;
import exceptions.InvalidIsbnException;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.concurrent.locks.ReentrantLock;


public class Book implements Serializable {
    private final ReentrantLock lock = new ReentrantLock();
    private String bookID;
    private String ISBN;
    private String title;
    private String author;

    public Book() {

    }

    public Book(String isbn, String title, String author, int year) {
        this.ISBN = isbn;
        this.title = title;
        this.author = author;
        this.pubYear = year;
    }

    public enum Genre {LITERARY,MYSTERY,THRILLER,HORROR, HISTORICAL};
    private Genre genre;
    private int pubYear;
    private boolean isAvailable;
    private LocalDate lastBorrowed;

//    public LocalDate getLastBorrowed() {
//        return lastBorrowed;
//    }
//
//    public void setLastBorrowed(LocalDate lastBorrowed) {
//        this.lastBorrowed = lastBorrowed;
//    }

    public Book(String isbn, String title, String author, Genre genre, int pubYear, boolean b) {
        this.ISBN= isbn;
        this.title=title;
        this.author=author;
        this.genre=genre;
        this.pubYear=pubYear;
        this.isAvailable=b;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) throws InvalidIsbnException {
        if (IsbnValidator.isValid(ISBN)){
            this.ISBN = ISBN;
        }
        else{
            throw new InvalidIsbnException("Invalid ISBN format: "+ ISBN);
        }
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

//    public boolean isAvailable() {
//        return isAvailable;
//    }
//
//    public void setAvailable(boolean available) {
//        isAvailable = available;
//    }

    public int getPubYear() {
        return pubYear;
    }

    public void setPubYear(int pubYear) throws InvalidYearException {
        if (YearValidator.isValid(pubYear)){
            this.pubYear = pubYear;
        }
        else{
            throw new InvalidYearException("Input year invalid: "+ pubYear);
        }
    }

    public boolean isAvailable() {
        lock.lock();
        try {
            return isAvailable;
        } finally {
            lock.unlock();
        }
    }

    public void setAvailable(boolean available) {
        lock.lock();
        try {
            isAvailable = available;
        } finally {
            lock.unlock();
        }
    }

    public LocalDate getLastBorrowed() {
        lock.lock();
        try {
            return lastBorrowed;
        } finally {
            lock.unlock();
        }
    }

    public void setLastBorrowed(LocalDate lastBorrowed) {
        lock.lock();
        try {
            this.lastBorrowed = lastBorrowed;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Book [ISBN=" + ISBN + ", Title=" + title + ", Author=" + author + ", Genre=" + genre + ", Year=" + pubYear + ", Available=" + (isAvailable ? "Yes" : "No") + "]";
    }

}