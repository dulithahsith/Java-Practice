package networking;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class NIO_Ex {
    public static void main(String[] args) {
        try {
            // Create a non-blocking server socket channel
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);  // Set to non-blocking mode
            serverChannel.bind(new InetSocketAddress(8000));  // Bind to port 8080

            // Open a selector to handle multiple channels
            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Non-blocking server started on port 8080");

            while (true) {
                // Wait for events on registered channels
                selector.select();  // Blocks until at least one channel is ready

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        // Handle incoming client connections
                        ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverSocket.accept();
                        clientChannel.configureBlocking(false);  // Set the client to non-blocking
                        clientChannel.register(selector, SelectionKey.OP_READ);  // Monitor for readable data
                        System.out.println("New client connected: " + clientChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        // Handle data from a client
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        int bytesRead = clientChannel.read(buffer);

                        if (bytesRead == -1) {
                            // Client closed the connection
                            clientChannel.close();
                            System.out.println("Client disconnected");
                        } else {
                            buffer.flip();
                            String message = new String(buffer.array(), 0, buffer.limit());
                            System.out.println("Received: " + message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
