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
import ru.ochkasovap.Library.util.SortType;

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

	public Map<String, Object> findAllWithAttributes(String pageParam, String booksPerPageParam, SortType sortType) {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("books", findAll(pageParam, booksPerPageParam, Sort.by(sortType.fieldName())));
		attributes.put("sortTypes", List.of(SortType.values()));
		attributes.put("sorted_by", sortType);
		if(!booksPerPageParam.isBlank()&&!pageParam.isBlank()) {
			int page = parseInt(pageParam);
			int booksPerPage = parseInt(booksPerPageParam);
			double maxPage =Math.ceil((double)booksRepository.count()/booksPerPage)-1;
			attributes.putAll(Map.of("current_page", (page+1),
										"max_page", maxPage,
										"next_page", (page<maxPage?page+1:page),
										"previous_page", (page>0?page-1:page)));
		}
		return attributes;
	}
	
	
	public Optional<List<Book>> findBySearchRequest(String request) {
		if(request.isBlank()) { 
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
	
	private List<Book> findAll(String page, String booksPerPage, Sort sort) {
		if (!page.isBlank() && !booksPerPage.isBlank()) {
			return booksRepository.findAll(PageRequest.of(parseInt(page), parseInt(booksPerPage), sort)).getContent();
		} else {
			return booksRepository.findAll(sort);
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
}
