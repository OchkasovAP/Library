package ru.ochkasovap.Library.models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull(message = "Поле не должно быть пустым")
	@Size(min = 1, message = "Поле не должно быть пустым")
	@Column(name = "name")
	private String name;

	@OneToMany (mappedBy = "author", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Book> books;
	
	public Author() {
		books = new LinkedList<>();
	}

	public Author(String name) {
		this();
		this.name = name;
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
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public void addBook(Book book) {
		books.add(book);
		book.setAuthor(this);
	}
	public void removeBook(Book book) {
		books.remove(book);
		book.setAuthor(null);
	}

}
