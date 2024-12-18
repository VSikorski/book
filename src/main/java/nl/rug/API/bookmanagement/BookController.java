package nl.rug.API.bookmanagement;

import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class:
 * Used to establish the communication between the service pattern and the client via https communication.
 *
 * @author vlad/yorrick
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    //Service pattern initialisation
    private final BookService bookService;

    /**
     * <p>
     * Method to receive a list of books that might be limited by optional parameters
     * Endpoint associated with the method: ("/books")
     * <p/>
     *
     * @param author(String): Author of a book and optional parameter to limit the list of books.
     * @param genre(String):  Genre of a book and optional parameter to limit the list of books.
     * @param id(Integer):    ID associated with a book and optional parameter to limit the list of books.
     * @param isbn(String):   ISBN associated with a book and optional parameter to limit the list of books.
     * @param title(String):  Title associated with a book and optional parameter to limit the list of books.
     * @param limit(Integer): An integer that limits the list of book from the GET request.
     * @return List<Book>: A possible return of a list of books.
     */
    @GetMapping
    public List<Book> getAllBooks(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "genre", required = false) String genre) {

        // Initialize an empty list to collect books
        List<Book> books = new ArrayList<>();

        // Check if any filtering parameters are provided
        boolean hasFilters = (id != null || title != null || isbn != null || author != null || genre != null);

        // Collect results based on provided parameters
        if (id != null) {
            books.add(bookService.getBookById(id));
        }

        if (title != null) {
            books.add(bookService.getBookByName(title));
        }

        if (isbn != null) {
            books.add(bookService.getBookByIsbn(isbn));
        }

        if (author != null) {
            books.addAll(bookService.getBooksByAuthor(author));
        }

        if (genre != null) {
            books.addAll(bookService.getBooksByGenre(genre));
        }

        // If no filters were applied, get all books
        if (!hasFilters) {
            books.addAll(bookService.getAllBooks(null)); // Handle Optional correctly
        }

        // Apply limit if provided
        if (limit != null && books.size() > limit) {
            books = books.subList(0, limit);
        }

        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found");
        }

        return books;
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    /**
     * <p>
     * Method to add a book to the DataBase.
     * Endpoint associated with the method: ("/books/add")
     * <p/>
     *
     * @param book(Object): A book object as described in the model directory and the object that will be used in adding.
     * @return Book: return of the object book that was added.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody BookDTO bookDTO) {
        Book book = new Book(
                bookDTO.name(),
                bookDTO.author(),
                bookDTO.publishedYear(),
                bookDTO.isbn(),
                bookDTO.genre(),
                bookDTO.description());

        return bookService.addBook(book);
    }

    @PutMapping("/updateByIsbn/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        Book book = new Book(
                bookDTO.name(),
                bookDTO.author(),
                bookDTO.publishedYear(),
                bookDTO.isbn(),
                bookDTO.genre(),
                bookDTO.description());

        return bookService.updateBook(isbn, book);
    }

    @PutMapping("/updateById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        Book book = new Book(
                bookDTO.name(),
                bookDTO.author(),
                bookDTO.publishedYear(),
                bookDTO.isbn(),
                bookDTO.genre(),
                bookDTO.description());

        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/deleteByIsbn/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable(required = false) String isbn) {
        bookService.deleteBook(isbn);
    }

    @DeleteMapping("/deleteById/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/{id}/rating")
    public double getBookRating(@PathVariable int id) {
        return bookService.getBookRating(id);
    }

    /**
     * <p>
     * Method to import a book(s) to the DataBase.
     * Endpoint associated with the method: ("/books/import")
     * <p/>
     *
     * @param filePath(String): Filepath where the file that needs importing is kept.
     * @param format(String):   The format in which the filepath is written in.
     * @return
     */
    @PostMapping("/import")
    public ResponseEntity<String> importBooks(@RequestParam(value = "file") String filePath, @RequestParam(value = "format") String format) throws IOException, CsvValidationException {
        if (format.equals("csv")) {
            try {
                bookService.importBooksCsv(filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error importing books: " + e.getMessage());
            }
        } else if (format.equals("json")) {
            try {
                bookService.importBooksJson(filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error importing books: " + e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid format specified. Use 'json' or 'csv'");
        }
        return null;
    }

    /**
     * <p>
     * Method to export book(s) from the DataBase.
     * Endpoint associated with the method: ("/books/export")
     * <p/>
     *
     * @param format(String): The format in which the filepath needs to be written in.
     * @return String: List of books in the form of a Json/CSV parsed string.
     */
    @GetMapping("/export")
    public ResponseEntity<String> exportBooks(@RequestParam(value = "format") String format) throws IOException {

        List<Book> books = bookService.getAllBooks(null);
        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No books are stored inside the DB");
        }

        if (format.equals("csv")) {
            String content = bookService.exportBooksCsv(books);
            if (content.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No books are stored inside the DB");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.csv");
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } else if (format.equals("json")) {
            return new ResponseEntity<>(bookService.exportBooksJson(books), HttpStatus.OK);
        }
        return null;
    }
}
