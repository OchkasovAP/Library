package ru.ochkasovap.Library.entity;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Component
public class Person {
	private static final Calendar today = Calendar.getInstance();
	private static final long actualYear = today.get(Calendar.YEAR);
	private int id;
	
	@NotNull(message = "Поле ФИО не должно быть пустым")
	@Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "Поле должно соответствовать шаблону \"Фамилия Имя Отчество\"")
	private String name;
	
	@NotNull(message = "Поле \"год\" не должно быть пустым")
	@Min(value = 1900, message = "Не корректный год рождения")
	@Max(value = 2023, message = "Не корректный год рождения")
	private int yearOfBirth;
	
	public Person() {
	}

	public Person(int id, String name, int yearOfBirth) {
		super();
		this.id = id;
		this.name = name;
		this.yearOfBirth = yearOfBirth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	@Override
	public String toString() {
		return name + ", " + yearOfBirth;
	}
	
}
