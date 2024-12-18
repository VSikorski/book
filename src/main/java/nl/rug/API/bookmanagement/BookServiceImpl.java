package nl.rug.API.bookmanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.rug.API.reviewmanagement.Review;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Class which implements the service layer of the design and which
 * extends the interface. For method explanations please see the BookService interface.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Book addBook(@RequestBody Book book) {
        // Implementation
        bookRepository.save(book);
        book.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
        book.setAddedOn(new Timestamp(System.currentTimeMillis()));
        return book;
    }

    /**
     * Updates a book.
     * @param id Entity of the Book object from which a book can be searched for.
     * @param updatedBook updatedBook
     * @return Book
     */
    @Override
    @Transactional
    public Book updateBook(int id, Book updatedBook) {
        // Implementation
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Error: Book with id " + id + " not found"));

        book.setName(book.getName());
        book.setAuthor(book.getAuthor());
        book.setPublishedYear(book.getPublishedYear());
        book.setIsbn(book.getIsbn());
        book.setGenre(book.getGenre());
        book.setDescription(book.getDescription());

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(String isbn, Book updatedBook) {
        // Implementation
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(
                () -> new EntityNotFoundException("Error: Book with isbn " + isbn + " not found"));

        book.setName(book.getName());
        book.setAuthor(book.getAuthor());
        book.setPublishedYear(book.getPublishedYear());
        book.setIsbn(book.getIsbn());
        book.setGenre(book.getGenre());
        book.setDescription(book.getDescription());

        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Integer id) {
        // Implementation
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Error: Book with id " + id + " not found"));
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        // Implementation
        return bookRepository.findByIsbn(isbn).orElseThrow(
                () -> new EntityNotFoundException("Error: Book with isbn " + isbn + " not found"));
    }

    @Override
    public Book getBookByName(String name) {
        // Implementation
        return bookRepository.findByNameOrderByAddedOnDesc(name).orElseThrow(
                () -> new EntityNotFoundException("Error: Book with name " + name + " not found"));
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        // Implementation
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        // Implementation
        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<Book> getAllBooks(Integer limit) {
        // Retrieve all books
        List<Book> books = StreamSupport.stream(bookRepository.findAll().spliterator(), false).collect(Collectors.toList());

        // Apply limit if it's specified and greater than 0
        if (limit != null && limit > 0) {
            books = books.stream().limit(limit).collect(Collectors.toList());
        }

        return books;
    }

    @Override
    public Book getRandomBook() {
        // Implementation
        return null;
    }

    @Override
    @Transactional
    public void deleteBook(int id) {
        // Implementation
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBook(String isbn) {
        // Implementation
        bookRepository.deleteBookByIsbn(isbn);
    }

    /**
     * Imports books from a JSON file.
     *
     * @param filePath The file path to the JSON file.
     * @throws IOException If there is an error processing the file.
     */
    @Override
    @Transactional
    public void importBooksJson(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource(filePath);
        List<Book> books = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });

        bookRepository.saveAll(books);
    }

    /**
     * Imports books from a CSV file.
     *
     * @param filePath The file path to the CSV file.
     * @throws IOException            If there is an error processing the file.
     * @throws CsvValidationException If there is an error with the CSV format.
     */
    @Override
    @Transactional
    public void importBooksCsv(String filePath) throws IOException, CsvValidationException {

        Resource resource = resourceLoader.getResource(filePath);
        List<Book> books = new ArrayList<>();
        try (CSVReader csVreader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] line;
            csVreader.readNext();
            while ((line = csVreader.readNext()) != null) {
                Book book = new Book();
                book.setName(line[0]);
                book.setAuthor(line[1]);
                book.setPublishedYear(Integer.parseInt(line[2]));
                book.setIsbn(line[3]);
                book.setGenre(line[4]);
                book.setDescription(line[5]);
                books.add(book);
            }
        }
        bookRepository.saveAll(books);
    }

    /**
     * Exports a list of books to JSON.
     *
     * @param books The list of books to export.
     * @return A JSON string representation of the books.
     * @throws JsonProcessingException If an error occurs during JSON processing.
     */
    public String exportBooksJson(List<Book> books) {
        // Implementation
        try {
            return objectMapper.writeValueAsString(books);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    /**
     * Exports a list of books to CSV.
     *
     * @param books The list of books to export.
     * @return A CSV string representation of the books.
     * @throws IOException If an error occurs during writing to the CSV.
     * @noinspection checkstyle:LineLength
     */
    public String exportBooksCsv(List<Book> books) throws IOException {
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            String[] head = {"id", "name", "author", "publishedYear", "isbn", "genre", "description", "addedOn", "updatedOn"};
            csvWriter.writeNext(head);
            for (Book book : books) {
                String[] input = {String.valueOf(book.getId()), book.getName(), book.getAuthor(), String.valueOf(book.getPublishedYear()), book.getIsbn(), book.getGenre(), book.getDescription(), String.valueOf(book.getAddedOn()), String.valueOf(book.getUpdatedOn())};
                csvWriter.writeNext(input);
            }
        }
        return writer.toString();
    }

    @Override
    public double getBookRating(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error: Song with id " + id + " not found"));

        return book.getReviews().stream().mapToInt(Review::getReviewRating).average().orElse(0.0);
    }
}
