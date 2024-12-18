package nl.rug.API.bookmanagement;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface containing the repository layer of the Design pattern. It extends the already existing CrudRepository methods.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    /**
     * Method to find a book or a list of books by the author parameter
     *
     * @param author: Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    List<Book> findByAuthor(String author);

    /**
     * Method to find a book or a list of books by the genre parameter
     *
     * @param genre: Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    List<Book> findByGenre(String genre);

    /**
     * Method to find a book or a list of books by the isbn parameter
     *
     * @param isbn: Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Method to find a book or a list of books by the name parameter
     *
     * @param name: Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    Optional<Book> findByNameOrderByAddedOnDesc(String name);

    void deleteBookByIsbn(String isbn);
}
