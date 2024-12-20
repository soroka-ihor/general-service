package es.localchat.utils;

public class MessageCounter {
    public static int calculatePagesCount(int pageSize, int totalMessagesCount) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0.");
        }
        return (totalMessagesCount + pageSize - 1) / pageSize;
    }
}
