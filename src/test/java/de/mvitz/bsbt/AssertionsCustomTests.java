package de.mvitz.bsbt;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static de.mvitz.bsbt.AssertionsCustomTests.PersonAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

class AssertionsCustomTests {

    @Test
    void constructor_shouldSetNameAndAge() {
        // when
        var michael = new Person("Michael", 38);

        // then
        assertThat(michael.getName()).isEqualTo("Michael");
        assertThat(michael.getAge()).isEqualTo(38);
        assertThat(michael.isAdult()).isTrue();
    }

    @Test
    void constructor_shouldSetNameAndAge2() {
        // when
        var michael = new Person("Michael", 38);

        // then
        Assertions.assertThat(michael)
            .extracting(Person::getName, Person::getAge, Person::isAdult)
            .containsExactly("Michael", 38, true);
    }

    @Test
    void constructor_shouldSetNameAndAge3() {
        // when
        var michael = new Person("Michael", 38);

        // then
        assertThat(michael)
            .hasName("Michael")
            .hasAge(38)
            .isAdult();
    }

    static class PersonAssert extends AbstractAssert<PersonAssert, Person> {

        public PersonAssert(Person actual) {
            super(actual, PersonAssert.class);
        }

        public PersonAssert hasName(String name) {
            return this;
        }

        public PersonAssert hasAge(int age) {
            return this;
        }

        public PersonAssert isAdult() {
            isNotNull();
            if (!actual.isAdult()) {
                failWithMessage("Expected person to be adult");
            }
            return this;
        }

        public static PersonAssert assertThat(Person actual) {
            return new PersonAssert(actual);
        }
    }
}
