package com.griddynamics.examples.simple.model;

/**
 * Sample DTO represnts student (potentially))
 */
public class Student {

    private String id;
    private String name;
    private String surname;
    private Short age;
    private Direction direction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public enum Direction{
        QA("Quality Assurance"),
        DEV("Java Developer"),
        DEV_OPS("DevOps engineer"),
        UI("User Interface engineer");

        private String userFriendlyName;

        Direction(String userFriendlyName) {
            this.userFriendlyName = userFriendlyName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        if (surname != null ? !surname.equals(student.surname) : student.surname != null) return false;
        if (age != null ? !age.equals(student.age) : student.age != null) return false;
        return direction == student.direction;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

}
