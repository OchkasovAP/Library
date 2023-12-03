package ru.ochkasovap.Library.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class Book {
	private int id;
	private int userID;
	@NotNull(message = "Поле не должно быть пустым")
	@Size(min = 1, message = "Поле не должно быть пустым")
	private String author;
	
	@NotNull(message = "Поле не должно быть пустым")
	@Size(min = 1, message = "Поле не должно быть пустым")
	private String name;
	
	private int year;

	public Book() {
	}

	public Book(int id, int userID, String author, String name, int year) {
		super();
		this.id = id;
		this.userID = userID;
		this.author = author;
		this.name = name;
		this.year = year;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		if (userID!=null) {
			this.userID = userID;
		} else {
			userID = 0;
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
	public boolean isBusy() {
		return userID!=0;
	}

	@Override
	public String toString() {
		return name + ", " + author + "," + year;
	}
	
}
