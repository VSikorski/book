package nl.rug.API.bookmanagement;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

/**
 * Interface containing the service layer of the Design pattern.
 */
public interface BookService {
    /**
     * Method to find a book or a list of books by the isbn parameter.
     *
     * @param book Book object which will be added.
     * @return Book object
     */
    Book addBook(@RequestBody Book book);

    /**
     * Method to update a book or a list of books by the id parameter.
     *
     * @param id          Entity of the Book object from which a book can be searched for.
     * @param updatedBook object which contains the updated version.
     * @return Book object
     */
    Book updateBook(int id, Book updatedBook);

    /**
     * Method to update a book or a list of books by the isbn parameter.
     *
     * @param isbn Entity of the Book object from which a book can be searched for
     * @param updatedBook Book object which contains the updated version.
     * @return Book object
     */
    Book updateBook(String isbn, Book updatedBook);

    /**
     * Method to find a book or a list of books by the id parameter
     *
     * @param id Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    Book getBookById(Integer id);

    /**
     * Method to find a book or a list of books by the isbn parameter
     *
     * @param isbn: Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    Book getBookByIsbn(String isbn);

    /**
     * Method to find a book or a list of books by the name parameter
     *
     * @param name: Entity of the Book object from which a book can be searched for.
     * @return List of book(s)
     */
    Book getBookByName(String name);

    /**
     * Method to recieve a list of books by the genre parameter.
     *
     * @param author Entity of the Book object from which a book can be searched for.
     * @return a possible list of book objects.
     */
    List<Book> getBooksByAuthor(String author);

    /**
     * Method to recieve a list of books by the genre parameter.
     *
     * @param genre Entity of the Book object from which a book can be searched for.
     * @return a possible list of book objects.
     */
    List<Book> getBooksByGenre(String genre);

    /**
     * Method to recieve a list of books possibly limited by the limit parameter.
     *
     * @return a possible list of book objects.
     */
    List<Book> getAllBooks(Integer limit);

    /**
     * Method to recieve a random book.
     *
     * @return a possible book object.
     */
    Book getRandomBook();

    /**
     * Method to delete a book by the id parameter
     *
     * @param id: Entity of the Book object from which a book can be searched for.
     */
    void deleteBook(int id);

    /**
     * Method to delete a book by the isbn parameter
     *
     * @param isbn: Entity of the Book object from which a book can be searched for.
     */
    void deleteBook(String isbn);

    /**
     * Method to import a book or a list of books from a json file.
     *
     * @param filePath: json file that need to be imported.
     */
    void importBooksJson(String filePath) throws IOException;

    /**
     * Method to import a book or a list of books from a csv file.
     *
     * @param filePath: csv file that need to be imported.
     */
    void importBooksCsv(String filePath) throws IOException, CsvValidationException;

    /**
     * Method to export a book or a list of books.
     *
     * @param books: List of books that need to be exported.
     * @return list of books in the type String in the format json
     */
    String exportBooksJson(List<Book> books);

    /**
     * Method to export a book or a list of books.
     *
     * @param books: List of books that need to be exported.
     * @return list of books in the type String in the format csv
     */
    String exportBooksCsv(List<Book> books) throws IOException;

    double getBookRating(int id);
}
