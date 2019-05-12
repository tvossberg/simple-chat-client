import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    //1345-15:21
    //16:29
    //2-2.5 hours 10:30-115
    //+15 minutes need empty line and Connection:cloaed. Curl still gets stuck
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();

        Map<String, ArrayList<Double>> map = gson.fromJson(new FileReader(args[0]), Map.class);
        System.out.println(map);

        Map<Long, Set<Long>> realMap = new HashMap<>();
        for(Map.Entry<String,ArrayList<Double>> entry: map.entrySet()){
            Set<Long> valueSet= entry.getValue().stream().map(Double::longValue).collect(Collectors.toSet());
            realMap.put(Long.parseLong(entry.getKey()),valueSet);
        }
        System.out.println(realMap);

        openServer();
    }

    private static void openServer() throws IOException {
        SocketChannel server = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 9000);
        server.bind(socketAddr);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        server.read(buffer);
//        SocketChannel socketChannel = SocketChannel.open();
//
//        socketChannel.bind(new InetSocketAddress(20000));
//        socketChannel.connect(new InetSocketAddress(host, 80));
//
//        socketChannel.bind(new InetSocketAddress("http://localhost", 8008));
//        ByteBuffer b = ByteBuffer.allocate(100);
//        socketChannel.read(b);
//        b.getChar();

    }
}
