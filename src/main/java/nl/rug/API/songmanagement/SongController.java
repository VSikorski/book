package nl.rug.API.songmanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing songs.
 */
@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    /**
     * Retrieves a list of all songs or a filtered list based on optional parameters.
     *
     * @param limit       Optional parameter to limit the number of songs returned.
     * @param artist      Optional parameter to filter songs by artist.
     * @param albumId     Optional parameter to filter songs by album ID.
     * @param name        Optional parameter to filter songs by name.
     * @param releaseYear Optional parameter to filter songs by release year.
     * @param genre       Optional parameter to filter songs by genre.
     * @return A list of songs that match the given criteria.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Song> getAllSongs(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "albumId", required = false) Integer albumId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "year", required = false) String releaseYear,
            @RequestParam(value = "genre", required = false) String genre) {
        return songService.getAllSongs();
    }

    /**
     * Retrieves a single song by its ID.
     *
     * @param id the ID of the song to retrieve.
     * @return The requested song.
     * @throws  if no song with the provided ID is found.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Song getSongById(@PathVariable int id) {
        return songService.getSongById(id);
    }

    /**
     * Adds a new song based on the provided song data transfer object.
     *
     * @param songDTO the song data transfer object containing the song details.
     * @return The newly created song.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Song addSong(@RequestBody SongDTO songDTO) {
        Song song = new Song(
                songDTO.name(),
                songDTO.artist(),
                songDTO.releasedOn(),
                songDTO.duration(),
                songDTO.genre(),
                songDTO.description());

        return songService.addSong(song);
    }

    /**
     * Updates an existing song with new details provided in the song data transfer object.
     *
     * @param id      the ID of the song to update.
     * @param songDTO the song data transfer object containing the new details.
     * @return The updated song.
     * @throws  if no song with the provided ID is found.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Song updateSong(@PathVariable int id, @RequestBody SongDTO songDTO) {
        Song song = new Song(
                songDTO.name(),
                songDTO.artist(),
                songDTO.releasedOn(),
                songDTO.duration(),
                songDTO.genre(),
                songDTO.description());

        return songService.updateSong(id, song);
    }

    /**
     * Deletes a song by its ID.
     *
     * @param id the ID of the song to delete.
     * @throws if no song with the provided ID is found.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSong(@PathVariable int id) {
        songService.deleteSong(id);
    }

    /**
     * Retrieves all reviews associated with a song by its ID.
     *
     * @param id the ID of the song whose reviews are to be retrieved.
     * @return A list of reviews for the song.
     * @throws if no song with the provided ID is found.
     */
    @GetMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Object getSongReviews(@PathVariable int id) {
        return songService.getSongReviews(id);
    }

    /**
     * Retrieves the average rating of a song by its ID.
     *
     * @param id the ID of the song whose average rating is to be calculated.
     * @return The average rating of the song.
     * @throws if no song with the provided ID is found.
     */
    @GetMapping("/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    public Object getSongRating(@PathVariable int id) {
        return songService.getSongRating(id);
    }
}
