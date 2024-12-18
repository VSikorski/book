package nl.rug.API.albummanagement;

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
import nl.rug.API.songmanagement.Song;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Album class is the representation of the Album entity.
 */
@Entity
@Table(name = "album")
@Getter
@Setter
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false, updatable = false)
    private Timestamp addedOn; //Timestamp of the addition to the Database

    @Column(nullable = false)
    private Timestamp updatedOn; //Timestamp of the last update to the Database

    @OneToMany
    private List<Song> songs = new ArrayList<>();

    /**
     * The constructor of the Album entity:
     *
     * @param title:       The title of the album
     * @param artist:      Name and surname of the artist
     * @param releaseDate: Album release date
     */
    public Album(String title, String artist, LocalDate releaseDate) {
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.addedOn = new Timestamp(System.currentTimeMillis());
        this.updatedOn = new Timestamp(System.currentTimeMillis());
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSongs(Song song) {
        songs.remove(song);
    }
}
