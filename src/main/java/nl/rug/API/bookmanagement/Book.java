package nl.rug.API.bookmanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rug.API.reviewmanagement.Review;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Book object.
 */
@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Automatically generated ID for the book in the database (Primary)

    @Column(nullable = false)
    private String name; //Name and or title of the book

    @Column(nullable = false)
    private String author; //Author of the book

    @Column(nullable = false)
    private int publishedYear; //Year of publishing

    @Column(nullable = false)
    private String isbn; //Unique ID for a book

    @Column(nullable = false)
    private String genre; //Genre of the book

    @Column(nullable = false)
    private String description; //Short description of the book

    @Column(nullable = false, updatable = false)
    private Timestamp addedOn; //Timestamp of the addition to the Database

    @Column(nullable = false)
    private Timestamp updatedOn; //Timestamp of the last update to the Database

    @OneToMany
    private List<Review> reviews = new ArrayList<>();

    /**
     * Constructor.
     * @param name Name and or title of the book
     * @param author Author of the book
     * @param publishedYear Year of publishing
     * @param isbn Unique ID for a book
     * @param genre Genre of the book
     * @param description Short description of the book
     */
    public Book(String name,
                String author,
                int publishedYear,
                String isbn,
                String genre,
                String description) {
        this.name = name;
        this.author = author;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.genre = genre;
        this.description = description;
        this.addedOn = new Timestamp(System.currentTimeMillis());
        this.updatedOn = new Timestamp(System.currentTimeMillis());
    }
}
