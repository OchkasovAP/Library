package ru.ochkasovap.Library.util;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ochkasovap.Library.models.Person;
import ru.ochkasovap.Library.services.PeopleService;

@Component
public class PersonValidator implements Validator {
	@Autowired
	private PeopleService peopleService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		Optional<Person> personFromDB = peopleService.findOne(person.getName());
		if (personFromDB.isPresent()) {
			if (person.getId() != personFromDB.get().getId()) {
				errors.rejectValue("name", null, "Такой пользователь уже существует");
			}
		}
		
		if(person.getYearOfBirth()>Calendar.getInstance().get(Calendar.YEAR)) {
			errors.rejectValue("yearOfBirth", null, "Не корректный год рождения");
		}
	}

}
