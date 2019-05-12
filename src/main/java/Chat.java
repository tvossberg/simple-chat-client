import java.util.Set;

public class Chat {
    public Chat(Long id, Set<Long> participantIds) {
        this.id = id;
        this.participantIds = participantIds;
    }

    private final Long id;

    private final Set<Long> participantIds;

    public Long getId() {
        return id;
    }

    public Set<Long> getParticipantIds() {
        return participantIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (id != chat.id) return false;
        return participantIds.equals(chat.participantIds);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (participantIds != null ? participantIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", participantIds=" + participantIds +
                '}';
    }
}
