public class Message {
    private final long sourceUserId;
    private final long destinationUserId;
    private final long timestamp;
    private final String message;

    public Message(long sourceUserId, long destinationUserId, long timestamp, String message) {
        this.sourceUserId = sourceUserId;
        this.destinationUserId = destinationUserId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public long getSourceUserId() {
        return sourceUserId;
    }

    public long getDestinationUserId() {
        return destinationUserId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (sourceUserId != message1.sourceUserId) return false;
        if (destinationUserId != message1.destinationUserId) return false;
        if (timestamp != message1.timestamp) return false;
        return message.equals(message1.message);
    }

    @Override
    public int hashCode() {
        int result = (int) (sourceUserId ^ (sourceUserId >>> 32));
        result = 31 * result + (int) (destinationUserId ^ (destinationUserId >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sourceUserId=" + sourceUserId +
                ", destinationUserId=" + destinationUserId +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}
