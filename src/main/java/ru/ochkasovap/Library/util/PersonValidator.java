package ru.ochkasovap.Library.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.ochkasovap.Library.dao.PersonDAO;
import ru.ochkasovap.Library.entity.Person;

@Component
public class PersonValidator implements Validator {
	@Autowired
	private PersonDAO personDAO;

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		Optional<Person> personFromDB = personDAO.getPerson(person.getName());
		if (personFromDB.isPresent()) {
			if (person.getId() != personFromDB.get().getId()) {
				errors.rejectValue("name", null, "Такой пользователь уже существует");
			}
		}
	}

}
