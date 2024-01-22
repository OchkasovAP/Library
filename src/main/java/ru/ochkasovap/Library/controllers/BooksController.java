package ru.ochkasovap.Library.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.ochkasovap.Library.models.Book;
import ru.ochkasovap.Library.models.Person;
import ru.ochkasovap.Library.services.BooksService;
import ru.ochkasovap.Library.services.PeopleService;
import ru.ochkasovap.Library.util.BookValidator;
import ru.ochkasovap.Library.util.SortType;

@Controller
@RequestMapping("/books")
public class BooksController {
	@Autowired
	private BooksService booksService;
	@Autowired
	private PeopleService peopleService;
	@Autowired
	private BookValidator validator;
	
	@GetMapping()
	public String showBooks(Model model,
			@ModelAttribute("page") String page,
			@ModelAttribute("books_per_page") String booksPerPage,
			@RequestParam("sorted_by") SortType sortType) {
		model.addAllAttributes(booksService.findAllWithAttributes(page, booksPerPage, sortType));
		return "books/allBooks";
	}
	@GetMapping("/search")
	public String searchBook(Model model,@ModelAttribute("search_request") String searchRequest) {
		model.addAttribute("booksOptional", booksService.findBySearchRequest(searchRequest));
		return "books/search";
	}
	
	@GetMapping("/new")
	public String createForm(@ModelAttribute("book") Book book) {
		return "books/newBook";
	}
	@PostMapping()
	public String createPerson(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		validator.validate(book, bindingResult);
		if(bindingResult.hasErrors()) {
			return "books/newBook";
		}
		booksService.save(book);
		return "redirect:/books?sorted_by=ID";
	}
	
	@GetMapping("/{id}")
	public String showBookInfo(@PathVariable("id") int bookID, Model model) {
		Book book = booksService.findOne(bookID).get();
		model.addAttribute("book", book);
		model.addAttribute("people", peopleService.findAll());
		Person person = book.getPerson()==null?new Person():book.getPerson();
		model.addAttribute("person", person);
		return "books/bookInfo";
	}
	
	@GetMapping("/{id}/edit")
	public String editBookView(@PathVariable("id") int bookID, Model model) {
		model.addAttribute("book", booksService.findOne(bookID).get());
		return "books/editBook";
	}
	@PatchMapping()
	public String editBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
		validator.validate(book, bindingResult);
		if(bindingResult.hasErrors()) {
			return "books/editBook";
		}	
		booksService.update(book);
		return "redirect:/books?sorted_by=ID";
	}
	
	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable("id") int bookID) {
		booksService.delete(bookID);
		return "redirect:/books?sorted_by=ID";
	}
}
