package models;

import databases.Serializor;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

public class LibraryCatalog {
    private final ConcurrentHashMap<String, Book> books;
    private final String bookFilePath;
    private int finePerDay = 5;

    public void setFinePerDay(int finePerDay) {
        this.finePerDay = finePerDay;
    }

    public LibraryCatalog() {
        this.bookFilePath = "C:\\Users\\DulithaHasith\\IdeaProjects\\LibraryManagementSystem\\src\\main\\java\\databases\\BookDatabase.ser";
        File file = new File(bookFilePath);

        if (!file.exists()) {
            this.books = new ConcurrentHashMap<>();
        } else {
            this.books = Serializor.deserialize(bookFilePath);
            System.out.println("Deserialized books from the file");
        }
    }

    public synchronized void addBook(Book book) {
        books.put(book.getISBN(), book);
        Serializor.serialize(books, bookFilePath);

        System.out.println("Book added successfully!\n");
        System.out.println("Book: " + book.getISBN() + " " + book.getTitle() + " " + book.getAuthor() + " " + book.getGenre() + " " + book.getPubYear());
    }

    public synchronized void borrowBook(MemberHandling memhandle, String memID, String ISBN) {
        ConcurrentHashMap<String, LibraryMember> members = memhandle.getMembers();

        if (!books.containsKey(ISBN)) {
            System.out.println("Invalid ISBN. Book not found in the catalog.");
            return;
        }

        if (!members.containsKey(memID)) {
            System.out.println("Invalid member ID. Member not found.");
            return;
        }

        LibraryMember member = members.get(memID);

        if (member.getLastRenewDate().plusDays(365).isAfter(LocalDate.now())) {
            if (member.getBorrowed().size() < 2) {
                if(books.get(ISBN).isAvailable()){
                    books.get(ISBN).setAvailable(false);
                    books.get(ISBN).setLastBorrowed(LocalDate.now());
                    Serializor.serialize(books, bookFilePath);
                    member.getBorrowed().add(ISBN);
                    Serializor.serialize(members, memhandle.getMemFilePath());
                    System.out.println("Hand over within two weeks or get fined.");
                }
                else{
                    System.out.println("Book already borrowed");
                }
            } else {
                System.out.println("Maximum books borrowed. Return to borrow again.");
            }
        } else {
            System.out.println("Renew membership to borrow again.");
        }
    }

    public synchronized void returnBook(MemberHandling memhandle, String memID, String ISBN) {
        ConcurrentHashMap<String, LibraryMember> members = memhandle.getMembers();

        if (!books.containsKey(ISBN)) {
            System.out.println("Invalid ISBN. Book not found in the catalog.");
            return;
        }

        if (!members.containsKey(memID)) {
            System.out.println("Invalid member ID. Member not found.");
            return;
        }

        LibraryMember member = members.get(memID);
        List<String> borrowedBooks = member.getBorrowed();

        if (borrowedBooks == null || !borrowedBooks.contains(ISBN)) {
            System.out.println("Book not found in member's borrowed list.");
            return;
        }

        long daysDifference = ChronoUnit.DAYS.between(books.get(ISBN).getLastBorrowed().plusDays(14), LocalDate.now());

        if (daysDifference > 0) {
            long fine = daysDifference * finePerDay;
            System.out.println("Pay fine of " + fine);
        }

        books.get(ISBN).setAvailable(true);
        Serializor.serialize(books, bookFilePath);
        borrowedBooks.remove(ISBN);
        Serializor.serialize(members, memhandle.getMemFilePath());
        System.out.println("Book returned successfully");
    }

    public synchronized String printAll() {
        StringBuilder sb = new StringBuilder();

//        sb.append("All books:\n");
//        sb.append("____________________________________________________________________________________________\n");
//        sb.append("| ISBN              | Title            | Author           | Genre       | Year | Available |\n");
//        sb.append("|-------------------|------------------|------------------|-------------|------|-----------|\n");

        for (Book book : books.values()) {
            sb.append(String.format(
                    "%s, %s, %s, %s, %d, %s\r\n",
                    book.getISBN(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getPubYear(),
                    book.isAvailable() ? "Yes" : "No"
            ));
        }
        sb.append("--------------------------------------------------------------------------------------------\n");

        return sb.toString();
    }

    public synchronized void removeBook(Book book) {
        books.remove(book.getISBN());
    }

    public synchronized String searchByTitle(String s) {
        StringBuilder sb = new StringBuilder();
        List<Book> matchingBooks = books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(s.toLowerCase()))
                .toList();
        for (Book book : matchingBooks) {
            sb.append(String.format(
                    "%s, %s, %s, %s, %d, %s\r\n",
                    book.getISBN(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getPubYear(),
                    book.isAvailable() ? "Yes" : "No"
            ));
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public synchronized String searchByAuthor(String s) {
        StringBuilder sb = new StringBuilder();
        List<Book> matchingBooks = books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(s.toLowerCase()))
                .toList();
        for (Book book : matchingBooks) {
            sb.append(String.format(
                    "%s, %s, %s, %s, %d, %s\r\n",
                    book.getISBN(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getPubYear(),
                    book.isAvailable() ? "Yes" : "No"
            ));
        }
        return sb.toString();
    }

    public synchronized String groupByGenre(Book.Genre genre) {
        StringBuilder sb = new StringBuilder();
        Map<Book.Genre, List<Book>> booksOfGenres = books.values().stream()
                .collect(Collectors.groupingBy(Book::getGenre));
        if (booksOfGenres.containsKey(genre)) {
            for (Book book : booksOfGenres.get(genre)) {
                sb.append(String.format(
                        "%s, %s, %s, %s, %d, %s\r\n",
                        book.getISBN(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        book.getPubYear(),
                        book.isAvailable() ? "Yes" : "No"
                ));
            }
            return sb.toString();
        } else {
            return ("No books found for genre: " + genre);
        }
    }

    public synchronized String recommendBooks(Book.Genre genre, int yearAfter) {
        StringBuilder sb = new StringBuilder();
        List<Book> recommended = books.values().stream()
                .filter(book -> book.getGenre() == genre)
                .filter(book -> book.getPubYear() > yearAfter)
                .sorted(Comparator.comparingInt(Book::getPubYear))
                .toList();
        for (Book book : recommended) {
            sb.append(String.format(
                    "%s, %s, %s, %s, %d, %s\r\n",
                    book.getISBN(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getPubYear(),
                    book.isAvailable() ? "Yes" : "No"
            ));
        }
        return sb.toString();
    }
}
