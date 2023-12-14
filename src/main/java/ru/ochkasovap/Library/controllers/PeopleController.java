package ru.ochkasovap.Library.controllers;

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

import javax.validation.Valid;
import ru.ochkasovap.Library.models.Person;
import ru.ochkasovap.Library.services.PeopleService;
import ru.ochkasovap.Library.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PeopleController {
	@Autowired
	private PeopleService peopleService;
	@Autowired
	private PersonValidator validator;
	
	@GetMapping()
	public String showPeople(Model model) {
		model.addAttribute("people", peopleService.findAll());
		return "people/allPeople";
	}
	
	@GetMapping("/new")
	public String createForm(@ModelAttribute("person") Person person) {
		return "people/newPerson";
	}
	@PostMapping()
	public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		validator.validate(person, bindingResult);
		if(bindingResult.hasErrors()) {
			return "people/newPerson";
		}
		peopleService.save(person);
		return "redirect:/people";
	}
	
	@GetMapping("/{id}")
	public String showPerson(@PathVariable("id") int personID, Model model) {
		model.addAttribute("person", peopleService.findOne(personID).get());
		model.addAttribute("personBooks", peopleService.findPersonBooks(personID));
		return "people/personInfo";
	}
	
	@GetMapping("/{id}/edit")
	public String editPersonView(@PathVariable("id") int personID, Model model) {
		model.addAttribute("person", peopleService.findOne(personID).get());
		return "people/editPerson";
	}
	@PatchMapping()
	public String editPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
		validator.validate(person, bindingResult);
		if(bindingResult.hasErrors()) {
			return "people/editPerson";
		}
		peopleService.update(person);
		return "redirect:/people/"+person.getId();
	}
	@DeleteMapping("/{id}")
	public String deletePerson(@PathVariable("id") int personID) {
		peopleService.delete(personID);
		return "redirect:/people";
	}
}
