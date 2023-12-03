package ru.ochkasovap.Library.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.ochkasovap.Library.entity.Author;
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
		Object userID = book.getUserID() != 0 ? book.getUserID() : null;
		jdbcTemplate.update("UPDATE book SET name=?, author_id=?, year=?, user_id=? WHERE id=?", book.getName(),
				authorID, book.getYear(), userID, book.getId());
	}

	public List<Book> getBooks() {
		StringBuilder query = new StringBuilder(
				"SELECT b.id, b.user_id userID, b.name, a.name author, b.year FROM book b\n");
		query.append("JOIN author a ON a.id = b.author_id");
		return jdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(Book.class));
	}

	public Optional<Book> getBook(int bookID) {
		return jdbcTemplate.query(createQuery("WHERE b.id=?"), new BeanPropertyRowMapper<>(Book.class), bookID).stream().findAny();
	}

	public Optional<Book> getBook(String bookName) {
		return jdbcTemplate.query(createQuery("WHERE b.name=?"), new BeanPropertyRowMapper<>(Book.class), bookName).stream().findAny();
	}
	
	private String createQuery(String searchCondition) {
		StringBuilder query = new StringBuilder(
				"SELECT b.id, b.user_id userID, b.name, a.name author, b.year FROM book b\n");
		query.append("JOIN author a ON a.id = b.author_id\n");
		query.append(searchCondition);
		return query.toString();
	}

	private Integer getAuthorID(String authorName) {
		for (;;) {
			Optional<Author> authors = jdbcTemplate.query("SELECT id, name FROM author WHERE name = ?",
					new BeanPropertyRowMapper<>(Author.class), authorName).stream().findAny();
			if (authors.isPresent()) {
				return authors.get().getId();
			}
			jdbcTemplate.update("INSERT INTO author (name) VALUES (?)", authorName);
		}
	}

}
