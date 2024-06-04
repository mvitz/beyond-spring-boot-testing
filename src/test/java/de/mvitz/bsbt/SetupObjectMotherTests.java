package de.mvitz.bsbt;

import org.junit.jupiter.api.Test;

class SetupObjectMotherTests {

    @Test
    void total_shouldIncludeDiscount_whenPersonIsAdult() {
        // given
        var adult = Persons.MICHAEL; // or Persons.michael();

        // when
        // ...

        // then
        // ...
    }

    static class Persons {

        static final Person MICHAEL = new Person("Michael", 37);
    }
}
