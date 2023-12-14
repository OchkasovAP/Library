package ru.ochkasovap.Library.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ochkasovap.Library.models.Book;
import ru.ochkasovap.Library.models.Person;
import ru.ochkasovap.Library.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {

	private final PeopleRepository peopleRepository;
	
	@Autowired
	public PeopleService(PeopleRepository peopleRepository) {
		super();
		this.peopleRepository = peopleRepository;
	}

	public List<Person> findAll() {
		return peopleRepository.findAll();
	}

	public Optional<Person> findOne(int id) {
		return peopleRepository.findById(id);
	}

	public Optional<Person> findOne(String name) {
		return peopleRepository.findByName(name).stream().findAny();
	}

	public List<Book> findPersonBooks(int personId) {
		Person person = peopleRepository.findById(personId).get();	
		return new ArrayList<>(person.getBooks());
	}

	@Transactional
	public void save(Person person) {
		peopleRepository.save(person);
	}

	@Transactional
	public void update(Person person) {
		save(person);
	}

	@Transactional
	public void delete(int id) {
		peopleRepository.deleteById(id);
	}
}
