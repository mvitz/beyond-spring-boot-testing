package de.mvitz.bsbt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InstancePerTestTests {

    Person adultPerson = new Person(42);

    /* Not required because by default JUnit creates a new instance for every test
    @BeforeEach
    void setUp() {
        adultPerson = new Person(42);
    }
     */

    @Test
    void test1() {
        assertThat(adultPerson.getAge()).isEqualTo(42);

        adultPerson.setAge(21);
        assertThat(adultPerson.getAge()).isEqualTo(21);
    }

    @Test
    void test2() {
        assertThat(adultPerson.getAge()).isEqualTo(42);

        adultPerson.setAge(37);
        assertThat(adultPerson.getAge()).isEqualTo(37);
    }
}
