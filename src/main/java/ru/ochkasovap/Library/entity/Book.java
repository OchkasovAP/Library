package ru.ochkasovap.Library.entity;

import org.springframework.stereotype.Component;

@Component
public class Book {
	private int id;
	private int userID;
	private String author;
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

	public void setUserID(int userID) {
		this.userID = userID;
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
}
