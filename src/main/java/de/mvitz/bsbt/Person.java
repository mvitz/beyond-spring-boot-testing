package de.mvitz.bsbt;

public class Person {

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
}
