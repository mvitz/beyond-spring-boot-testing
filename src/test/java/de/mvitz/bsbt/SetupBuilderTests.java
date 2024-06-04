package de.mvitz.bsbt;

import org.junit.jupiter.api.Test;

class SetupBuilderTests {

    @Test
    void total_shouldIncludeDiscount_whenPersonIsAdult() {
        // given
        var adult = Persons.aPerson()
            .withAge(18)
            .build();

        // when
        // ...

        // then
        // ...
    }

    static class Persons {

        static Builder aPerson() {
            return new Builder();
        }

        static class Builder {

            Person person = new Person();

            Builder withAge(int age) {
                person.setAge(age);
                return this;
            }

            Person build() {
                return person;
            }
        }
    }
}
