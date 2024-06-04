package de.mvitz.bsbt;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Random;

class SetupDataFakerTests {

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

        static Faker FAKER = new Faker(Locale.of("es"), new Random(42));

        static Builder aPerson() {
            return new Builder();
        }

        static class Builder {

            Person person = new Person(
                FAKER.name().fullName(),
                FAKER.number().numberBetween(0, 120));

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
