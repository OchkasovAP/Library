package ru.ochkasovap.Library.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.ochkasovap.Library.dao.BookDAO;
import ru.ochkasovap.Library.entity.Book;
import ru.ochkasovap.Library.entity.Person;

@Component
public class BookValidator implements Validator {
	@Autowired
	private BookDAO bookDAO;
	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Book book = (Book) target;
		Optional<Book> bookFromDB = bookDAO.getBook(book.getName());
		if (bookFromDB.isPresent()) {
			if (book.getId() != bookFromDB.get().getId()) {
				errors.rejectValue("name", null, "Такая книга уже существует");
			}
		}
	}
	
}
