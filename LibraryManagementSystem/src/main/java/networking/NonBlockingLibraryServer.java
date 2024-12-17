package networking;

import Library.SmartLibMgtSys;
import exceptions.InvalidIsbnException;
import exceptions.InvalidYearException;
import models.Book;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NonBlockingLibraryServer {
    private static SmartLibMgtSys lib;

    public NonBlockingLibraryServer(SmartLibMgtSys lib) {
        this.lib = lib;
    }

    public void start() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.bind(new java.net.InetSocketAddress(8080));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Non-Blocking Library Server is running on port 8080...");

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isAcceptable()) {
                        handleAccept(serverChannel, selector);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    } else if (key.isWritable()) {
                        handleWrite(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        System.out.println("New client connected: " + clientChannel.getRemoteAddress());
        clientChannel.write(ByteBuffer.wrap("Welcome to the Smart Library Management System!\n".getBytes()));
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        try {
            int bytesRead = clientChannel.read(buffer);  // Reads from the channel into the buffer

            if (bytesRead == -1) {
                clientChannel.close();  // Client has closed the connection
                return;
            }

            // If we read any bytes, process them
            if (bytesRead > 0) {
                buffer.flip();  // Switch to reading mode

                StringBuilder input = new StringBuilder();
                while (buffer.hasRemaining()) {
                    char c = (char) buffer.get();
                    if (c == '\n') {
                        break;  // Stop reading when newline is found
                    }
                    input.append(c);
                }

                String command = input.toString().trim();
                if (!command.isEmpty()) {
                    System.out.println("Received: " + command);
                    String response = processQuery(command);

                    // Prepare the response buffer
                    ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
                    key.interestOps(SelectionKey.OP_WRITE);  // Switch to write mode
                    key.attach(responseBuffer);  // Attach the response buffer
                }

                buffer.compact();  // Move unprocessed data to the start of the buffer
            }

        } catch (IOException e) {
            try {
                clientChannel.close();  // Ensure the channel is closed if error occurs
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }




    private void handleWrite(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        try {
            clientChannel.write(buffer);
            if (!buffer.hasRemaining()) {
                buffer.clear();
                key.interestOps(SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            try {
                clientChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String processQuery(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (command.toLowerCase()) {
            case "1":
                return "Books: \n" + lib.getLibcat().printAll() + "\n";
            case "2":
                return "Members: \n" + lib.getMemhandle().printAll() + "\n";
            case "3":
                return registerBook(data);
            default:
                return "Invalid command. Use '1', '2', or '3'.\n";
        }
    }

    private String registerBook(String data) {
        String[] bookDetails = data.split("\\s+", 4);
        if (bookDetails.length < 4) {
            return "Error: Invalid input format. Use: write_book ISBN Title Author Year\n";
        }

        String isbn = bookDetails[0];
        String title = bookDetails[1];
        String author = bookDetails[2];
        String yearString = bookDetails[3];

        try {
            int year = Integer.parseInt(yearString);
            Book book = new Book(isbn, title, author, year);
            lib.getLibcat().addBook(book);
            return "Book registered successfully: " + book.toString() + "\n";

        } catch (NumberFormatException e) {
            return "Error: Year must be a valid number.\n";
        }
    }

    public static void main(String[] args){
        SmartLibMgtSys lib = new SmartLibMgtSys();
        NonBlockingLibraryServer nbls = new NonBlockingLibraryServer(lib);
        nbls.start();
    }

}
