package ru.ochkasovap.Library.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ochkasovap.Library.models.Author;
import ru.ochkasovap.Library.models.Book;
import ru.ochkasovap.Library.repositories.AuthorRepository;
import ru.ochkasovap.Library.repositories.BooksRepository;
import static java.lang.Integer.*;

@Service
@Transactional(readOnly = true)
public class BooksService {
	
	private final BooksRepository booksRepository;

	private final AuthorRepository authorRepository;
	
	@Autowired
	public BooksService(BooksRepository booksRepository, AuthorRepository authorRepository) {
		super();
		this.booksRepository = booksRepository;
		this.authorRepository = authorRepository;
	}

	public List<Book> findAll(String page, String booksPerPage, String sortByYear) {
		if (sortByYear.equals("true") && !isNullParam(page) && !isNullParam(booksPerPage)) {
			return booksRepository.findAll(PageRequest.of(parseInt(page), parseInt(booksPerPage), Sort.by("year"))).getContent();
		} else if (sortByYear.equals("true") && (isNullParam(page) || isNullParam(booksPerPage))) {
			return booksRepository.findAll(Sort.by("year"));
		} else if(!sortByYear.equals("true") && !isNullParam(page) && !isNullParam(booksPerPage)) {
			return booksRepository.findAll(PageRequest.of(parseInt(page), parseInt(booksPerPage))).getContent();
		}
		return booksRepository.findAll();
	}
	public Map<String, Object> findAllWithAttributes(String pageParam, String booksPerPageParam, String sortByYear) {
		Map<String, Object> attributes = new HashMap<>();
		List<Book> books = findAll(pageParam, booksPerPageParam, sortByYear);
		attributes.put("books", books);
		if(!isNullParam(booksPerPageParam)&&!(isNullParam(pageParam))) {
			int page = parseInt(pageParam);
			int booksPerPage = parseInt(booksPerPageParam);
			attributes.put("current_page", (page+1));
			long maxPage = booksRepository.count()/booksPerPage-1;
			attributes.put("max_page", maxPage);
			attributes.put("next_page", (page<maxPage?page+1:page));
			attributes.put("previous_page", (page>0?page-1:page));
		}
		return attributes;
	}
	
	
	public Optional<List<Book>> findBySearchRequest(String request) {
		if(isNullParam(request)) { 
			return Optional.empty();
		}
		return Optional.of(booksRepository.findByNameStartingWith(request));
	}

	public Optional<Book> findOne(int id) {
		return booksRepository.findById(id);
	}

	public Optional<Book> findOne(String name) {
		return booksRepository.findByName(name).stream().findAny();
	}

	@Transactional
	public void save(Book book) {
		book.setAuthor(getAuthor(book));
		booksRepository.save(book);
	}

	@Transactional
	public void update(Book book) {
		save(book);
	}

	@Transactional
	public void delete(int id) {
		Book book = findOne(id).get();
		Author author = book.getAuthor();
		author.removeBook(book);
		if (author.getBooks().isEmpty()) {
			authorRepository.deleteById(author.getId());
		}
	}

	private Author getAuthor(Book book) {
		Optional<Author> author = authorRepository.findByName(book.getAuthor().getName()).stream().findAny();
		if(author.isEmpty()) {
			authorRepository.save(book.getAuthor());
			author = authorRepository.findByName(book.getAuthor().getName()).stream().findAny();
		}
		return author.get();
	}
	private boolean isNullParam(String parameter) {
		return parameter==null||"".equals(parameter);
	}

	
}
