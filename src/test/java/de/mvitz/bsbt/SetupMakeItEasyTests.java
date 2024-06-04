package de.mvitz.bsbt;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.junit.jupiter.api.Test;

import static com.natpryce.makeiteasy.MakeItEasy.an;
import static com.natpryce.makeiteasy.MakeItEasy.with;

class SetupMakeItEasyTests {

    @Test
    void total_shouldIncludeDiscount_whenPersonIsAdult() {
        // given
        var adult = an(Persons.Person,
            with(42, Persons.age))
            .make();

        // when
        // ...

        // then
        // ...
    }

    static class Persons {

        static final Property<Person, String> name = Property.newProperty();
        static final Property<Person, Integer> age = Property.newProperty();

        static final Instantiator<Person> Person = lookup -> new Person(
            lookup.valueOf(name, "Michael"),
            lookup.valueOf(age, 42));
    }
}
