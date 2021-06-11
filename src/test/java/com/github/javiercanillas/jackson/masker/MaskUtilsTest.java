package com.github.javiercanillas.jackson.masker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MaskUtilsTest {

    private static Stream<Arguments> stringArguments() {
        return Stream.of(
                Arguments.of(null, 0, '*', null),
                Arguments.of("a", 0, '*', "*"),
                Arguments.of("a", 1, '*', "a"),
                Arguments.of("abc", 1, '*', "**c")
        );
    }

    @ParameterizedTest
    @MethodSource("stringArguments")
    void mask(String value, int keepLast, char maskChar, String result) {
        assertEquals(result, MaskUtils.mask(value, keepLast, maskChar));
    }

    private static Stream<Arguments> stringArrayArguments() {
        return Stream.of(
                Arguments.of(null, 0, '*', null),
                Arguments.of(new String[0], 0, '*', new String[0]),
                Arguments.of(new String[1], 0, '*', new String[1]),
                Arguments.of(new String[]{"a"}, 1, '*', new String[]{"a"}),
                Arguments.of(new String[]{"a", null}, 1, '*', new String[]{"a", null}),
                Arguments.of(new String[]{"abc", "a"}, 1, '*', new String[]{"**c", "a"})
        );
    }

    @ParameterizedTest
    @MethodSource("stringArrayArguments")
    void mask(String[] value, int keepLast, char maskChar, String[] result) {
        final String[] masked = MaskUtils.mask(value, keepLast, maskChar);
        if (value == null) {
            assertNull(masked);
        } else {
            assertEquals(result.length, masked.length);
            IntStream.range(0, result.length).forEach(i -> assertEquals(result[i], masked[i]));
        }

    }

    private static Stream<Arguments> stringListArguments() {
        return Stream.of(
                Arguments.of(null, 0, '*', null),
                Arguments.of(List.of(), 0, '*', List.of()),
                Arguments.of(List.of("a"), 1, '*', List.of("a")),
//                Arguments.of(List.of("a", null), 1, '*', List.of("a", null)),
                Arguments.of(List.of("abc", "a"), 1, '*', List.of("**c", "a"))
        );
    }

    @ParameterizedTest
    @MethodSource("stringListArguments")
    void mask(List<String> value, int keepLast, char maskChar, List<String> result) {
        final List<String> masked = MaskUtils.mask(value, keepLast, maskChar);
        if (value == null) {
            assertNull(masked);
        } else {
            assertEquals(result.size(), masked.size());
            IntStream.range(0, result.size()).forEach(i -> assertEquals(result.get(i), masked.get(i)));
        }

    }

    private static Stream<Arguments> stringSetArguments() {
        return Stream.of(
                Arguments.of(null, 0, '*', null),
                Arguments.of(Set.of(), 0, '*', Set.of()),
                Arguments.of(Set.of("a"), 1, '*', Set.of("a")),
//                Arguments.of(Set.of("a", null), 1, '*', Set.of("a", null)),
                Arguments.of(Set.of("abc", "a"), 1, '*', Set.of("**c", "a"))
        );
    }

    @ParameterizedTest
    @MethodSource("stringSetArguments")
    void mask(Set<String> value, int keepLast, char maskChar, Set<String> result) {
        final Set<String> masked = MaskUtils.mask(value, keepLast, maskChar);
        if (value == null) {
            assertNull(masked);
        } else {
            assertEquals(result.size(), masked.size());
            result.forEach(s -> assertTrue(masked.contains(s), String.format("Didn't find %s on masked result", s)));
        }

    }

    @Test
    void maskIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> MaskUtils.mask("abc", -1, '*'));
    }
}