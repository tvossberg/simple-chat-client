import com.google.gson.Gson;

import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ChatService {

    private final ConcurrentHashMap<Long,Chat> chatIdToChat = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long,Chat> userIdToChat = new ConcurrentHashMap<>();
    private AtomicLong chatId = new AtomicLong(0);

    public ChatService(){
//        Gson gson = new Gson();
//
//        Map<String, ArrayList<Double>> map = gson.fromJson(reader, Map.class);
//        System.out.println(map);
//
//        this.userIdToContacts = new HashMap<>();
//        for(Map.Entry<String,ArrayList<Double>> entry: map.entrySet()){
//            Set<Long> valueSet= entry.getValue().stream().map(Double::longValue).collect(Collectors.toSet());
//            userIdToContacts.put(Long.parseLong(entry.getKey()),valueSet);
//        }
//        System.out.println(userIdToContacts);
    }

    //Create a chat
    public Chat create(Chat chat){
        Chat updateChat = new Chat(chatId.incrementAndGet(), chat.getParticipantIds());
        chatIdToChat.put(updateChat.getId(),updateChat);
        for(long participant :chat.getParticipantIds()){
            userIdToChat.put(participant,chat);
        }

        return chat;
    }

    public Message create(Message message) {
        return message;
    }

    public List<Message> getMessages(long chatId) {
        return Arrays.asList(defaultMessage());
    }

    public Chat getChat(long userId) {
        return new Chat(1L, new HashSet<>());
    }



    private static Message defaultMessage() {
        return new Message(1L,1L,1L,"Hello!");
    }
}
