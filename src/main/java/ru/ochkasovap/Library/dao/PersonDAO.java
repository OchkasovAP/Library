package ru.ochkasovap.Library.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.ochkasovap.Library.entity.Book;
import ru.ochkasovap.Library.entity.Person;

@Component
public class PersonDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addNewPerson(Person person) {
		jdbcTemplate.update("INSERT INTO person (name, year_of_birth) VALUES (?, ?)", person.getName(),
				person.getYearOfBirth());
	}

	public void deletePerson(int personID) {
		jdbcTemplate.update("DELETE FROM person WHERE id=?", personID);
	}

	public void updatePerson(Person person) {
		jdbcTemplate.update("UPDATE person SET name=?, year_of_birth=? WHERE id=?", person.getName(),
				person.getYearOfBirth(), person.getId());
	}

	public List<Person> getPersons() {
		return jdbcTemplate.query("SELECT id, name, year_of_birth as yearOfBirth FROM person",
				new BeanPropertyRowMapper<>(Person.class));
	}

	public Optional<Person> getPerson(int personID) {
		return jdbcTemplate.query("SELECT id, name, year_of_birth yearOfBirth FROM person WHERE id=?",
				new BeanPropertyRowMapper<>(Person.class), personID).stream().findAny();
	}

	public Optional<Person> getPerson(String personName) {
		return jdbcTemplate.query("SELECT id, name, year_of_birth yearOfBirth FROM person WHERE name=?",
				new BeanPropertyRowMapper<>(Person.class), personName).stream().findAny();
	}

	public List<Book> getPersonsBooks(int personID) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT book.id, user_id userID, author.name author, book.name, year FROM book\n");
		query.append("JOIN author ON author.id = book.author_id\n");
		query.append("WHERE user_id=?");
		return jdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(Book.class), personID);
	}
}
