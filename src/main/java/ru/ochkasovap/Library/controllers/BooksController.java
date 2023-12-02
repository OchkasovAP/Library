package ru.ochkasovap.Library.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.ochkasovap.Library.dao.BookDAO;
import ru.ochkasovap.Library.dao.PersonDAO;
import ru.ochkasovap.Library.entity.Book;

@Controller
@RequestMapping("/books")
public class BooksController {
	@Autowired
	private BookDAO bookDAO;
	@Autowired
	private PersonDAO personDAO;
	
	@GetMapping()
	public String showBooks(Model model) {
		model.addAttribute("books", bookDAO.getBooks());
		return "books/allBooks";
	}
	
	@GetMapping("/new")
	public String createForm(@ModelAttribute("book") Book book) {
		return "books/newBook";
	}
	@PostMapping()
	public String createPerson(@ModelAttribute("book") Book book) {
		bookDAO.addBook(book);
		return "redirect:/books";
	}
	
	@GetMapping("/{id}")
	public String showBookInfo(@PathVariable("id") int bookID, Model model) {
		Book book = bookDAO.getBook(bookID);
		model.addAttribute("book", book);
		model.addAttribute("people", personDAO.getPersons());
		if(book.isBusy()) {
			model.addAttribute("person", personDAO.getPerson(book.getUserID()));
		}
		return "books/bookInfo";
	}
	
	@GetMapping("/{id}/edit")
	public String editBookView(@PathVariable("id") int bookID, Model model) {
		model.addAttribute("book", bookDAO.getBook(bookID));
		return "books/editBook";
	}
	@PatchMapping()
	public String editBook(@ModelAttribute("book") Book book) {
		bookDAO.updateBook(book);
		return "redirect:/books";
	}
	
	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable("id") int bookID) {
		bookDAO.deleteBook(bookID);
		return "redirect:/books";
	}
}
