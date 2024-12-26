package com.libApp.LibraryMgtSystem.services;

import com.libApp.LibraryMgtSystem.models.Book;
import com.libApp.LibraryMgtSystem.models.LibraryMember;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class BookService {
    private static final long FINE_PER_DAY = 4;
    private ConcurrentHashMap<String, Book> books;
    private ConcurrentHashMap<String, LibraryMember> members;

    public BookService(){
        books = DataService.loadBooks();
        members=DataService.loadMembers();
    }

    public ConcurrentHashMap<String,Book> getAllBooks(){
        return books;
    }

    public void addBook(Book book){
        books.put(book.getISBN(), book);
    }

    public List<Book> searchByTitle(String title){
        return books.values().stream()
                .filter(book->book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author){
        return books.values().stream()
                .filter(book->book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> groupByGenre(Book.Genre genre){
        Map<Book.Genre, List<Book>> booksOfGenres = books.values().stream()
                .collect(Collectors.groupingBy(Book::getGenre));
        return booksOfGenres.get(genre);
    }

    public List<Book> recommendBooks(Book.Genre genre, int yearAfter){
        return books.values().stream()
                .filter(book -> book.getGenre() == genre)
                .filter(book -> book.getPubYear() > yearAfter)
                .sorted(Comparator.comparingInt(Book::getPubYear))
                .toList();
    }

    public synchronized void borrowBook(String memID, String ISBN) {
        if (!books.containsKey(ISBN)) {
            throw new IllegalArgumentException("Invalid ISBN. Book not found in the catalog.");
        }

        if (!members.containsKey(memID)) {
            throw new IllegalArgumentException("Invalid member ID. Member not found.");
        }

        LibraryMember member = members.get(memID);

        if (member.getLastRenewDate().plusDays(365).isBefore(LocalDate.now())) {
            throw new IllegalStateException("Membership expired. Renew to borrow books.");
        }

        if (member.getBorrowed().size() >= 2) {
            throw new IllegalStateException("Maximum books borrowed. Return a book to borrow more.");
        }

        Book book = books.get(ISBN);

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is already borrowed.");
        }

        // Update book and member details
        book.setAvailable(false);
        book.setLastBorrowed(LocalDate.now());
        member.getBorrowed().add(ISBN);

        // Save changes
        DataService.saveBooks(books);
        DataService.saveMembers(members);

        System.out.println("Book borrowed successfully. Hand over within two weeks to avoid fines.");
    }

    public synchronized void returnBook(String memID, String ISBN) {
        if (!books.containsKey(ISBN)) {
            throw new IllegalArgumentException("Invalid ISBN. Book not found in the catalog.");
        }

        if (!members.containsKey(memID)) {
            throw new IllegalArgumentException("Invalid member ID. Member not found.");
        }

        LibraryMember member = members.get(memID);
        List<String> borrowedBooks = member.getBorrowed();

        if (borrowedBooks == null || !borrowedBooks.contains(ISBN)) {
            throw new IllegalArgumentException("Book not found in member's borrowed list.");
        }

        // Calculate fine if the book is overdue
        long daysOverdue = ChronoUnit.DAYS.between(books.get(ISBN).getLastBorrowed().plusDays(14), LocalDate.now());
        if (daysOverdue > 0) {
            long fine = daysOverdue * FINE_PER_DAY;
            System.out.println("Pay fine of " + fine);
        }

        // Update book and member details
        books.get(ISBN).setAvailable(true);
        borrowedBooks.remove(ISBN);

        // Save changes
        DataService.saveBooks(books);
        DataService.saveMembers(members);

        System.out.println("Book returned successfully.");
    }
}

