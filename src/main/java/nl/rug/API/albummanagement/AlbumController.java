package nl.rug.API.albummanagement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AlbumController exposes the endpoints to HTTP requests and performs operations on entities.
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;

    /**
     * Method to retrieve all the albums with optional parameters.
     *
     * @param id The id of the album
     * @param title  The title of the album
     * @param artist The artist of the album
     * @param limit A limit value for the response entities quantity
     * @return ResponseEntity The response code with an album or list or none
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Album> getAllAlbums(@RequestParam(value = "id", required = false) Integer id,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "artist", required = false) String artist,
                                    @RequestParam(value = "limit", required = false) Integer limit) {

        List<Album> albums = new ArrayList<>();

        if (id != null) {
            Album albumById = albumService.getAlbumById(id);
            albums.add(albumById);
            return albums;
        }

        if (title != null) {
            albums.addAll(albumService.getAlbumByTitle(title));
        } else if (artist != null) {
            albums.addAll(albumService.getAlbumByArtist(artist));
        } else {
            albums = albumService.getAllAlbums();
        }

        if (limit != null && albums.size() > limit) {
            albums = albums.subList(0, limit);
        }

        return albums;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album getAlbum(@PathVariable int id) {
        return albumService.getAlbumById(id);
    }

    /**
     * Adds to the repository the passed album.
     *
     * @param albumDTO - the album entity to be saved
     * @return the http response and the album if created successfully
     * Valid annotation will attempt to validate the object, else handleValidationExceptions is called
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Album addAlbum(@RequestBody @Valid AlbumDTO albumDTO) {
        Album album = new Album(albumDTO.title(), albumDTO.artist(), albumDTO.releaseDate());
        return albumService.addAlbum(album);
    }

    /**
     * Updates an album with the new details.
     *
     * @param id The id of the album
     * @param albumDTO Dto of album
     * @return the http response
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album updateAlbum(@PathVariable int id, @RequestBody @Valid AlbumDTO albumDTO) {
        Album album = new Album(albumDTO.title(), albumDTO.artist(), albumDTO.releaseDate());
        return albumService.updateAlbum(id, album);
    }

    /**
     * Deletes an album from the repository.
     *
     * @param id The id of the album
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable int id) {
        albumService.deleteAlbum(id);
    }

    @PostMapping("/{id}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    public Album addSongsToAlbum(@PathVariable int id, @RequestBody int... songs) {
        return albumService.addSongsToAlbum(id, songs);
    }

    @DeleteMapping("/{id}/songs")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSongsFromAlbum(@PathVariable int id, @RequestBody int... songs) {
        albumService.removeSongsFromAlbum(id, songs);
    }

    @GetMapping("/{id}/rating")
    public Object getAlbumRating(@PathVariable int id) {
        return albumService.getAlbumRating(id);
    }

    /**
     * If validation fails, code 400 will be thrown.
     *
     * @param ex - exception reference
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * If no entity was found, code 404 will be thrown.
     *
     * @param ex - exception reference
     * @return ResponseEntity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
