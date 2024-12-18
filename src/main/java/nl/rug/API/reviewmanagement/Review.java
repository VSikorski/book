package nl.rug.API.reviewmanagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents a review entity in the Review Management API.
 * This entity stores all relevant data about reviews, including the type of media being reviewed,
 * the textual content of the review, and associated metadata like the author and timestamps.
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String reviewFullText;

    @Column(nullable = false)
    private String reviewDescription;

    @Column(nullable = false)
    private String affiliation;

    @Column(nullable = false)
    private Integer reviewRating;

    @Column(nullable = false)
    private Integer mediaId;

    @Column(nullable = false)
    private String mediaTitle;

    @Column(nullable = false, updatable = false)
    private Timestamp addedOn; // Timestamp of the addition to the Database

    @Column(nullable = false)
    private Timestamp updatedOn; // Timestamp of the last update to the Database

    /**
     * Constructs a new Review with the specified details.
     * The timestamps for addedOn and updatedOn are set to the current system time upon creation.
     * @param reviewType The type of the media being reviewed, e.g., BOOK or SONG.
     * @param title The title of the review.
     * @param author The author of the review.
     * @param reviewFullText The complete textual content of the review.
     * @param reviewDescription A brief description or summary of the review.
     * @param affiliation The affiliation of the review author, if any.
     * @param reviewRating The numeric rating given in the review.
     * @param mediaId The ID of the media item being reviewed.
     * @param mediaTitle The title of the media item being reviewed.
     * @noinspection checkstyle:ParameterNumber
     */
    public Review(ReviewType reviewType,
                  String title,
                  String author,
                  String reviewFullText,
                  String reviewDescription,
                  String affiliation,
                  Integer reviewRating,
                  Integer mediaId,
                  String mediaTitle) {
        this.reviewType = reviewType;
        this.title = title;
        this.author = author;
        this.reviewFullText = reviewFullText;
        this.reviewDescription = reviewDescription;
        this.affiliation = affiliation;
        this.reviewRating = reviewRating;
        this.mediaId = mediaId;
        this.mediaTitle = mediaTitle;
        this.addedOn = new Timestamp(System.currentTimeMillis());
        this.updatedOn = new Timestamp(System.currentTimeMillis());
    }
}
