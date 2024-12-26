package networking;

import Library.SmartLibMgtSys;
import exceptions.InvalidIsbnException;
import exceptions.InvalidYearException;
import models.Book;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

// Wrapper class to manage both ByteBuffer and StringBuilder for a client connection
class ClientAttachment {
    ByteBuffer buffer;
    StringBuilder stringBuilder;

    public ClientAttachment(int bufferSize) {
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.stringBuilder = new StringBuilder();
    }
}

public class NonBlockingLibraryServer {
    private final SmartLibMgtSys lib;

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
                selector.select(); // Wait for events
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
            System.err.println("Server Error: " + e.getMessage());
        }
    }

    private void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        // Attach ClientAttachment to manage ByteBuffer and StringBuilder
        clientChannel.register(selector, SelectionKey.OP_READ, new ClientAttachment(1024));
        System.out.println("New client connected: " + clientChannel.getRemoteAddress());

        clientChannel.write(ByteBuffer.wrap("Welcome to the Smart Library Management System!\n".getBytes()));
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientAttachment attachment = (ClientAttachment) key.attachment();

        try {
            int bytesRead = clientChannel.read(attachment.buffer);

            if (bytesRead == -1) {
                System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
                closeChannel(clientChannel);
                return;
            }

            if (bytesRead > 0) {
                attachment.buffer.flip(); // Switch to read mode

                while (attachment.buffer.hasRemaining()) {
                    char c = (char) attachment.buffer.get();
                    attachment.stringBuilder.append(c);

                    // Check for full command (new line '\n')
                    if (c == '\n') {
                        String command = attachment.stringBuilder.toString().trim();
                        System.out.println("Received: " + command);

                        String response = processQuery(command);

                        // Prepare response buffer for writing
                        ByteBuffer responseBuffer = ByteBuffer.wrap((response + "\n").getBytes());
                        key.interestOps(SelectionKey.OP_WRITE);
                        key.attach(responseBuffer);

                        attachment.stringBuilder.setLength(0); // Clear the accumulated input
                    }
                }

                attachment.buffer.clear(); // Clear the buffer for the next read
            }
        } catch (IOException e) {
            System.err.println("Error reading from client: " + e.getMessage());
            closeChannel(clientChannel);
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
                key.attach(new ClientAttachment(1024)); // Reattach new ClientAttachment
            }
        } catch (IOException e) {
            System.err.println("Error writing to client: " + e.getMessage());
            closeChannel(clientChannel);
        }
    }

    private String processQuery(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        switch (command.toLowerCase()) {
            case "1":
                return "Books: \n" + lib.getLibcat().printAll();
            case "2":
                return "Members: \n" + lib.getMemhandle().printAll();
            case "3":
                return registerBook(data);
            default:
                return "Invalid command. Use '1' for books, '2' for members, or '3' to add a book.";
        }
    }

    private String registerBook(String data) {
        String[] bookDetails = data.split("\\s+", 4);
        if (bookDetails.length < 4) {
            return "Error: Invalid input format. Use: ISBN Title Author Year";
        }

        String isbn = bookDetails[0];
        String title = bookDetails[1];
        String author = bookDetails[2];
        String yearString = bookDetails[3];

        try {
            int year = Integer.parseInt(yearString);
            Book book = new Book(isbn, title, author, year);
            lib.getLibcat().addBook(book);
            return "Book registered successfully: " + book.toString();
        } catch (NumberFormatException e) {
            return "Error: Year must be a valid number.";
        }
    }

    private void closeChannel(SocketChannel clientChannel) {
        try {
            if (clientChannel != null && clientChannel.isOpen()) {
                System.out.println("Closing connection to client: " + clientChannel.getRemoteAddress());
                clientChannel.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SmartLibMgtSys lib = new SmartLibMgtSys();
        NonBlockingLibraryServer nbls = new NonBlockingLibraryServer(lib);
        nbls.start();
    }
}
