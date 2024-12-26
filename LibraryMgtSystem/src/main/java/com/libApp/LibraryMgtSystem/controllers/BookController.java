package com.libApp.LibraryMgtSystem.controllers;

import com.libApp.LibraryMgtSystem.models.Book;
import com.libApp.LibraryMgtSystem.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ConcurrentHashMap<String,Book> getAllBooks(){
        return  bookService.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book){
        bookService.addBook(book);
        System.out.println("Received Book: "+book);
        return ResponseEntity.ok("Book added successfully!");
    }

    @GetMapping("/search")
    public List<Book> searchBooksByTitle(@RequestParam String title){
        return bookService.searchByTitle(title);
    }

    // Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestParam String memID, @RequestParam String ISBN) {
        try {
            bookService.borrowBook(memID, ISBN);
            return ResponseEntity.ok("Book borrowed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Return a book
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam String memID, @RequestParam String ISBN) {
        try {
            bookService.returnBook(memID, ISBN);
            return ResponseEntity.ok("Book returned successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


}
