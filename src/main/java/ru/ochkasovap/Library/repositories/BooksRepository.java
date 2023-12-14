package ru.ochkasovap.Library.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ochkasovap.Library.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
	List<Book> findByName(String name);
	List<Book> findByNameStartingWith(String parameter);
}
