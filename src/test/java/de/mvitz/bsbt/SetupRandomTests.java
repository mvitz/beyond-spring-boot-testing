package de.mvitz.bsbt;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

class SetupRandomTests {

    @Test
    void total_shouldIncludeDiscount_whenPersonIsAdult() {
        // given
        var adult = Persons.aPerson()
            .olderThan(18)
            // or .thatsAnAdult()
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

            Person person = new Person(
                RandomStringUtils.randomAlphabetic(2, 20),
                RandomUtils.nextInt(0, 120));

            Builder withAge(int age) {
                person.setAge(age);
                return this;
            }

            Builder olderThan(int age) {
                return withAge(RandomUtils.nextInt(age, 120));
            }

            Builder thatsAnAdult() {
                return this.olderThan(21);
            }

            Person build() {
                return person;
            }
        }
    }
}
