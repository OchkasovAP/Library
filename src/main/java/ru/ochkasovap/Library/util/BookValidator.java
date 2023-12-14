package ru.ochkasovap.Library.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.ochkasovap.Library.models.Author;
import ru.ochkasovap.Library.models.Book;
import ru.ochkasovap.Library.services.BooksService;

@Component
public class BookValidator implements Validator {
	@Autowired
	private BooksService booksService;
	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Book book = (Book) target;
		Optional<Book> bookFromDB = booksService.findOne(book.getName());
		if (bookFromDB.isPresent()) {
			if (book.getId() != bookFromDB.get().getId()) {
				errors.rejectValue("name", null, "Такая книга уже существует");
			}
		}
		Author author = book.getAuthor();
		if(author.getName()==null||"".equals(author.getName())) {
			errors.rejectValue("author.name", null, "Имя автора не может быть пустым");
		}
	}
	
}
