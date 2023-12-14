package ru.ochkasovap.Library.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ochkasovap.Library.models.Author;
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{
	List<Author> findByName(String name);
}
