
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {

    static Gson gson = new Gson();

    private static Map<SocketChannel,byte[]> dataTracking = new HashMap<>();

    private static final String INITIAL_HEADERS = "HTTP/1.1 200 OK\r\n" +
            "Content-Length: 0\r\n"+
            "Connection: keep-alive\r\n";

    private static final String OK_RESPONSE = INITIAL_HEADERS +
            "\r\n";

    private final ChatService chatService;

    private Server(ChatService chatService){
        this.chatService = chatService;
    }

    private static String generateRequest(String body){
        String output = "HTTP/1.1 200 OK\r\n";
        output += "Content-Length: "+( body.getBytes().length )+"\r\n";
        output += "Connection: keep-alive\r\n";
        output += "\r\n";
        output += body;
        output += "\r\n";
        return output;
    }


    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {
        Server server = new Server(new ChatService());
        server.run();
    }

    private void run() throws IOException {
        Selector selector = Selector.open(); // selector is open here

        ServerSocketChannel socket = ServerSocketChannel.open();
        InetSocketAddress addr = new InetSocketAddress("localhost", 1111);

        socket.bind(addr);

        socket.configureBlocking(false);

        int ops = socket.validOps();
        socket.register(selector, ops, null);

        while (true) {

            log("i'm a server and i'm waiting for new connection and buffer select...");
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey myKey = iter.next();

                iter.remove();

                if (!myKey.isValid()){
                    continue;
                }

                // Tests whether this key's channel is ready to accept a new socket connection
                if (myKey.isAcceptable()) {

                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) myKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ);

                    log("Connection Accepted: \n");
                    // Tests whether this key's channel is ready for reading
                }

                if(myKey.isReadable()){
                    log("Reading: \n");
                    SocketChannel channel = (SocketChannel) myKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    readBuffer.clear();
                    int read;
                    try {
                        read = channel.read(readBuffer);
                    } catch (IOException e) {
                        System.out.println("Reading problem, closing connection");
                        myKey.cancel();
                        channel.close();
                        continue;
                    }
                    if (read == -1){
                        System.out.println("Nothing was there to be read, closing connection");
                        channel.close();
                        myKey.cancel();
                        continue;
                    }
                    readBuffer.flip();
                    byte[] data = new byte[1000];
                    readBuffer.get(data, 0, read);
                    //Filter null bytes
                    int i=0;
                    for(; i<1000;i++){
                        if (data[i]==(byte)0){
                            break;
                        }
                    }
                    data = Arrays.copyOfRange(data, 0, i);

                    String request = new String(data);
                    System.out.println("Received: "+request);
                    String response = handle(request);

                    System.out.println("Responding with:\n"+response);

                    SocketChannel socketChannel = (SocketChannel) myKey.channel();
                    dataTracking.put(socketChannel, response.getBytes());
                    //dataTracking.put(socketChannel, data);
                    myKey.interestOps(SelectionKey.OP_WRITE);
                }

                if (myKey.isWritable()) {
                    SocketChannel channel = (SocketChannel) myKey.channel();

                    byte[] data = dataTracking.get(channel);
                    dataTracking.remove(channel);
                    channel.write(ByteBuffer.wrap(data));
                    myKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }

    private String handle(String request) {
        if(request.contains("POST")){
            if(request.contains("messages")){
                long chatId = Long.parseLong(request.split("/")[2]);
                log("recieved message " +chatId);
                String foo = request.substring(request.indexOf("{"));
                log(foo);
                Message message = gson.fromJson(foo, Message.class);
                log(message.toString());
                log("Creating message");
                return  generateRequest(gson.toJson(chatService.create(message)));
            }else{
                String foo = request.substring(request.indexOf("{"));
                log(foo);
                Chat chat = gson.fromJson(foo, Chat.class);
                //Create a chat between two users
                log("Creating chat between two users");
                return  generateRequest(gson.toJson(chatService.create(chat)));
            }
        }else if (request.contains("GET")){
            if(request.contains("messages")){
                //list messages
                long chatId = Long.parseLong(request.split("/")[2]);
                log("retrieving messages for chat " +chatId);
                List<Message> messages = chatService.getMessages(chatId);
                return  generateRequest(gson.toJson(messages));
            }else{
                long userId = Long.parseLong(request.split("=")[1].split(" ")[0]);
                log("list user's current chats for user "+ userId);
                List<Chat> chats = Arrays.asList(chatService.getChat(userId));
                return generateRequest(gson.toJson(chats));
            }
        }
        //return OK_RESPONSE;
        return generateRequest("");
    }



    private static void log(String str) {
        System.out.println(str);
    }
}