package nl.rug.API.songmanagement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nl.rug.API.reviewmanagement.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Provides service methods for managing songs.
 */
@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;

    /**
     * Retrieves a song by its ID.
     *
     * @param id the ID of the song
     * @return the found song
     * @throws EntityNotFoundException if no song with the given ID is found
     */
    public Song getSongById(int id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Song with id " + id + " not found"));
    }

    /**
     * Retrieves all songs in the database.
     *
     * @return a list of all songs
     */
    public List<Song> getAllSongs() {
        return StreamSupport.stream(songRepository.findAll().spliterator(), false).toList();
    }

    /**
     * Adds a new song to the database with current timestamps for addedOn and updatedOn fields.
     *
     * @param song the song to add
     * @return the added song
     */
    @Transactional
    public Song addSong(Song song) {
        song.setAddedOn(new Timestamp(System.currentTimeMillis()));
        song.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
        return songRepository.save(song);
    }

    /**
     * Updates an existing song with new details.
     *
     * @param id the ID of the song to update
     * @param updatedSong contains the new song details
     * @return the updated song
     * @throws EntityNotFoundException if no song with the given ID is found
     */
    @Transactional
    public Song updateSong(int id, Song updatedSong) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Song with id " + id + " not found"));

        song.setName(updatedSong.getName());
        song.setArtist(updatedSong.getArtist());
        song.setReleasedOn(updatedSong.getReleasedOn());
        song.setDuration(updatedSong.getDuration());
        song.setGenre(updatedSong.getGenre());
        song.setDescription(updatedSong.getDescription());

        return songRepository.save(song);
    }

    /**
     * Deletes a song by its ID.
     *
     * @param id the ID of the song to delete
     * @throws EntityNotFoundException if no song with the given ID is found
     */
    public void deleteSong(int id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Error: Song with id " + id + " not found");
        }
    }

    /**
     * Retrieves reviews for a specific song.
     *
     * @param id the ID of the song
     * @return a list of reviews for the song
     * @throws EntityNotFoundException if no song with the given ID is found
     */
    public List<Review> getSongReviews(int id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Song with id " + id + " not found"));
        return song.getReviews();
    }

    /**
     * Calculates the average rating of a song based on its reviews.
     *
     * @param id the ID of the song
     * @return the average rating of the song
     * @throws EntityNotFoundException if no song with the given ID is found
     */
    public double getSongRating(int id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Song with id " + id + " not found"));

        return song.getReviews().stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);
    }
}
