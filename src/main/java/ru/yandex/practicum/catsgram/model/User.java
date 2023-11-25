package ru.yandex.practicum.catsgram.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class User {
    private String email;
    private String nickname;
    private LocalDate birthdate;

    public User(String email, String nickname, LocalDate birthdate) {
        this.email = email;
        this.nickname = nickname;
      //  this.birthdate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        try {
            this.birthdate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода даты деньрождения пользователя");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
