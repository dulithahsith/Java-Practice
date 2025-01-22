package com.libApp.LibraryMgtSystem.views;

import com.libApp.LibraryMgtSystem.exceptions.InvalidIsbnException;
import com.libApp.LibraryMgtSystem.exceptions.InvalidYearException;
import com.libApp.LibraryMgtSystem.models.Book;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("books")
@Component
public class BookView extends VerticalLayout {
    private RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api/books";
    private Grid<Book> bookGrid = new Grid<>(Book.class);
    private TextField titleField = new TextField("Title");
    private TextField authorField = new TextField("Author");
    private TextField isbnField = new TextField("ISBN");
    private NumberField yearField = new NumberField("Published Year");
    ComboBox<Book.Genre> genreField = new ComboBox<>("Genre");

    // Fields for borrowing and returning books
    private TextField borrowMemIDField = new TextField("Member ID (Borrow)");
    private TextField borrowISBNField = new TextField("Book ISBN (Borrow)");
    private TextField returnMemIDField = new TextField("Member ID (Return)");
    private TextField returnISBNField = new TextField("Book ISBN (Return)");

    private TextField deleteISBNField = new TextField("ISBN (Delete)");

    public BookView() {

        genreField.setItems(Book.Genre.LITERARY, Book.Genre.MYSTERY, Book.Genre.THRILLER, Book.Genre.HORROR, Book.Genre.HISTORICAL);
        genreField.setPlaceholder("Select Genre");
        genreField.setClearButtonVisible(true);

        Button loadBooksButton = new Button("Load Books", event -> loadBooks());

        Button addBookButton = new Button("Add Book", event -> {
            try {
                addBook();
            } catch (InvalidYearException e) {
                Notification.show("Invalid year: " + e.getMessage());
            } catch (InvalidIsbnException e) {
                Notification.show("Invalid ISBN: " + e.getMessage());
            }
        });

        ComboBox<String> searchTypeDropdown = new ComboBox<>("Search by");
        searchTypeDropdown.setItems("Title", "Author", "Genre");
        searchTypeDropdown.setPlaceholder("Select Search Type");
        searchTypeDropdown.setClearButtonVisible(true);

        TextField searchInputField = new TextField("Search Input");

        Button searchButton = new Button("Search", event -> {
            String searchType = searchTypeDropdown.getValue();
            String searchInput = searchInputField.getValue();

            if (searchType == null || searchInput == null || searchInput.isEmpty()) {
                Notification.show("Please select a search type and provide input.");
                return;
            }

            switch (searchType) {
                case "Title":
                    searchBooksByTitle(searchInput);
                    break;
                case "Author":
                    searchBooksByAuthor(searchInput);
                    break;
                case "Genre":
                    searchBooksByGenre(searchInput);
                    break;
                default:
                    Notification.show("Invalid search type selected.");
            }
        });

        Button borrowBookButton = new Button("Borrow Book", event -> borrowBook());

        Button returnBookButton = new Button("Return Book", event -> returnBook());

        Button deleteBookButton = new Button("Delete Book", event -> deleteBook());

        add(
                new HorizontalLayout(loadBooksButton),
                new HorizontalLayout(searchTypeDropdown, searchInputField),
                searchButton,
                new HorizontalLayout(titleField, authorField, isbnField, yearField, genreField ),
                addBookButton,
                new HorizontalLayout(borrowMemIDField, borrowISBNField ),
                borrowBookButton,
                new HorizontalLayout(returnMemIDField, returnISBNField ),
                returnBookButton,
                deleteISBNField,
                deleteBookButton,
                bookGrid
        );

    }

    private void loadBooks() {
        List<Book> books = Arrays.asList(restTemplate.getForObject(BASE_URL, Book [].class));
        bookGrid.setItems(books);
    }

    private void addBook() throws InvalidYearException, InvalidIsbnException {
        Book book = new Book(
                isbnField.getValue(),
                titleField.getValue(),
                genreField.getValue(),
                authorField.getValue(),
                yearField.getValue().intValue()
        );
        restTemplate.postForObject(BASE_URL, book, String.class);
        Notification.show("Book added successfully!");
        loadBooks();
    }

    private void searchBooksByTitle(String title) {
        String searchUrl = BASE_URL + "/search?title=" + title;
        try {
            List<Book> books = Arrays.asList(restTemplate.getForObject(searchUrl, Book[].class));
            bookGrid.setItems(books);
            Notification.show("Displaying books with title: " + title);
        } catch (Exception e) {
            Notification.show("Error fetching books: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void searchBooksByAuthor(String author) {
        String searchUrl = BASE_URL + "/search?author=" + author;
        try {
            List<Book> books = Arrays.asList(restTemplate.getForObject(searchUrl, Book[].class));
            bookGrid.setItems(books);
            Notification.show("Displaying books by author: " + author);
        } catch (Exception e) {
            Notification.show("Error fetching books: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void searchBooksByGenre(String genre) {
        try {
            Book.Genre bookGenre = Book.Genre.valueOf(genre.toUpperCase());
            String searchUrl = BASE_URL + "/search?genre=" + bookGenre.name();
            try {
                List<Book> books = Arrays.asList(restTemplate.getForObject(searchUrl, Book[].class));
                bookGrid.setItems(books);
                Notification.show("Displaying books in genre: " + genre);
            } catch (Exception e) {
                Notification.show("Error fetching books by genre: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        } catch (IllegalArgumentException e) {
            Notification.show("Invalid genre selected.", 5000, Notification.Position.MIDDLE);
        }
    }


    private void borrowBook() {
        String memID = borrowMemIDField.getValue();
        String isbn = borrowISBNField.getValue();
        String borrowUrl = BASE_URL + "/borrow?memID=" + memID + "&ISBN=" + isbn;
        try {
            restTemplate.postForObject(borrowUrl, null, String.class);
            Notification.show("Book borrowed successfully!");
            loadBooks();
        } catch (Exception e) {
            Notification.show("Error borrowing book: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void returnBook() {
        String memID = returnMemIDField.getValue();
        String isbn = returnISBNField.getValue();
        String returnUrl = BASE_URL + "/return?memID=" + memID + "&ISBN=" + isbn;  // Construct URL for returning book
        try {
            restTemplate.postForObject(returnUrl, null, String.class);
            Notification.show("Book returned successfully!");
            loadBooks();
        } catch (Exception e) {
            Notification.show("Error returning book: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void deleteBook() {
        String isbn = deleteISBNField.getValue();
        String deleteUrl = BASE_URL + "/delete?ISBN=" + isbn;  // Construct URL for deleting the book
        try {
            restTemplate.delete(deleteUrl);
            Notification.show("Book deleted successfully!");
            loadBooks();
        } catch (Exception e) {
            Notification.show("Error deleting book: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

}
