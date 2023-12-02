package ru.ochkasovap.Library.controllers;

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

import ru.ochkasovap.Library.dao.PersonDAO;
import ru.ochkasovap.Library.entity.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {
	@Autowired
	private PersonDAO personDAO;
	
	@GetMapping()
	public String showPeople(Model model) {
		model.addAttribute("people", personDAO.getPersons());
		return "people/allPeople";
	}
	
	@GetMapping("/new")
	public String createForm(@ModelAttribute("person") Person person) {
		return "people/newPerson";
	}
	@PostMapping()
	public String createPerson(@ModelAttribute("person") Person person) {
		personDAO.addNewPerson(person);
		return "redirect:/people";
	}
	
	@GetMapping("/{id}")
	public String showPerson(@PathVariable("id") int personID, Model model) {
		model.addAttribute("person", personDAO.getPerson(personID));
		model.addAttribute("personBooks", personDAO.getPersonsBooks(personID));
		return "people/personInfo";
	}
	
	@GetMapping("/{id}/edit")
	public String editPersonView(@PathVariable("id") int personID, Model model) {
		model.addAttribute("person", personDAO.getPerson(personID));
		return "people/editPerson";
	}
	@PatchMapping()
	public String editPerson(@ModelAttribute("person") Person person) {
		personDAO.updatePerson(person);
		return "redirect:/people/"+person.getId();
	}
	@DeleteMapping("/{id}")
	public String deletePerson(@PathVariable("id") int personID) {
		personDAO.deletePerson(personID);
		return "redirect:/people";
	}
}
