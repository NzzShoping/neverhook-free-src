package ru.neverhook.event.types;

public class Priority {

    public static final byte
            HIGHEST = 0,
            HIGH = 1,
            MEDIUM = 2,
            LOW = 3,
            LOWEST = 4;

    public static byte[] VALUE_ARRAY;

    static {
        VALUE_ARRAY = new byte[]{
                HIGHEST,
                HIGH,
                MEDIUM,
                LOW,
                LOWEST
        };
    }

}
