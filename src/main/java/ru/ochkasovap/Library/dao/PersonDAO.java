package ru.ochkasovap.Library.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.ochkasovap.Library.entity.Book;
import ru.ochkasovap.Library.entity.Person;

@Component
public class PersonDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void addNewPerson(Person person) {
		jdbcTemplate.update("INSERT INTO person (name, year_of_birth) VALUES (?, ?)", person.getName(), person.getYearOfBirth());
	}
	
	public void deletePerson(int personID) {
		jdbcTemplate.update("DELETE FROM person WHERE id=?", personID);
	}
	
	public void updatePerson(Person person) {
		jdbcTemplate.update("UPDATE person SET name=?, year_of_birth=? WHERE id=?", person.getName(), person.getYearOfBirth(), person.getId());
	}
	
	public List<Person> getPersons() {
		return jdbcTemplate.query("SELECT id, name, year_of_birth yearOfBirth FROM person", new BeanPropertyRowMapper<>(Person.class));
	}
	
	public Person getPerson(int personID) {
		List<Person> persons = jdbcTemplate.query("SELECT id, name, year_of_birth yearOfBirth FROM person WHERE id=?", new BeanPropertyRowMapper<>(Person.class), personID);
		for(Person person: persons) {
			return person;
		}
		throw new RuntimeException("This person don't exist");
	}
	public List<Book> getPersonsBooks(int personID) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT book.id, user_id userID, author.name author, book.name, year FROM book\n");
		query.append("JOIN author ON author.id = book.author_id\n");
		query.append("WHERE user_id=?");
		return jdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(Book.class), personID);
	}
}
