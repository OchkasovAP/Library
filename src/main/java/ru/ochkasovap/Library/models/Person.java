package ru.ochkasovap.Library.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull(message = "Поле ФИО не должно быть пустым")
	@Pattern(regexp = "[А-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+", message = "Поле должно соответствовать шаблону \"Фамилия Имя Отчество\"")
	@Column(name = "name")
	private String name;
	
	@NotNull(message = "Поле \"год\" не должно быть пустым")
	@Min(value = 1900, message = "Не корректный год рождения")
	@Column(name = "year_of_birth")
	private int yearOfBirth;
	
	@OneToMany (mappedBy = "person", orphanRemoval = true)
	private List<Book> books;
	
	public Person() {
		books = new LinkedList<>();
	}

	public Person(String name, int yearOfBirth) {
		this();
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
	
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public void addBook(Book book) {
		book.setTimeOfGetting(new Date());
		books.add(book);
		book.setPerson(this);
		
	}
	public void removeBook(Book book) {
		book.setTimeOfGetting(null);
		books.remove(book);
		book.setPerson(null);
	}

	@Override
	public String toString() {
		return name + ", " + yearOfBirth;
	}
	
}
