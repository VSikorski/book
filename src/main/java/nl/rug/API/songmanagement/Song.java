package nl.rug.API.songmanagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rug.API.reviewmanagement.Review;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Song object.
 */
@Entity
@Table(name = "song")
@Getter
@Setter
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private Timestamp releasedOn;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private Timestamp addedOn;

    @Column(nullable = false)
    private Timestamp updatedOn;

    @OneToMany
    private List<Review> reviews = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param name        Name and or title of the song
     * @param artist      Artist/artists of the song
     * @param releasedOn  Release date of the song
     * @param duration    Duration of the song
     * @param genre       Genre of the song
     * @param description Short description of the song
     */
    public Song(String name, String artist, Timestamp releasedOn, int duration, String genre, String description) {
        this.name = name;
        this.artist = artist;
        this.releasedOn = releasedOn;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.addedOn = new Timestamp(System.currentTimeMillis());
        this.updatedOn = new Timestamp(System.currentTimeMillis());
    }
}
