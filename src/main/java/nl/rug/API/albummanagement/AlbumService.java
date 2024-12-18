package nl.rug.API.albummanagement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nl.rug.API.reviewmanagement.Review;
import nl.rug.API.songmanagement.Song;
import nl.rug.API.songmanagement.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * The AlbumService class provides business logic for managing Albums in the Album Management API.
 */
@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    /**
     * Method to retrieve the album by id.
     *
     * @param id - the id of the album
     * @return the album entity (if found)
     * @throws EntityNotFoundException if the album with the given id is not found
     */
    public Album getAlbumById(int id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Album with id " + id + " not found"));
    }

    /**
     * Method to retrieve the album by title.
     *
     * @param title - the title of the album
     * @return the list of album entities (if found)
     */
    public List<Album> getAlbumByTitle(String title) {
        List<Album> albums = albumRepository.findByTitle(title);
        if (albums.isEmpty()) {
            throw new EntityNotFoundException("Error: No albums found with the title " + title);
        }
        return albums;
    }

    /**
     * Method to retrieve the album by artist.
     *
     * @param artist - the artist of the album
     * @return the list of album entities (if found)
     */
    public List<Album> getAlbumByArtist(String artist) {
        List<Album> albums = albumRepository.findByArtist(artist);
        if (albums.isEmpty()) {
            throw new EntityNotFoundException("Error: No albums found of the artist " + artist);
        }
        return albums;
    }

    /**
     * Method to retrieve all the albums from the database.
     *
     * @return the list of album entities
     */
    public List<Album> getAllAlbums() {
        return StreamSupport.stream(albumRepository.findAll().spliterator(), false).toList();
    }

    /**
     * Adds an album to the repository.
     *
     * @param album - album to be saved
     * @return the saved album
     * @throwsexception if process was not successful
     */
    @Transactional
    public Album addAlbum(Album album) {
        return albumRepository.save(album);
    }

    /**
     * Updates an album in the repository.
     *
     * @param id           - the id of the album to update
     * @param updatedAlbum - the new album details
     * @return the updated album
     * @throws EntityNotFoundException exception if album is not found
     */
    @Transactional
    public Album updateAlbum(int id, Album updatedAlbum) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Album with id " + id + " not found"));

        album.setArtist(updatedAlbum.getArtist());
        album.setTitle(updatedAlbum.getTitle());
        album.setReleaseDate(updatedAlbum.getReleaseDate());

        return albumRepository.save(album);
    }

    /**
     * Deletes an album from the repository.
     *
     * @param id - the id of the album to delete
     * @throws EntityNotFoundException if the album with the given id is not found
     */
    public void deleteAlbum(int id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
        }

        throw new EntityNotFoundException("Error: Album with id " + id + " not found");
    }

    /**
     * Adds one or more songs to an album.
     *
     * @param id The ID of the album to which songs will be added.
     * @param songIds The IDs of the songs to add to the album.
     * @return The updated album with the new songs added.
     * @throws EntityNotFoundException If the album with the specified ID does not exist.
     */
    @Transactional
    public Album addSongsToAlbum(int id, int... songIds) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Album with id " + id + " not found"));

        Iterable<Song> songs = songRepository.findAllById(Arrays.stream(songIds).boxed().toList());
        songs.forEach(album::addSong);

        return albumRepository.save(album);
    }

    /**
     * Removes one or more songs from an album.
     *
     * @param id The ID of the album from which songs will be removed.
     * @param songIds The IDs of the songs to remove from the album.
     * @throws EntityNotFoundException If the album with the specified ID does not exist.
     */
    @Transactional
    public void removeSongsFromAlbum(int id, int... songIds) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Album with id " + id + " not found"));

        Iterable<Song> songs = songRepository.findAllById(Arrays.stream(songIds).boxed().toList());
        songs.forEach(album::removeSongs);

        albumRepository.save(album);
    }

    /**
     * Calculates the average rating of an album based on the ratings of its songs' reviews.
     *
     * @param id The ID of the album for which the average rating is calculated.
     * @return The average rating of the album, or 0.0 if there are no reviews.
     * @throws EntityNotFoundException If the album with the specified ID does not exist.
     */
    public double getAlbumRating(int id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: Album with id " + id + " not found"));

        return album.getSongs().stream()
                .map(Song::getReviews)
                .flatMap(List::stream)
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);
    }
}
