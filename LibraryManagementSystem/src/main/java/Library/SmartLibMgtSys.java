package Library;

import exceptions.InvalidIsbnException;
import exceptions.InvalidYearException;
import exceptions.InvalidEmailException;
import models.Book;
import models.LibraryCatalog;
import models.MemberHandling;
import models.LibraryMember;
import networking.BlockingLibraryServer;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SmartLibMgtSys {
    private static LibraryCatalog libcat = new LibraryCatalog();
    private static MemberHandling memhandle = new MemberHandling();
    public SmartLibMgtSys(){
        System.out.println();

        System.out.println("--------------------------------------------------------------");
        System.out.println("/.-_..  WELCOME TO THE SMART LIBRARY MANAGEMENT SYSTEM  .._-.\\");
        System.out.println("--------------------------------------------------------------");
        System.out.println("           Empowering Readers, One Book at a Time!");
        System.out.println("--------------------------------------------------------------");

        System.out.println("\n**Type the related number next to the action you want to perform.**");
    }
    public static LibraryCatalog getLibcat() {
        return libcat;
    }
    public static MemberHandling getMemhandle() {
        return memhandle;
    }
    public void test(){
        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter MemberID: ");
//        String id = sc.nextLine();
//        //        Labelled loops so the flow can be directed efficiently
        outerLoop:
        while (true){
            System.out.println("Register Book (1): ");
            System.out.println("Print all books(2): ");
            System.out.println("Register new Member (3): ");
            System.out.println("Show all members (4): ");
            System.out.println("For searching and recommendations");
            System.out.println("           By Title (5):");
            System.out.println("           By Author (6):");
            System.out.println("           By Genre (7):");
            System.out.println("           By Genre and Time (8):");
            System.out.println("Borrow book (9): ");
            System.out.println("Return book(10): ");

            System.out.println("Exit(11): ");

            ConcurrentHashMap<String, LibraryMember> members;

            int command = sc.nextInt();
            sc.nextLine();
            switch (command){
                case(1):{
                    System.out.println("type exit to go back");
                    Book book = new Book();
                    System.out.println();
                    while(true){
                        System.out.println("Enter the ISBN: ");
                        String ISBN = sc.nextLine();
                        if (ISBN.equalsIgnoreCase("exit")){
                            continue outerLoop;
                        }
                        try {
                            book.setISBN(ISBN);
                            break;
                        } catch (InvalidIsbnException e) {
                            System.out.println(e.getMessage());
                        }

                    }

                    System.out.println("Enter the title:");
                    String title = sc.nextLine();
                    book.setTitle(title);

                    System.out.println("Enter the author:");
                    String author = sc.nextLine();
                    book.setAuthor(author);

                    while(true){
                        System.out.println("Choose the genre (LITERARY, MYSTERY, THRILLER, HORROR, HISTORICAL): ");
                        try {
                            Book.Genre genre = Book.Genre.valueOf(sc.nextLine().toUpperCase());
                            book.setGenre(genre);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid Genre, give again: "+ e.getMessage());
                        }
                    }

                    while(true){
                        System.out.println("Enter the publication year:");
                        int pubYear = sc.nextInt();
                        try {
                            book.setPubYear(pubYear);
                            break;
                        } catch (InvalidYearException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    book.setAvailable(true);
                    libcat.addBook(book);
                    break;

                }
                case 2: {
                    String s =libcat.printAll();
                    System.out.println(s);
                    break;
                }
                case 3:{
                    System.out.println("type exit to go back");
                    LibraryMember mem = new LibraryMember();
                    System.out.println();

                    System.out.println("Enter the name:");
                    String name = sc.nextLine();
                    mem.setName(name);

                    System.out.println("Enter the address:");
                    String address = sc.nextLine();
                    mem.setAddress(address);

                    while(true){
                        System.out.println("Enter the email address:");
                        String email = sc.nextLine();
                        try {
                            mem.setEmailAddress(email);
                            break;
                        } catch (InvalidEmailException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    mem.setBorrowed(new ArrayList<String>());
                    mem.setLastRenewDate(LocalDate.now());

                    memhandle.addMember(mem);
                    break;

                }
                case 4:{
                    memhandle.printAll();
                    break;
                }
                case 5:{
                    System.out.println("Type title:");
                    libcat.searchByTitle(sc.nextLine());
                    break;
                }
                case 6:{
                    System.out.println("Type author:");
                    libcat.searchByAuthor(sc.nextLine());
                    break;
                }
                case 7:{
                    System.out.println("Type preferred genre:");
                    libcat.groupByGenre(Book.Genre.valueOf(sc.nextLine().toUpperCase()));
                    break;
                }
                case 8:{
                    System.out.println("Type genre and preferred time:");
                    Book.Genre genre = Book.Genre.valueOf(sc.nextLine().toUpperCase());
                    int year = sc.nextInt();
                    libcat.recommendBooks(genre,year);
                    break;
                }
                case 9:{
                    System.out.println("Type Member ID");
                    String memID = sc.nextLine();
                    System.out.println("Type ISBN");
                    String ISBN = sc.nextLine();
                    libcat.borrowBook(memhandle, memID, ISBN);
                    break ;
                }
                case 10:{
                    System.out.println("Type Member ID");
                    String memID = sc.nextLine();
                    System.out.println("Type ISBN");
                    String ISBN = sc.nextLine();
                    libcat.returnBook(memhandle, memID, ISBN);
                    break ;
                }
                case 11:{
                    break outerLoop;
                }

            }

        }
    }

    public void testUserActions(String ISBN,String memID) {
        try {
//            // Register a new member (we assume this part is already done)
//            LibraryMember mem = new LibraryMember();
//            mem.setName("User " + memID);
//            mem.setAddress("A of " + memID);
//            mem.setEmailAddress(memID + "@example.com");
//            mem.setBorrowed(new ArrayList<String>());
//            mem.setLastRenewDate(LocalDate.now());
//            memhandle.addMember(mem);
//
//            // Register a new book (for testing purposes)
//            Book book = new Book();
//            book.setISBN(ISBN);
//            book.setTitle("Test Book " + ISBN);
//            book.setAuthor("Author " + ISBN);
//            book.setGenre(Book.Genre.MYSTERY);
//            book.setPubYear(2024);
//            book.setAvailable(true);
//            libcat.addBook(book);

            // Borrow the book
            System.out.println("User " + memID + " borrowing book with ISBN: " + ISBN);
            libcat.borrowBook(memhandle, memID, ISBN);

            // Sleep for a while to simulate delay
            Thread.sleep(1000);

//            Return the book
//            System.out.println("User " + memID + " returning book with ISBN: " + ISBN);
//            libcat.returnBook(memhandle, memID, ISBN);

//        } catch (InvalidIsbnException e) {
//            e.printStackTrace();
//        } catch (InvalidEmailException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidYearException e) {
//            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        SmartLibMgtSys lib = new SmartLibMgtSys();
//        lib.test();


//        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
//            String[] memIDs = {"M000000", "M000001", "M000002"};
//            String[] ISBNs = {"978-3-16-148410-1", "978-3-16-148410-2", "978-3-16-148410-3"};
//
//            for (int i = 0; i < 3; i++) {
//                final String memID = memIDs[i];
//                final String ISBN = ISBNs[i];
//                executor.submit(() -> {
//                    lib.testUserActions(ISBN,memID);  // Each user performs actions concurrently
//                });
//            }
//
//            // Shutdown the executor after all tasks are complete
//            executor.shutdown();
//        }

        BlockingLibraryServer bls = new BlockingLibraryServer(lib);
        bls.start();
    }
}