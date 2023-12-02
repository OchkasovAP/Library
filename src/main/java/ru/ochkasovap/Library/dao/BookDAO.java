package ru.ochkasovap.Library.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.ochkasovap.Library.entity.Book;

@Component
public class BookDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addBook(Book book) {
		Integer authorID = getAuthorID(book.getAuthor());
		jdbcTemplate.update("INSERT INTO book (name, author_id, year) VALUES (?, ?, ?)", book.getName(), authorID,
				book.getYear());
	}
	public void deleteBook(int bookID) {
		jdbcTemplate.update("DELETE FROM book WHERE id=?", bookID);
	}
	public void updateBook(Book book) {
		Integer authorID = getAuthorID(book.getAuthor());
		jdbcTemplate.update("UPDATE book SET name=?, author_id=?, year=? WHERE id=?", book.getName(), authorID,
				book.getYear(), book.getId());
	}
	public List<Book> getBooks() {
		return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
	}
	public Book getBook(int bookID) {
		//TODO validation
		Optional<Book> book = jdbcTemplate.query("SELECT * FROM book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), bookID).stream().findAny();
		return book.get();
	}

	private Integer getAuthorID(String authorName) {
		//TODO придумать условие while
		while(true) { 
			Optional<Integer> authors = jdbcTemplate.query("SELECT id FROM author WHERE name = ?", new BeanPropertyRowMapper<>(Integer.class), authorName).stream().findAny();
			if(authors.isPresent()){
				return authors.get();
			}
			jdbcTemplate.update("INSERT INTO author (name) VALUES (?)", authorName);
		}
	}
	
}
