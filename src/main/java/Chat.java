import java.util.Set;

public class Chat {
    public Chat(long id, Set<Long> participants) {
        this.id = id;
        this.participants = participants;
    }

    private final long id;

    private final Set<Long> participants;

    public long getId() {
        return id;
    }

    public Set<Long> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (id != chat.id) return false;
        return participants.equals(chat.participants);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + participants.hashCode();
        return result;
    }
}
