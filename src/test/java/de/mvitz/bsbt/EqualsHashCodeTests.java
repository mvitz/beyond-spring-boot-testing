package de.mvitz.bsbt;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class EqualsHashCodeTests {

    @Test
    void equalsAndHashCode_shouldFulfillContract() {
        EqualsVerifier.forClass(Person.class).verify();
    }
}
