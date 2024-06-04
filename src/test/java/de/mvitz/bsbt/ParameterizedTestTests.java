package de.mvitz.bsbt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ParameterizedTestTests {

    @ParameterizedTest
    @ValueSource(ints = { 18, 42, 77, 99, 122 })
    void isAdult_shouldReturnTrue_whenPersonIsOverEighteen(int age) {
        // given
        var person = new Person();
        person.setAge(age);

        // when
        var isAdult = person.isAdult();

        // then
        assertTrue(isAdult);
    }

    @ParameterizedTest
    @MethodSource("isAdultWithCountryExamples")
    void isAdult_shouldWork_whenPersonIsAdultInGivenCountry(String country, int age, boolean shouldBeAdult) {
        // given
        var person = new Person();
        person.setCountry(country);
        person.setAge(age);

        // when
        var isAdult = person.isAdult();

        // then
        assertEquals(shouldBeAdult, isAdult);
    }

    static Stream<Arguments> isAdultWithCountryExamples() {
        return Stream.of(
            arguments("US", 18, false),
            arguments("US", 21, true),
            arguments("DE", 18, true));
    }
}
