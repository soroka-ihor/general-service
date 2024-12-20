package es.localchat.utils;

import java.util.Optional;

public class OptionalWrapper {
    public static boolean isNullOrEmpty(Object obj) {
        return Optional.ofNullable(obj).isEmpty();
    }
}
