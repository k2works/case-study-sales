package com.example.sms.presentation;

import java.util.function.Function;

/**
 * Helper class for DTO mappers in the presentation layer.
 * Contains common utility methods used by various ResourceDTOMapper classes.
 */
public class ResourceDTOMapperHelper {

    /**
     * Maps a string value to a code using the provided mapper function.
     * Returns null if the input value is null.
     *
     * @param value  the string value to map
     * @param mapper the function to apply to the value
     * @param <T>    the type of the result
     * @return the result of applying the mapper to the value, or null if value is null
     */
    public static <T> T mapStringToCode(String value, Function<String, T> mapper) {
        return value != null ? mapper.apply(value) : null;
    }
}