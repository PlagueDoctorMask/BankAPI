package ru.gb.Client.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * id - идентификатор, уникальный, обязательный к заполнению, присваевается автоматически
 * fullName - ФИО - обязательный к заполнению
 * birthdate - дата рождения, обязательная к заполнению
 * phoneNumber - номер телефона, обязательный к заполнению
 * extraPhoneNumber - дополнительный номер телефона
 * email - почта, обязательная к заполнению
 * extraEmail - дополнительная почта
 * login - логин, обязательный к заполнению
 * password - пароль, обязательный к заполнению
 * account_number - номер счёта, присваевается автоматически, обязательный к заполнению, равен уникальному случайному значению
 * accountBalance - баланс счёта, присваевается автоматически, обязательный к заполнению, равен 500
 */

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String birthdate;

    @Column(nullable = false)
    private String phoneNumber;

    @Column()
    private String extraPhoneNumber;

    @Column(nullable = false)
    private String email;

    @Column()
    private String extraEmail;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private long accountNumber;

    @Column(nullable = false)
    private BigDecimal accountBalance;

    /**
     * Метод возвращения возраста по дате рождения
     * @return - Возраст
     */
    public int getAge(){
        LocalDate date = LocalDate.parse(this.getBirthdate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int year = date.getYear();
        return 2024 - year;
    }
}
