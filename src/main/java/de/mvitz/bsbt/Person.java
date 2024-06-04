package de.mvitz.bsbt;

import java.util.Objects;

public final class Person {

    private String name;
    private int age;
    private String country;

    Person() {}

    public Person(int age) {
        this.age = age;
    }

    public Person(String name, int age) {
        this(age);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isAdult() {
        if ("US".equalsIgnoreCase(country)) {
            return age >= 21;
        }
        return age >= 18;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person other)) {
            return false;
        }
        return Objects.equals(name, other.name)
               && Objects.equals(age, other.age)
               && Objects.equals(country, other.country);
    }
}
