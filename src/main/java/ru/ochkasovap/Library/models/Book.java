package ru.ochkasovap.Library.models;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.tools.javac.util.List;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotNull(message = "Поле не должно быть пустым")
	@Size(min = 1, message = "Поле не должно быть пустым")
	@Column(name = "name")
	private String name;

	@Column(name = "year")
	private int year;

	@Column(name = "time_of_getting")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeOfGetting;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Person person;

	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private Author author;

	public Book() {
	}

	public Book(String name, int year) {
		super();
		this.name = name;
		this.year = year;
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		if (person != null) {
			if (!person.getBooks().contains(this)) {
				person.addBook(this);
			}
		} else if(this.person != null){
			this.person.removeBook(this);
		}
		this.person = person;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Date getTimeOfGetting() {
		return timeOfGetting;
	}

	public void setTimeOfGetting(Date timeOfGetting) {
		this.timeOfGetting = timeOfGetting;
	}
	public boolean isOverdue() {
		long tenDaysAgo = new Date().getTime()-1000*60*60*24*10;
		return timeOfGetting.getTime()<tenDaysAgo;
	}

	@Override
	public String toString() {
		return name + ", " + author.getName() + "," + year;
	}

}
