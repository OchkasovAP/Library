package ru.ochkasovap.Library.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ru.ochkasovap.Library.models.Author;
import ru.ochkasovap.Library.models.Book;
import ru.ochkasovap.Library.models.Person;
import ru.ochkasovap.Library.repositories.AuthorRepository;
import ru.ochkasovap.Library.repositories.BooksRepository;

public class BooksServiceTest {
	private BooksService service;
	private BooksRepository booksRepository;
	private AuthorRepository authorRepository;
	private List<Book> expectedList;
	private List<Author> authors;
	private List<Person> persons;

	@Before
	public void configService() {
		booksRepository = mock(BooksRepository.class);
		authorRepository = mock(AuthorRepository.class);
		service = new BooksService(booksRepository, authorRepository);
		createExpectedList();
	}

	public void createPersonList() {
		persons = new ArrayList<>();
		int yearOfBirth = 1990;
		for (int i = 1; i < 4; i++) {
			Person person = new Person("Person" + i, yearOfBirth += 5);
			person.setId(i);
			persons.add(person);
		}
	}

	public void createAuthorList() {
		authors = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			Author author = new Author("Author" + i);
			author.setId(i);
			authors.add(author);
		}
	}

	public void createExpectedList() {
		createAuthorList();
		createPersonList();
		expectedList = new ArrayList<>();
		int year = 2000;
		for (int i = 1; i < 11; i++) {
			Book book = new Book("Book" + i, year--);
			book.setId(i);
			book.setAuthor(authors.get(i % 5));
			if (i % 2 == 0) {
				book.setPerson(persons.get(i % 3));
			}
			expectedList.add(book);
		}

	}

	@Test
	public void findAttributesWithouParams() {
		List<Book> books = expectedList;
		when(booksRepository.findAll()).thenReturn(books);
		Map<String, Object> actual = service.findAllWithAttributes("", "", "");
		Map<String, Object> expected = new HashMap<>();
		expected.put("books", books);
		assertEquals(expected, actual);
	}

	@Test
	public void findAtributesWithPageAndPerPage() {
		@SuppressWarnings("unchecked")
		Page<Book> page = mock(Page.class);
		List<Book> books = expectedList.stream().limit(2).toList();
		when(booksRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);
		when(booksRepository.count()).thenReturn((long) expectedList.size());
		when(page.getContent()).thenReturn(books);
		Map<String, Object> actual = service.findAllWithAttributes("0", "2", "");
		Map<String, Object> expected = new HashMap<>();
		expected.put("books", books);
		expected.put("current_page", 1);
		expected.put("max_page", 4L);
		expected.put("next_page", 1);
		expected.put("previous_page", 0);
		assertEquals(expected, actual);
	}

	@Test
	public void findAllBooksSortByYear() {
		List<Book> sortedBooks = expectedList.stream().sorted(Comparator.comparing(b -> b.getYear())).toList();
		when(booksRepository.findAll(Sort.by("year"))).thenReturn(sortedBooks);
		Map<String, Object> actual = service.findAllWithAttributes("", "", "true");
		Map<String, Object> expected = new HashMap<>();
		expected.put("books", sortedBooks);
		assertEquals(expected, actual);
	}

	@Test
	public void findAtributesWithPageAndPerPageSortByYear() {
		@SuppressWarnings("unchecked")
		Page<Book> page = mock(Page.class);
		List<Book> books = expectedList.stream().sorted(Comparator.comparing(b -> b.getYear()))
				.filter(b -> expectedList.indexOf(b) > 5).limit(2).toList();
		when(booksRepository.findAll(PageRequest.of(3, 2, Sort.by("year")))).thenReturn(page);
		when(booksRepository.count()).thenReturn(10L);
		when(page.getContent()).thenReturn(books);
		Map<String, Object> actual = service.findAllWithAttributes("3", "2", "true");
		Map<String, Object> expected = new HashMap<>();
		expected.put("books", books);
		expected.put("current_page", 4);
		expected.put("max_page", 4L);
		expected.put("next_page", 4);
		expected.put("previous_page", 2);
		assertEquals(expected, actual);
	}

	@Test
	public void findByNullSearchRequest() {
		assertFalse(service.findBySearchRequest(null).isPresent());
		assertFalse(service.findBySearchRequest("").isPresent());
	}

	@Test
	public void findBySearchBook1() {

	}

	@Test
	public void deleteTest() {
		Book book = expectedList.get(0);
		Author author = new Author("TestAuthor");
		book.setAuthor(author);
		when(booksRepository.findById(0)).thenReturn(Optional.of(book));
		service.delete(0);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				authors.remove(author);
				assertFalse(authors.contains(author));
				return null;
			}
		}).when(authorRepository).deleteById(author.getId());
		assertNull(book.getAuthor());
		assertFalse(author.getBooks().contains(book));
	}

	@Test
	public void saveTest() {
		Book book = new Book("NewBook", 2000);
		book.setId(0);
		Author author = new Author("TestAuthor");
		author.setId(0);
		book.setAuthor(author);
		when(authorRepository.save(author)).then(o -> {
			authors.add(author);
			return null;
		});
		when(booksRepository.save(book)).then(o -> {
			expectedList.add(book);
			return null;
		});
		when(authorRepository.findByName(author.getName()))
		.thenAnswer(o -> {
			if(authors.contains(author)) {
				return List.of(author);
			}
			return Collections.emptyList();
		});
		when(booksRepository.save(book)).thenReturn(book);
		service.save(book);
		assertTrue(authors.contains(author));
		assertTrue(expectedList.contains(book));
	}

}
