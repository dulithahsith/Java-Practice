package Samples;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServer {
    public void start(final int portNumber) {
        try (var serverSocketChannel = ServerSocketChannel.open();
             var selector = Selector.open()) {
            serverSocketChannel.bind(new InetSocketAddress(portNumber));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            var buffer = ByteBuffer.allocate(1024);
            while (true) {
                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }
                for (var key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        if (key.channel() instanceof ServerSocketChannel channel) {
                            var client = channel.accept();
                            var socket = client.socket();
                            var clientInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                            System.out.println("CONNECTED: " + clientInfo);
                            client.configureBlocking(false);
                            client.register(selector,SelectionKey.OP_READ);

                        }
                    }
                    else if(key.isReadable()){
                        if(key.channel() instanceof SocketChannel client){
                            var bytesRead = client.read(buffer);
                            if(bytesRead == -1){
                                var socket = client.socket();
                                var clientInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                                System.out.println("DISCONNECTED: " + clientInfo);
                                client.close();
                            }
                            buffer.flip();
                            var data = new String(buffer.array(),buffer.position(),bytesRead);
                            System.out.println(data);
                            while(buffer.hasRemaining()){
                                client.write(buffer);
                            }
                            buffer.clear();
                        }

                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
