package networking;

import Library.SmartLibMgtSys;
import exceptions.InvalidEmailException;
import exceptions.InvalidIsbnException;
import exceptions.InvalidYearException;
import models.LibraryCatalog;
import models.LibraryMember;
import models.Book;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BlockingLibraryServer {
    static SmartLibMgtSys lib;

    public BlockingLibraryServer(SmartLibMgtSys lib) {
        this.lib = lib;
    }

    // Starts the server and listens for incoming client connections
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Library Server is running on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected, creating a new thread to handle the client...");
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handles communication with a single client
    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Welcome to the Smart Library Management System!");
            boolean running = true;
            Scanner sc = new Scanner(System.in);

            while (running) {
                // Display menu to client
                out.println("Please choose an option:");
                out.println("Register Book(1)");
                out.println("Print all books(2)");
                out.println("Register new Member(3)");
                out.println("Show all members(4)");
                out.println("For Searching and recommendations");
                out.println("     Search by Title(5)");
                out.println("     Search by Author(6)");
                out.println("     Search by Genre(7)");
                out.println("     Search by Genre and Time(8)");
                out.println("Borrow Book(9)");
                out.println("Return Book(10)");
                out.println("Exit(11)");

                String choice = in.readLine();  // Read the client's choice

                switch (choice) {
                    case "1":
                        out.println("Register a new Book...");
                        registerBook(in, out);
                        break;
                    case "2":
                        out.println("Printing all books...");
                        String s = lib.getLibcat().printAll();
                        out.println(s);
                        break;
                    case "3":
                        out.println("Register a new Member...");
                        registerMember(in, out);
                        break;
                    case "4":
                        out.println("Showing all members...");
                        String s1 = lib.getMemhandle().printAll();
                        out.println(s1);
                        break;
                    case "5":
                        out.println("Searching by Title...");
                        searchByTitle(in, out);
                        break;
                    case "6":
                        out.println("Searching by Author...");
                        searchByAuthor(in, out);
                        break;
                    case "7":
                        out.println("Searching by Genre...");
                        searchByGenre(in, out);
                        break;
                    case "8":
                        out.println("Searching by Genre and Time...");
                        searchByGenreAndTime(in, out);
                        break;
                    case "9":
                        out.println("Borrowing a Book...");
                        borrowBook(in, out);
                        break;
                    case "10":
                        out.println("Returning a Book...");
                        returnBook(in, out);
                        break;
                    case "11":
                        out.println("Exiting...");
                        running = false; // End the loop and disconnect
                        break;
                    default:
                        out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerBook(BufferedReader in, PrintWriter out) {
        out.println("type exit to go back");
        Book book = new Book();
        out.println();
        try {
            while (true) {
                out.println("Enter the ISBN: ");
                String ISBN = in.readLine();
                if (ISBN.equalsIgnoreCase("exit")) {
                    continue;
                }
                try {
                    book.setISBN(ISBN);
                    break;
                } catch (InvalidIsbnException e) {
                    out.println(e.getMessage());
                }

            }

            out.println("Enter the title:");
            String title = in.readLine();
            book.setTitle(title);

            out.println("Enter the author:");
            String author = in.readLine();
            book.setAuthor(author);

            while (true) {
                out.println("Choose the genre (LITERARY, MYSTERY, THRILLER, HORROR, HISTORICAL): ");
                try {
                    Book.Genre genre = Book.Genre.valueOf(in.readLine().toUpperCase());
                    book.setGenre(genre);
                    break;
                } catch (IllegalArgumentException e) {
                    out.println("Invalid Genre, give again: " + e.getMessage());
                }
            }

            while (true) {
                out.println("Enter the publication year:");
                int pubYear = Integer.parseInt(in.readLine());
                try {
                    book.setPubYear(pubYear);
                    break;
                } catch (InvalidYearException e) {
                    out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            out.println(e.getMessage());;
        }
        book.setAvailable(true);
        lib.getLibcat().addBook(book);
        out.println("Book added successfully");
    }

    private void registerMember(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Name:");
            String name = in.readLine();
            out.println("Enter Address:");
            String address = in.readLine();
            LibraryMember member = new LibraryMember(name, address);
            while(true){
                out.println("Enter the email address:");
                String email = in.readLine();
                try {
                    member.setEmailAddress(email);
                    break;
                } catch (InvalidEmailException e) {
                    System.out.println(e.getMessage());
                }
            }
            lib.getMemhandle().addMember(member);
            out.println("Member successfully registered!");


        } catch (Exception e) {
            out.println("Error registering member: " + e.getMessage());
        }
    }

    private void searchByTitle(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Title to search:");
            String title = in.readLine();
            String s =lib.getLibcat().searchByTitle(title);
            out.println(s);
        } catch (Exception e) {
            out.println("Error searching by title: " + e.getMessage());
        }
    }

    private void searchByAuthor(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Author to search:");
            String author = in.readLine();
            String s =lib.getLibcat().searchByAuthor(author);
            out.println(s);
        } catch (Exception e) {
            out.println("Error searching by author: " + e.getMessage());
        }
    }

    private void searchByGenre(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Genre to search (LITERARY, MYSTERY, etc.):");
            String genre = in.readLine();
            String s =lib.getLibcat().groupByGenre(Book.Genre.valueOf(genre.toUpperCase()));
            out.println(s);
        } catch (Exception e) {
            out.println("Error searching by genre: " + e.getMessage());
        }
    }

    private void searchByGenreAndTime(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Genre and Year:");
            String genre = in.readLine();
            int year = Integer.parseInt(in.readLine());
            String s = lib.getLibcat().recommendBooks(Book.Genre.valueOf(genre.toUpperCase()), year);
            out.println(s);
        } catch (Exception e) {
            out.println("Error searching by genre and time: " + e.getMessage());
        }
    }

    private void borrowBook(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Member ID:");
            String memID = in.readLine();
            out.println("Enter ISBN of the book to borrow:");
            String ISBN = in.readLine();
            lib.getLibcat().borrowBook(lib.getMemhandle(), memID, ISBN);
            out.println("Book borrowed successfully!");
        } catch (Exception e) {
            out.println("Error borrowing book: " + e.getMessage());
        }
    }

    private void returnBook(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter Member ID:");
            String memID = in.readLine();
            out.println("Enter ISBN of the book to return:");
            String ISBN = in.readLine();
            lib.getLibcat().returnBook(lib.getMemhandle(), memID, ISBN);
            out.println("Book returned successfully!");
        } catch (Exception e) {
            out.println("Error returning book: " + e.getMessage());
        }
    }
}