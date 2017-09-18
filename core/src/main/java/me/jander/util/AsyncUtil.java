package me.jander.util;

import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class AsyncUtil {

    public static <T, R> Function<T, R> withCompletionException(ThrowingFunction<T, R, Exception> throwingFunc) {

        return i -> {
            try {
                return throwingFunc.apply(i);
            } catch (Exception ex) {
                throw new CompletionException(ex);
            }
        };
    }
}


