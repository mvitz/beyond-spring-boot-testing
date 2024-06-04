package de.mvitz.bsbt;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class SetupInstancioTests {

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

            Person person = Instancio.of(Person.class)
                    .generate(Select.field(Person::getAge), gen -> gen.ints().min(0).max(120))
                    .generate(Select.field(Person::getName), gen -> gen.string().alphaNumeric())
                        .create();

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
